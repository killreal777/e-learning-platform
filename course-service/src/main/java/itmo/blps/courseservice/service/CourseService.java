package itmo.blps.courseservice.service;

import itmo.blps.courseservice.dto.course.CourseDto;
import itmo.blps.courseservice.dto.course.request.CreateCourseRequest;
import itmo.blps.courseservice.mapper.CourseMapper;
import itmo.blps.courseservice.model.Course;
import itmo.blps.courseservice.repository.CourseRepository;
import itmo.blps.courseservice.service.exception.EntityNotFoundWithIdException;
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
