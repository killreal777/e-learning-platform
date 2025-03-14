package itmo.blps.elearningplatform.repository;

import itmo.blps.elearningplatform.model.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Integer> {
}
