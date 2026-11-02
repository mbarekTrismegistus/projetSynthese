package ma.enset.chatbot.config;

import ma.enset.chatbot.agent.EBankAgent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DiscordAskListener extends ListenerAdapter {
    private static final Logger LOG = Logger.getLogger(DiscordAskListener.class.getName());
    private final EBankAgent agent;
    public DiscordAskListener(EBankAgent agent) { this.agent = agent; }

    @Override public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("ask")) return;
        String query = event.getOption("query").getAsString();
        event.deferReply().queue(hook -> {
            try { hook.editOriginal(agent.chat(query)).queue(); }
            catch (RuntimeException error) {
                LOG.log(Level.WARNING, "Discord message could not be handled", error);
                hook.editOriginal("The assistant could not process that request.").queue();
            }
        });
    }
}
