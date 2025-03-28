package itmo.blps.elearningplatform.repository;

import itmo.blps.elearningplatform.model.Homework;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HomeworkRepository extends JpaRepository<Homework, Integer> {
}
