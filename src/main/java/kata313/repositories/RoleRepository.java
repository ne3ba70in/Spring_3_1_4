package kata313.repositories;

import kata313.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
    // Метод findById уже есть в JpaRepository, но если нужна кастомная реализация, добавьте:
    // Optional<Role> findById(Long id);
}
