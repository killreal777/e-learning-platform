package itmo.blps.elearningplatform.mapper;

import itmo.blps.elearningplatform.dto.course.CourseDto;
import itmo.blps.elearningplatform.dto.course.request.CreateCourseRequest;
import itmo.blps.elearningplatform.model.Course;
import org.mapstruct.Mapper;

@Mapper(uses = {HomeworkMapper.class, TestMapper.class})
public interface CourseMapper extends EntityMapper<CourseDto, Course> {

    Course toEntity(CreateCourseRequest request);

    static void linkEntities(Course course) {
        course.getHomeworks().forEach(homework -> {
            homework.setCourse(course);
        });
        course.getTests().forEach(test -> {
            TestMapper.linkEntities(test);
            test.setCourse(course);
        });
    }
}
