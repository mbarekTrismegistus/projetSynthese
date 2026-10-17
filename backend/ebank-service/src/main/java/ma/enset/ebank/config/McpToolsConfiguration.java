package ma.enset.ebank.config;
import ma.enset.ebank.services.EBankService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class McpToolsConfiguration {
 @Bean ToolCallbackProvider ebankTools(EBankService service) {
  return MethodToolCallbackProvider.builder().toolObjects(service).build();
 }
}
