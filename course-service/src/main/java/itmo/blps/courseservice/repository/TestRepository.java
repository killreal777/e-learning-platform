package itmo.blps.courseservice.repository;

import itmo.blps.courseservice.model.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<Test, Integer> {
}
