package itmo.blps.elearningplatform.repository;

import itmo.blps.elearningplatform.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
}
