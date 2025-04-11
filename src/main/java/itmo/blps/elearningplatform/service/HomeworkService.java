package itmo.blps.elearningplatform.service;

import itmo.blps.elearningplatform.dto.course.HomeworkAnswerDto;
import itmo.blps.elearningplatform.dto.course.HomeworkDto;
import itmo.blps.elearningplatform.dto.course.request.CreateHomeworkAnswerRequest;
import itmo.blps.elearningplatform.dto.course.request.ReviewHomeworkAnswerRequest;
import itmo.blps.elearningplatform.mapper.HomeworkAnswerMapper;
import itmo.blps.elearningplatform.mapper.HomeworkMapper;
import itmo.blps.elearningplatform.model.Homework;
import itmo.blps.elearningplatform.model.HomeworkAnswer;
import itmo.blps.elearningplatform.model.User;
import itmo.blps.elearningplatform.repository.HomeworkAnswerRepository;
import itmo.blps.elearningplatform.repository.HomeworkRepository;
import itmo.blps.elearningplatform.service.exception.EntityNotFoundWithIdException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class HomeworkService {

    private final HomeworkRepository homeworkRepository;
    private final HomeworkAnswerRepository homeworkAnswerRepository;

    private final HomeworkMapper homeworkMapper;
    private final HomeworkAnswerMapper homeworkAnswerMapper;

    private final CourseService courseService;

    public HomeworkAnswerDto completeHomework(Integer homeworkId, CreateHomeworkAnswerRequest request, User student) {
        Homework homework = getHomeworkEntityById(homeworkId);
        courseService.validateStudentIsEnrolled(student.getId(), homework.getCourse().getId());
        HomeworkAnswer answer = new HomeworkAnswer();
        answer.setStudent(student);
        answer.setHomework(homework);
        answer.setText(request.text());
        answer = homeworkAnswerRepository.save(answer);
        return homeworkAnswerMapper.toDto(answer);
    }

    public HomeworkAnswerDto reviewHomework(Integer answerId, ReviewHomeworkAnswerRequest request, User teacher) {
        HomeworkAnswer answer = getHomeworkAnswerEntityById(answerId);
        HomeworkAnswer lastActualAnswer = homeworkAnswerRepository
                .findByHomeworkIdAndStudentIdAndActualTrue(
                        answer.getHomework().getId(),
                        answer.getStudent().getId()
                ).orElse(null);
        if (lastActualAnswer != null && lastActualAnswer.getScore() <= request.score()) {
            lastActualAnswer.setActual(false);
            homeworkAnswerRepository.save(lastActualAnswer);
            answer.setActual(true);
        } else if (lastActualAnswer == null) {
            answer.setActual(true);
        }
        answer.setReviewer(teacher);
        answer.setScore(request.score());
        answer.setStatus(HomeworkAnswer.Status.REVIEWED);
        answer = homeworkAnswerRepository.save(answer);
        return homeworkAnswerMapper.toDto(answer);
    }

    private Homework getHomeworkEntityById(Integer id) {
        return homeworkRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundWithIdException(Homework.class, id));
    }

    private HomeworkAnswer getHomeworkAnswerEntityById(Integer id) {
        return homeworkAnswerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundWithIdException(HomeworkAnswer.class, id));
    }
}
