package itmo.blps.courseservice.service;

import itmo.blps.courseservice.model.Course;
import itmo.blps.courseservice.model.Study;
import itmo.blps.courseservice.model.User;
import itmo.blps.courseservice.repository.StudyRepository;
import itmo.blps.courseservice.service.exception.StudentIsNotEnrolledException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;

    private final CourseService courseService;
    private final UserService userService;

    public void enrollStudent(Integer courseId, Integer studentId) {
        Course course = courseService.getCourseEntityById(courseId);
        User student = userService.getUserEntityByIdAndRole(studentId, User.Role.ROLE_STUDENT);
        Study study = new Study(student.getId(), course);
        studyRepository.save(study);
    }

    public void validateStudentIsEnrolled(Integer studentId, Integer courseId) {
        if (!studyRepository.existsByStudentIdAndCourseId(studentId, courseId)) {
            throw new StudentIsNotEnrolledException(studentId, courseId);
        }
    }

    public void updateMark(Integer studentId, Integer courseId, Study.Mark mark) {
        Study study = findStudyEntityByStudentIdAndCourseId(studentId, courseId);
        study.setMark(mark);
        studyRepository.save(study);
    }

    public Study.Mark getMark(Integer studentId, Integer courseId) {
        Study study = findStudyEntityByStudentIdAndCourseId(studentId, courseId);
        return study.getMark();
    }

    private Study findStudyEntityByStudentIdAndCourseId(Integer studentId, Integer courseId) {
        return studyRepository.findByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new StudentIsNotEnrolledException(studentId, courseId));
    }
}
