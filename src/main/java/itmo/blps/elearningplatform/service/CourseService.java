package itmo.blps.elearningplatform.service;

import itmo.blps.elearningplatform.dto.course.CourseDto;
import itmo.blps.elearningplatform.dto.course.request.CreateCourseRequest;
import itmo.blps.elearningplatform.mapper.CourseMapper;
import itmo.blps.elearningplatform.mapper.TestAnswerMapper;
import itmo.blps.elearningplatform.mapper.TestMapper;
import itmo.blps.elearningplatform.model.Course;
import itmo.blps.elearningplatform.model.HomeworkAnswer;
import itmo.blps.elearningplatform.model.Study;
import itmo.blps.elearningplatform.model.User;
import itmo.blps.elearningplatform.repository.*;
import itmo.blps.elearningplatform.service.exception.EntityNotFoundWithIdException;
import itmo.blps.elearningplatform.service.exception.StudentIsNotEnrolledException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    private final UserRepository userRepository;
    private final StudyRepository studyRepository;

    private final TestMapper testMapper;

    private final TestAnswerRepository testAnswerRepository;
    private final HomeworkAnswerRepository homeworkAnswerRepository;
    private final TestAnswerMapper testAnswerMapper;

    public CourseDto createCourse(CreateCourseRequest request) {
        Course course = courseMapper.toEntity(request);
        CourseMapper.linkEntities(course);
        course = courseRepository.save(course);
        return courseMapper.toDto(course);
    }

    public Page<CourseDto> getCourses(Pageable pageable) {
        Page<Course> courses = courseRepository.findAll(pageable);
        return courses.map(courseMapper::toDto);
    }

    public CourseDto getCourse(Integer id) {
        return courseRepository.findById(id).map(courseMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundWithIdException(Course.class, id));
    }

    public void enrollStudent(Integer courseId, Integer studentId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundWithIdException(Course.class, courseId));
        User student = userRepository.findByIdAndRole(studentId, User.Role.ROLE_STUDENT)
                .orElseThrow(() -> new EntityNotFoundWithIdException("Student", studentId));
        Study study = new Study(student, course);
        studyRepository.save(study);
    }

    public Integer getScore(Integer courseId, Integer studentId) {
        int testsScore = testAnswerRepository
                .findAllByStudentIdAndTestCourseIdAndActualTrue(studentId, courseId)
                .stream()
                .filter(Objects::nonNull)
                .map(testAnswerMapper::getTotalScore)
                .filter(Objects::nonNull)
                .reduce(0, Integer::sum);
        int homeworksScore = homeworkAnswerRepository
                .findAllByStudentIdAndHomeworkCourseIdAndActualTrue(studentId, courseId)
                .stream()
                .filter(Objects::nonNull)
                .map(HomeworkAnswer::getScore)
                .filter(Objects::nonNull)
                .reduce(0, Integer::sum);
        return testsScore + homeworksScore;
    }

    public void validateStudentIsEnrolled(Integer studentId, Integer courseId) {
        if (!studyRepository.existsByStudentIdAndCourseId(studentId, courseId)) {
            throw new StudentIsNotEnrolledException(studentId, courseId);
        }
    }
}
