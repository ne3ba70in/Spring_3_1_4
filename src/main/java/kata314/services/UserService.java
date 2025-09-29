package kata314.services;

import kata314.config.dto.UserDto;
import kata314.entities.User;

import java.util.List;

public interface UserService {
    List<User> findAllUsers();
    User getUserById(Long id);
    User getUserByEmail(String email);
    void deleteUserById(Long id);
    User saveUser(User user, List<Long> roleIds);
    User updateUser(Long id, User updatedUser, List<Long> roleIds);
    List<UserDto> findAllUsersDto();
    UserDto getUserDtoById(Long id);
}