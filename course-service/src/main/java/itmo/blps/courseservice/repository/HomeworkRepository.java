package itmo.blps.courseservice.repository;

import itmo.blps.courseservice.model.Homework;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HomeworkRepository extends JpaRepository<Homework, Integer> {
}
