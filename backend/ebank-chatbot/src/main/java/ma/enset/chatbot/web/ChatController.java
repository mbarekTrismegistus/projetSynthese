package ma.enset.chatbot.web;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ma.enset.chatbot.agent.EBankAgent;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final EBankAgent agent;

    public ChatController(EBankAgent agent) {
        this.agent = agent;
    }

    @GetMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    public String chat(@RequestParam(defaultValue = "Bonjour") String query) {
        return agent.chat(query);
    }

    @PostMapping(consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public String ask(@RequestBody String query) {
        return agent.chat(query);
    }
}
