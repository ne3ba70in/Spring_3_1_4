package kata313.controllers;

import kata313.entities.User;
import kata313.services.RoleService;
import kata313.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

//    @GetMapping("/")
//    public String login() {
//        return "login-page";
//    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String showAdminPage(@RequestParam(value = "view", defaultValue = "admin") String view,
                             @RequestParam(required = false) Boolean showForm,
                             Model model,
                             @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("users", userService.findAllUsers());
        model.addAttribute("currentUser", userService.getUserByEmail(userDetails.getUsername()));
        model.addAttribute("activeView", view);
        model.addAttribute("allRoles", roleService.findAllRoles());
        model.addAttribute("newUser", new User());
        model.addAttribute("showAddForm", Boolean.TRUE.equals(showForm));
        model.addAttribute("roles", roleService.findAllRoles());
        return "admin";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute("newUser") User user,
                          @RequestParam(value = "roles", required = false) List<Long> roleIds,
                          RedirectAttributes redirectAttributes) {
        try {
            if (roleIds == null || roleIds.isEmpty()) {
                roleIds = List.of(1L);
            }
            userService.saveUser(user, roleIds);
            redirectAttributes.addFlashAttribute("successMessage", "Пользователь успешно добавлен!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при добавлении пользователя: " + e.getMessage());
            redirectAttributes.addAttribute("showForm", true);
        }
        return "redirect:/admin?view=admin";
    }

    @PostMapping("/edit/{id}")
    public String updateUser(@PathVariable("id") Long id,
                             @ModelAttribute("user") User user,
                             @RequestParam(value = "roles", defaultValue = "USER", required = false) List<Long> roleIds,
                             RedirectAttributes redirectAttributes) {
        try {
            userService.updateUser(id, user, roleIds);
            redirectAttributes.addFlashAttribute("successMessage", "Редактирование выполнено!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка редактирования пользователя: " + e.getMessage());
        }
        return "redirect:/admin?view=admin";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUserById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Пользователь успешно удален!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при удалении пользователя: " + e.getMessage());
        }
        return "redirect:/admin?view=admin";
    }
}
