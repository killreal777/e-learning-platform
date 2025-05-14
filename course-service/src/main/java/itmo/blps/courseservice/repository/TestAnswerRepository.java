package itmo.blps.courseservice.repository;

import itmo.blps.courseservice.model.TestAnswer;

import java.util.List;
import java.util.Optional;

public interface TestAnswerRepository extends BaseEntityJpaRepository<TestAnswer> {

    Optional<TestAnswer> findByTestIdAndStudentIdAndActualTrue(Integer testId, Integer studentId);

    List<TestAnswer> findAllByStudentIdAndTestCourseIdAndActualTrue(Integer studentId, Integer courseId);
}
