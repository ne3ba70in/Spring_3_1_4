package kata314.services;

import kata314.config.dto.UserDto;
import kata314.entities.Role;
import kata314.entities.User;
import kata314.repositories.RoleRepository;
import kata314.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь с ID: " + id + " не найден"));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void deleteUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь с ID: " + id + " не найден"));
        userRepository.delete(user);
    }

    @Override
    public User saveUser(User user, List<Long> roleIds) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("Пользователь с данным мейлом: " + user.getEmail() + ", уже существует");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> roles = new HashSet<>();
        for (Long roleId : roleIds) {
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new RuntimeException("Роль с ID " + roleId + " не найдена"));
            roles.add(role);
        }
        user.setRoles(roles);
        userRepository.save(user);
        return user;
    }

    @Override
    public User updateUser(Long id, User updatedUser, List<Long> roleIds) {
        User existingUser = getUserById(id);
        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setAge(updatedUser.getAge());
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        if (roleIds != null && !roleIds.isEmpty()) {
            Set<Role> roles = roleIds.stream()
                    .map(roleId -> roleRepository.findById(roleId)
                            .orElseThrow(() -> new RuntimeException("Role not found: " + roleId)))
                    .collect(Collectors.toSet());
            existingUser.setRoles(roles);
        }
        return userRepository.save(existingUser);
    }

    public List<UserDto> findAllUsersDto() {
        return userRepository.findAll().stream()
                .map(UserDto::new)
                .collect(Collectors.toList());
    }

    public UserDto getUserDtoById(Long id) {
        User user = getUserById(id);
        return new UserDto(user);
    }
}