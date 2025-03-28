package itmo.blps.elearningplatform.controller;

import itmo.blps.elearningplatform.dto.course.HomeworkAnswerDto;
import itmo.blps.elearningplatform.dto.course.TestAnswerDto;
import itmo.blps.elearningplatform.dto.course.request.CreateTestAnswerRequest;
import itmo.blps.elearningplatform.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tests")
@RequiredArgsConstructor
public class TestRestController {

    @PostMapping("/{testId}/answers")
    public ResponseEntity<TestAnswerDto> completeTest(
            @PathVariable Integer testId,
            @RequestBody CreateTestAnswerRequest request,
            @AuthenticationPrincipal User student
    ) {
        return null;
    }
}
