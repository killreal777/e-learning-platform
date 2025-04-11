package itmo.blps.elearningplatform.repository;

import itmo.blps.elearningplatform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByUsername(String username);

    boolean existsByRole(User.Role role);

    Optional<User> findByUsername(String username);

    List<User> findAllByEnabledFalse();

    List<User> findAllByRole(User.Role role);

    Optional<User> findByIdAndRole(Integer id, User.Role role);
}
