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

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String adminPanel(@RequestParam(value = "view", defaultValue = "admin") String view,
                             @RequestParam(required = false) Boolean showForm,
                             Model model,
                             @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("users", userService.findAllUsers());
        model.addAttribute("currentUser", userService.getUserByEmail(userDetails.getUsername()));
        model.addAttribute("activeView", view);
        model.addAttribute("allRoles", roleService.findAllRoles());
        model.addAttribute("newUser", new User());
        model.addAttribute("showAddForm", Boolean.TRUE.equals(showForm));

        // Добавьте это - список ролей для формы
        model.addAttribute("roles", roleService.findAllRoles());

        return "admin";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute("newUser") User user,
                          @RequestParam("roles") List<Long> roleIds, // Измените на список ID ролей
                          RedirectAttributes redirectAttributes) {
        try {
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
                             @RequestParam("roles") List<Long> roleIds, // Измените на список ID ролей
                             RedirectAttributes redirectAttributes) {
        try {
            userService.updateUser(id, user, roleIds);
            redirectAttributes.addFlashAttribute("successMessage", "User updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating user: " + e.getMessage());
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
