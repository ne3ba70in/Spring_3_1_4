package kata314.services;

import kata314.entities.Role;

import java.util.List;

public interface RoleService {
    List<Role> findAllRoles();
    Role findRoleById(Long id);
}