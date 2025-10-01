package kata314.service;

import kata314.dto.RoleDto;

import java.util.List;

public interface RoleService {
    List<RoleDto> findAllRoles();
}