package kata314.config.dto;

import kata314.entities.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
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
}
