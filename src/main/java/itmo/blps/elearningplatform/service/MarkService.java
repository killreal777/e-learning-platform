package itmo.blps.elearningplatform.service;

import itmo.blps.elearningplatform.model.Study;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class MarkService {

    private final ScoreService scoreService;
    private final CourseService courseService;
    private final StudyService studyService;

    public void updateMark(Integer studentId, Integer courseId) {
        studyService.validateStudentIsEnrolled(studentId, courseId);
        double studentScore = scoreService.getStudentCourseScore(studentId, courseId);
        double maxScore = courseService.getMaxScore(courseId);
        double percentage = studentScore / maxScore;
        Study.Mark mark = getMarkByPercentage(percentage);
        studyService.updateMark(studentId, courseId, mark);
    }

    private Study.Mark getMarkByPercentage(double percentage) {
        if (percentage >= 0.9) return Study.Mark.A;
        else if (percentage >= 0.8) return Study.Mark.B;
        else if (percentage >= 0.7) return Study.Mark.C;
        else if (percentage >= 0.6) return Study.Mark.D;
        else if (percentage >= 0.5) return Study.Mark.E;
        else return Study.Mark.FX;
    }
}
