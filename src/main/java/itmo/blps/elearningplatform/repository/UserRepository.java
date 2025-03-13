package itmo.blps.elearningplatform.repository;

import itmo.blps.elearningplatform.model.user.Role;
import itmo.blps.elearningplatform.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByUsername(String username);

    boolean existsByRole(Role role);

    Optional<User> findByUsername(String username);

    Page<User> findAllByEnabledFalse(Pageable pageable);

    Page<User> findAllByRole(Role role, Pageable pageable);
}
