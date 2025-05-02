package itmo.blps.elearningplatform.service;

import itmo.blps.elearningplatform.dto.course.CourseDto;
import itmo.blps.elearningplatform.dto.course.request.CreateCourseRequest;
import itmo.blps.elearningplatform.mapper.CourseMapper;
import itmo.blps.elearningplatform.model.Course;
import itmo.blps.elearningplatform.repository.CourseRepository;
import itmo.blps.elearningplatform.service.exception.EntityNotFoundWithIdException;
import itmo.blps.elearningplatform.service.transaction.Transactional;
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

    @Transactional
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

    public Integer getMaxScore(Integer courseId) {
        Course course = getCourseEntityById(courseId);
        return courseMapper.getMaxScore(course);
    }

    public Course getCourseEntityById(Integer id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundWithIdException(Course.class, id));
    }
}
