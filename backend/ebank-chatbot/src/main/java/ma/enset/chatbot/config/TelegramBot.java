package ma.enset.chatbot.config;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import ma.enset.chatbot.agent.EBankAgent;

@Component
@ConditionalOnExpression("'${TELEGRAM_BOT_TOKEN:}' != ''")
public class TelegramBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private static final Logger LOG = Logger.getLogger(TelegramBot.class.getName());
    private final String token;
    private final TelegramClient telegram;
    private final EBankAgent agent;

    public TelegramBot(@Value("${TELEGRAM_BOT_TOKEN}") String token, EBankAgent agent) {
        this.token = token;
        this.telegram = new OkHttpTelegramClient(token);
        this.agent = agent;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            try {
                String answer = agent.chat(update.getMessage().getText());
                telegram.execute(SendMessage.builder().chatId(chatId).text(answer).build());
            } catch (TelegramApiException | RuntimeException error) {
                LOG.log(Level.WARNING, "Telegram message could not be handled", error);
            }
        }
    }
}
