package kata313.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegController {

    @GetMapping("/")
    public String login() {
        return "login-page";
    }
}
