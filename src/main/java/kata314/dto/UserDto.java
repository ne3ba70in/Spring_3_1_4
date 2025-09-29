package kata314.dto;

import kata314.entities.Role;
import kata314.entities.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class UserDto { // для чтения
    private Long id;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private int age;
    private List<RoleDto> roles;

    public UserDto(User user) {
        this.id = user.getId();
        this.password = user.getPassword();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.age = user.getAge();
        this.roles = user.getRoles().stream().map(RoleDto::new).collect(Collectors.toList());
    }

    public UserDto(Long id, String firstName, String lastName, int age, String email, Set<Role> roles) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.roles = roles.stream().map(RoleDto::new).collect(Collectors.toList());
    }
}
