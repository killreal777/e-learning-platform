package itmo.blps.elearningplatform.controller;

import itmo.blps.elearningplatform.dto.course.HomeworkAnswerDto;
import itmo.blps.elearningplatform.dto.course.TestAnswerDto;
import itmo.blps.elearningplatform.dto.course.request.CreateHomeworkAnswerRequest;
import itmo.blps.elearningplatform.dto.course.request.CreateTestAnswerRequest;
import itmo.blps.elearningplatform.dto.course.request.ReviewHomeworkAnswerRequest;
import itmo.blps.elearningplatform.model.User;
import itmo.blps.elearningplatform.service.HomeworkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/homeworks")
@RequiredArgsConstructor
public class HomeworkRestController {

    private final HomeworkService homeworkService;

    @PostMapping("/{homeworkId}/answers")
    public ResponseEntity<HomeworkAnswerDto> completeHomework(
            @PathVariable Integer homeworkId,
            @RequestBody CreateHomeworkAnswerRequest request,
            @AuthenticationPrincipal User student
    ) {
        return ResponseEntity.ok(homeworkService.completeHomework(homeworkId, request, student));
    }

    @PostMapping("/answers/{answerId}")
    public ResponseEntity<HomeworkAnswerDto> reviewHomework(
            @PathVariable Integer answerId,
            @RequestBody ReviewHomeworkAnswerRequest request,
            @AuthenticationPrincipal User teacher
    ) {
        return ResponseEntity.ok(homeworkService.reviewHomework(answerId, request, teacher));
    }
}
