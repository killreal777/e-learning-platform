package itmo.blps.elearningplatform.mapper;

import itmo.blps.elearningplatform.dto.course.CourseDto;
import itmo.blps.elearningplatform.dto.course.request.CreateCourseRequest;
import itmo.blps.elearningplatform.model.Course;
import org.mapstruct.Mapper;

@Mapper
public interface CourseMapper extends EntityMapper<CourseDto, Course> {

    Course toEntity(CreateCourseRequest request);
}
