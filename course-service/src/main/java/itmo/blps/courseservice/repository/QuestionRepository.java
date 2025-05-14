package itmo.blps.courseservice.repository;

import itmo.blps.courseservice.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
}
