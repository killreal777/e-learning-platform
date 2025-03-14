package itmo.blps.elearningplatform.service;

import itmo.blps.elearningplatform.dto.course.CourseDto;
import itmo.blps.elearningplatform.dto.course.request.CreateCourseRequest;
import itmo.blps.elearningplatform.mapper.CourseMapper;
import itmo.blps.elearningplatform.model.course.Course;
import itmo.blps.elearningplatform.model.course.Study;
import itmo.blps.elearningplatform.model.user.Role;
import itmo.blps.elearningplatform.model.user.User;
import itmo.blps.elearningplatform.repository.CourseRepository;
import itmo.blps.elearningplatform.repository.StudyRepository;
import itmo.blps.elearningplatform.repository.UserRepository;
import itmo.blps.elearningplatform.service.exception.EntityNotFoundWithIdException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    private final UserRepository userRepository;
    private final StudyRepository studyRepository;

    public CourseDto createCourse(CreateCourseRequest request) {
        return courseMapper.toDto(courseRepository.save(courseMapper.toEntity(request)));
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
        User student = userRepository.findByIdAndRole(studentId, Role.ROLE_STUDENT)
                .orElseThrow(() -> new EntityNotFoundWithIdException("Student", studentId));
        Study study = new Study(student, course);
        studyRepository.save(study);
    }
}
