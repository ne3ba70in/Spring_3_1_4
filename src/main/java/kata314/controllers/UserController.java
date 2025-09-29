package kata314.controllers;

import kata314.entities.User;
import kata314.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String showUserProfile(@AuthenticationPrincipal UserDetails userDetails,
                                  Model model) {
        User currentUser = userService.getUserByEmail(userDetails.getUsername());
        model.addAttribute("user", currentUser);
        return "user";
    }
}
