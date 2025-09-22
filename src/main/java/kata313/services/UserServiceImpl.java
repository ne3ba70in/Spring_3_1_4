package kata313.services;

import kata313.entities.Role;
import kata313.entities.User;
import kata313.repositories.RoleRepository;
import kata313.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public void saveUser(User user, List<Long> roleIds) {
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
    }

    @Override
    public void updateUser(Long id, User userDetails, List<Long> roleIds) {
        User user = getUserById(id);
        User existingUser = userRepository.findByEmail(userDetails.getEmail());
        if (existingUser != null && !existingUser.getId().equals(id)) {
            throw new RuntimeException("Пользователь с данным мейлом: " + userDetails.getEmail() + ", уже существует");
        }
        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        user.setAge(userDetails.getAge());
        user.setEmail(userDetails.getEmail());
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }
        Set<Role> roles = new HashSet<>();
        for (Long roleId : roleIds) {
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new RuntimeException("Роль с ID " + roleId + " не найдена"));
            roles.add(role);
        }
        user.setRoles(roles);
        userRepository.save(user);
    }
}