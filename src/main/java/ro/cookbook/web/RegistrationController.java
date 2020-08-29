package ro.cookbook.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ro.cookbook.domain.User;
import ro.cookbook.service.RegistrationService;

@Controller
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @GetMapping("/register")
    public String displayRegistrationForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "topbar/registration";
    }

    @PostMapping("/createUser")
    public String register(User user, Model model) {
        registrationService.registrer(user);
        return "success";
    }
}
