package itmo.blps.elearningplatform.service;

import itmo.blps.elearningplatform.dto.course.CourseDto;
import itmo.blps.elearningplatform.dto.course.request.CreateCourseRequest;
import itmo.blps.elearningplatform.mapper.CourseMapper;
import itmo.blps.elearningplatform.model.course.Course;
import itmo.blps.elearningplatform.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    public CourseDto createCourse(CreateCourseRequest request) {
        return courseMapper.toDto(courseRepository.save(courseMapper.toEntity(request)));
    }

    public Page<CourseDto> getCourses(Pageable pageable) {
        return courseRepository.findAll(pageable).map(courseMapper::toDto);
    }

    public CourseDto getCourse(Integer id) {
        return courseRepository.findById(id).map(courseMapper::toDto).orElse(null);
    }
}
