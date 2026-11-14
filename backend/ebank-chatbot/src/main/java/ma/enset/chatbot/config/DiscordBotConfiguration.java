package ma.enset.chatbot.config;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ma.enset.chatbot.agent.EBankAgent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

@Configuration
@ConditionalOnExpression("'${DISCORD_BOT_TOKEN:}' != ''")
public class DiscordBotConfiguration {

    @Bean(destroyMethod = "shutdownNow")
    JDA discordBot(@Value("${DISCORD_BOT_TOKEN}") String token, EBankAgent agent) throws InterruptedException {
        JDA jda = JDABuilder.createLight(token, Collections.emptyList())
                .addEventListeners(new DiscordAskListener(agent)).build();
        jda.awaitReady();
        jda.updateCommands().addCommands(Commands.slash("ask", "Ask the eBank assistant")
                .addOption(OptionType.STRING, "query", "Your question about customers and accounts", true)).queue();
        return jda;
    }
}
