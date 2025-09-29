package kata314.services;

import kata314.entities.Role;
import kata314.repositories.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role findRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Роль с ID: " + id + " не найдена."));
    }
}
