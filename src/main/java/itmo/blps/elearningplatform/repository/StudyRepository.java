package itmo.blps.elearningplatform.repository;

import itmo.blps.elearningplatform.model.Study;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyRepository extends JpaRepository<Study, Integer> {

    boolean existsByStudentIdAndCourseId(Integer userId, Integer courseId);
}
