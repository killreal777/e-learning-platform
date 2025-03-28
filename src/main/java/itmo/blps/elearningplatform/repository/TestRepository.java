package itmo.blps.elearningplatform.repository;

import itmo.blps.elearningplatform.model.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<Test, Integer> {
}
