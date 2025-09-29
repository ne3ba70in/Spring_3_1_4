package kata314.services;

import kata314.dto.RoleDto;
import kata314.dto.UserDto;
import kata314.entities.Role;
import kata314.repositories.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<RoleDto> findAllRoles() {
        return roleRepository.findAll().stream()
                .map(RoleDto::new)
                .collect(Collectors.toList());
    }
}
