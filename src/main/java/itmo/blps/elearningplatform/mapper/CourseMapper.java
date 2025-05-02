package itmo.blps.elearningplatform.mapper;

import itmo.blps.elearningplatform.dto.course.CourseDto;
import itmo.blps.elearningplatform.dto.course.request.CreateCourseRequest;
import itmo.blps.elearningplatform.model.Course;
import itmo.blps.elearningplatform.model.Homework;
import itmo.blps.elearningplatform.model.Question;
import itmo.blps.elearningplatform.model.Test;
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
