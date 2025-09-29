package kata314.services;

import kata314.dto.RoleDto;
import kata314.entities.Role;

import java.util.List;

public interface RoleService {
    List<RoleDto> findAllRoles();
}