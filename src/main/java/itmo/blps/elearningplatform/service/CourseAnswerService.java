package itmo.blps.elearningplatform.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseAnswerService {

    private final TestAnswerService testAnswerService;
    private final HomeworkAnswerService homeworkAnswerService;

    public Integer getStudentCourseScore(Integer studentId, Integer courseId) {
        int testsScore = testAnswerService.getStudentTestsScore(studentId, courseId);
        int homeworksScore = homeworkAnswerService.getStudentHomeworksScore(studentId, courseId);
        return testsScore + homeworksScore;
    }
}
