package itmo.blps.elearningplatform.repository;

import itmo.blps.elearningplatform.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Integer> {
}
