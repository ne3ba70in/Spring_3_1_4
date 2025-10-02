package kata314.controller;

import kata314.dto.RoleDto;
import kata314.service.RoleService;
import kata314.util.UserMapper;
import kata314.dto.UserDto;
import kata314.entity.User;
import kata314.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;
    private final UserMapper userMapper;

    public AdminController(UserService userService, UserMapper userMapper, RoleService roleService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.roleService = roleService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userMapper.toDto(userService.findAllUsers()));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userMapper.toDto(userService.getUserById(id)));
    }

    @PostMapping("/users")
    public ResponseEntity<UserDto> saveUser(@RequestBody User user,
                                            @RequestParam List<Long> roleIds) {
        User savedUser = userService.saveUser(user, roleIds);
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toDto(savedUser));
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id,
                              @RequestBody User updatedUser,
                              @RequestParam List<Long> roleIds) {
        User user = userService.updateUser(id, updatedUser, roleIds);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @GetMapping("/roles")
    public List<RoleDto> getRoles() {
        return roleService.findAllRoles();
    }
}
