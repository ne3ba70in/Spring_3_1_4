package kata314.util;

import kata314.dto.RoleDto;
import kata314.dto.UserDto;
import kata314.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    public UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setAge(user.getAge());
        userDto.setRoles(user.getRoles().stream().map(RoleDto::new).collect(Collectors.toList()));
        return userDto;
    }

    public List<UserDto> toDto(List<User> users) {
        return users.stream()
                .map(user -> toDto(user))
                .collect(Collectors.toList());
    }
}
