package itmo.blps.elearningplatform.service;

import itmo.blps.elearningplatform.dto.course.CourseDto;
import itmo.blps.elearningplatform.dto.course.request.CreateCourseRequest;
import itmo.blps.elearningplatform.mapper.CourseMapper;
import itmo.blps.elearningplatform.model.Course;
import itmo.blps.elearningplatform.model.Study;
import itmo.blps.elearningplatform.model.User;
import itmo.blps.elearningplatform.repository.CourseRepository;
import itmo.blps.elearningplatform.repository.StudyRepository;
import itmo.blps.elearningplatform.repository.UserRepository;
import itmo.blps.elearningplatform.service.exception.EntityNotFoundWithIdException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    private final UserRepository userRepository;
    private final StudyRepository studyRepository;

    public CourseDto createCourse(CreateCourseRequest request) {
        log.info("Request {}", request);
        Course course = courseMapper.toEntity(request);
        log.info("Creating course {}", course);
        course = courseRepository.save(course);
        log.info("Created course {}", course);
        CourseDto courseDto = courseMapper.toDto(course);
        log.info("Mapped course {}", courseDto);
        return courseDto;
//        return courseMapper.toDto(courseRepository.save(courseMapper.toEntity(request)));
    }

    public Page<CourseDto> getCourses(Pageable pageable) {
        return courseRepository.findAll(pageable).map(courseMapper::toDto);
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
}
