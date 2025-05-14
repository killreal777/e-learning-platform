package itmo.blps.courseservice.service;

import itmo.blps.courseservice.dto.course.HomeworkAnswerDto;
import itmo.blps.courseservice.dto.course.request.CreateHomeworkAnswerRequest;
import itmo.blps.courseservice.dto.course.request.ReviewHomeworkAnswerRequest;
import itmo.blps.courseservice.mapper.HomeworkAnswerMapper;
import itmo.blps.courseservice.model.Homework;
import itmo.blps.courseservice.model.HomeworkAnswer;
import itmo.blps.courseservice.model.User;
import itmo.blps.courseservice.repository.HomeworkAnswerRepository;
import itmo.blps.courseservice.service.exception.EntityNotFoundWithIdException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class HomeworkAnswerService {

    private final HomeworkAnswerRepository homeworkAnswerRepository;
    private final HomeworkAnswerMapper homeworkAnswerMapper;

    private final HomeworkService homeworkService;
    private final StudyService studyService;
    private final MarkService markService;

    private final PlatformTransactionManager transactionManager;

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

    public HomeworkAnswerDto reviewHomework(Integer answerId, ReviewHomeworkAnswerRequest request, User teacher) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        def.setIsolationLevel(TransactionDefinition.ISOLATION_DEFAULT);
        def.setName("HomeworkAnswerService.reviewHomework");
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            log.atInfo()
                    .setMessage("Starting transaction")
                    .addKeyValue("transactionName", def.getName())
                    .log();
            HomeworkAnswer answer = getHomeworkAnswerEntityById(answerId);
            updateActual(answer, request);
            answer.setReviewerId(teacher.getId());
            answer.setScore(request.score());
            answer.setStatus(HomeworkAnswer.Status.REVIEWED);
            answer = homeworkAnswerRepository.save(answer);
            markService.updateMark(answer.getStudentId(), answer.getHomework().getCourse().getId());
            HomeworkAnswerDto answerDto = homeworkAnswerMapper.toDto(answer);
            log.atInfo()
                    .setMessage("Transaction committed successfully")
                    .addKeyValue("transactionName", def.getName())
                    .log();
            transactionManager.commit(status);
            return answerDto;
        } catch (Exception e) {
            log.atError()
                    .setMessage("Transaction failed, rolling back")
                    .addKeyValue("transactionName", def.getName())
                    .log();
            transactionManager.rollback(status);
            throw e;
        }
    }

    private void updateActual(HomeworkAnswer answer, ReviewHomeworkAnswerRequest request) {
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
