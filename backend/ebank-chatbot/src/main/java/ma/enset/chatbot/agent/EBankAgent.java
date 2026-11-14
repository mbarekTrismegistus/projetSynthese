package ma.enset.chatbot.agent;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Service;

@Service
public class EBankAgent {

    private final ChatClient chatClient;

    public EBankAgent(ChatClient.Builder builder, ToolCallbackProvider mcpTools) {
        this.chatClient = builder.defaultSystem("You are the eBank assistant. Use customer and bank account tools to answer accurately. Ask for missing identifiers and never invent account data.").defaultToolCallbacks(mcpTools).build();
    }

    public String chat(String query) {
        return chatClient.prompt().user(query).call().content();
    }
}
