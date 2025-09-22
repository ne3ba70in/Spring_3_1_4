package kata313.services;

import kata313.entities.User;

import java.util.List;

public interface UserService {
    List<User> findAllUsers();
    User getUserById(Long id);
    User getUserByEmail(String email);
    void deleteUserById(Long id);
    void saveUser(User user, List<Long> roleIds);
    void updateUser(Long id, User userDetails, List<Long> roleIds);
}