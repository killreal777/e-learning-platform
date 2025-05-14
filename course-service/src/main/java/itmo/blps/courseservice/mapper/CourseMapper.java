package itmo.blps.courseservice.mapper;

import itmo.blps.courseservice.dto.course.CourseDto;
import itmo.blps.courseservice.dto.course.request.CreateCourseRequest;
import itmo.blps.courseservice.model.Course;
import itmo.blps.courseservice.model.Homework;
import itmo.blps.courseservice.model.Question;
import itmo.blps.courseservice.model.Test;
import org.mapstruct.Mapper;

import java.util.List;

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

    default Integer getMaxScore(Course course) {
        Integer homeworksMaxScore = course.getHomeworks().stream()
                .map(Homework::getMaxScore)
                .reduce(0, Integer::sum);
        Integer testsMaxScore = course.getTests().stream()
                .map(Test::getQuestions)
                .flatMap(List::stream)
                .map(Question::getMaxScore)
                .reduce(0, Integer::sum);
        return homeworksMaxScore + testsMaxScore;
    }
}
