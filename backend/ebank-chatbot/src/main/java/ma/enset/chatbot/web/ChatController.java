package ma.enset.chatbot.web;
import ma.enset.chatbot.agent.EBankAgent;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/chat")
public class ChatController {
 private final EBankAgent agent;
 public ChatController(EBankAgent agent) { this.agent = agent; }
 @GetMapping(produces = MediaType.TEXT_PLAIN_VALUE)
 public String chat(@RequestParam(defaultValue = "Bonjour") String query) { return agent.chat(query); }
 @PostMapping(consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
 public String ask(@RequestBody String query) { return agent.chat(query); }
}
