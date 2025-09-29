package kata314.controllers;

import kata314.config.dto.UserDto;
import kata314.entities.User;
import kata314.services.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FirstRestController {
    private final UserService userService;

    public FirstRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<UserDto> getAllUsers() {
        return userService.findAllUsersDto();
    }

    @GetMapping("/users/{id}")
    public UserDto getUser(@PathVariable Long id) {
        return userService.getUserDtoById(id);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
    }

    @PostMapping("/users")
    public UserDto createUser(@RequestBody User user, @RequestParam List<Long> roleIds) {
        userService.saveUser(user, roleIds);
        return new UserDto(user);
    }

    @PutMapping("/users/{id}")
    public UserDto updateUser(@PathVariable Long id,
                           @RequestBody User updatedUser,
                           @RequestParam List<Long> roleIds) {
        User user = userService.updateUser(id, updatedUser, roleIds);
        return new UserDto(user);
    }
}
