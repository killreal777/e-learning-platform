package itmo.blps.elearningplatform.service;

import itmo.blps.elearningplatform.dto.course.HomeworkAnswerDto;
import itmo.blps.elearningplatform.dto.course.request.CreateHomeworkAnswerRequest;
import itmo.blps.elearningplatform.dto.course.request.ReviewHomeworkAnswerRequest;
import itmo.blps.elearningplatform.mapper.HomeworkAnswerMapper;
import itmo.blps.elearningplatform.model.Homework;
import itmo.blps.elearningplatform.model.HomeworkAnswer;
import itmo.blps.elearningplatform.model.User;
import itmo.blps.elearningplatform.repository.HomeworkAnswerRepository;
import itmo.blps.elearningplatform.service.exception.EntityNotFoundWithIdException;
import itmo.blps.elearningplatform.service.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class HomeworkAnswerService {

    private final HomeworkAnswerRepository homeworkAnswerRepository;
    private final HomeworkAnswerMapper homeworkAnswerMapper;

    private final HomeworkService homeworkService;
    private final StudyService studyService;
    private final MarkService markService;

    @Transactional
    public HomeworkAnswerDto completeHomework(Integer homeworkId, CreateHomeworkAnswerRequest request, User student) {
        Homework homework = homeworkService.getHomeworkEntityById(homeworkId);
        studyService.validateStudentIsEnrolled(student.getId(), homework.getCourse().getId());
        HomeworkAnswer answer = new HomeworkAnswer();
        answer.setStudentId(student.getId());
        answer.setHomework(homework);
        answer.setText(request.text());
        answer = homeworkAnswerRepository.save(answer);
        return homeworkAnswerMapper.toDto(answer);
    }

    @Transactional
    public HomeworkAnswerDto reviewHomework(Integer answerId, ReviewHomeworkAnswerRequest request, User teacher) {
        HomeworkAnswer answer = getHomeworkAnswerEntityById(answerId);
        HomeworkAnswer lastActualAnswer = homeworkAnswerRepository
                .findByHomeworkIdAndStudentIdAndActualTrue(
                        answer.getHomework().getId(),
                        answer.getStudentId()
                ).orElse(null);
        if (lastActualAnswer != null && lastActualAnswer.getScore() <= request.score()) {
            lastActualAnswer.setActual(false);
            homeworkAnswerRepository.save(lastActualAnswer);
            answer.setActual(true);
        } else if (lastActualAnswer == null) {
            answer.setActual(true);
        }
        answer.setReviewerId(teacher.getId());
        answer.setScore(request.score());
        answer.setStatus(HomeworkAnswer.Status.REVIEWED);
        answer = homeworkAnswerRepository.save(answer);
        markService.updateMark(answer.getStudentId(), answer.getHomework().getCourse().getId());
        return homeworkAnswerMapper.toDto(answer);
    }

    public Integer getStudentHomeworksScore(Integer studentId, Integer courseId) {
        return homeworkAnswerRepository
                .findAllByStudentIdAndHomeworkCourseIdAndActualTrue(studentId, courseId)
                .stream()
                .filter(Objects::nonNull)
                .map(HomeworkAnswer::getScore)
                .filter(Objects::nonNull)
                .reduce(0, Integer::sum);
    }

    private HomeworkAnswer getHomeworkAnswerEntityById(Integer id) {
        return homeworkAnswerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundWithIdException(HomeworkAnswer.class, id));
    }
}
