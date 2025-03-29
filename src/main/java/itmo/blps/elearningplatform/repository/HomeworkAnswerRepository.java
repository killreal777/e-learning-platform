package itmo.blps.elearningplatform.repository;

import itmo.blps.elearningplatform.model.HomeworkAnswer;

import java.util.List;
import java.util.Optional;

public interface HomeworkAnswerRepository extends BaseEntityJpaRepository<HomeworkAnswer> {

    Optional<HomeworkAnswer> findByHomeworkIdAndStudentIdAndActualTrue(Integer homeworkId, Integer studentId);

    List<HomeworkAnswer> findAllByStudentIdAndHomeworkCourseIdAndActualTrue(Integer studentId, Integer courseId);
}
