package ma.enset.ebank.config;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ma.enset.ebank.services.EBankService;

@Configuration
public class McpToolsConfiguration {

    @Bean
    ToolCallbackProvider ebankTools(EBankService service) {
        return MethodToolCallbackProvider.builder().toolObjects(service).build();
    }
}
