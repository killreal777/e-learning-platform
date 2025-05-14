package itmo.blps.courseservice.repository;

import itmo.blps.courseservice.model.Study;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudyRepository extends JpaRepository<Study, Integer> {

    boolean existsByStudentIdAndCourseId(Integer studentId, Integer courseId);

    Optional<Study> findByStudentIdAndCourseId(Integer studentId, Integer courseId);
}
