package itmo.blps.elearningplatform.repository;

import itmo.blps.elearningplatform.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    List<User> findAll();

    Optional<User> findById(Integer id);

    void delete(User user);

    boolean existsByUsername(String username);

    boolean existsByRole(User.Role role);

    Optional<User> findByUsername(String username);

    List<User> findAllByEnabledFalse();

    List<User> findAllByRole(User.Role role);

    Optional<User> findByIdAndRole(Integer id, User.Role role);
}
