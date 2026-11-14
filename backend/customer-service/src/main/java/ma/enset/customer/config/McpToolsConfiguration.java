package ma.enset.customer.config;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ma.enset.customer.services.CustomerService;

@Configuration
public class McpToolsConfiguration {

    @Bean
    ToolCallbackProvider customerTools(CustomerService service) {
        return MethodToolCallbackProvider.builder().toolObjects(service).build();
    }
}
