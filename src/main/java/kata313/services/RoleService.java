package kata313.services;

import kata313.entities.Role;

import java.util.List;

public interface RoleService {
    List<Role> findAllRoles();
    Role findRoleById(Long id);
}