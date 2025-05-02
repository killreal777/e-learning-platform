package itmo.blps.elearningplatform.controller;

import itmo.blps.elearningplatform.dto.course.CourseDto;
import itmo.blps.elearningplatform.dto.course.HomeworkAnswerDto;
import itmo.blps.elearningplatform.dto.course.TestAnswerDto;
import itmo.blps.elearningplatform.dto.course.request.CreateCourseRequest;
import itmo.blps.elearningplatform.dto.course.request.CreateHomeworkAnswerRequest;
import itmo.blps.elearningplatform.dto.course.request.CreateTestAnswerRequest;
import itmo.blps.elearningplatform.dto.course.request.ReviewHomeworkAnswerRequest;
import itmo.blps.elearningplatform.model.User;
import itmo.blps.elearningplatform.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseRestController {

    private final StudyService studyService;
    private final CourseService courseService;
    private final ScoreService scoreService;
    private final TestAnswerService testAnswerService;
    private final HomeworkAnswerService homeworkAnswerService;

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @PostMapping
    public ResponseEntity<CourseDto> createCourse(@RequestBody CreateCourseRequest request) {
        return ResponseEntity.ok(courseService.createCourse(request));
    }

    @GetMapping
    public ResponseEntity<Page<CourseDto>> getCourses(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(courseService.getCourses(pageable));
    }

    @GetMapping("{id}")
    public ResponseEntity<CourseDto> getCourse(@PathVariable Integer id) {
        return ResponseEntity.ok(courseService.getCourse(id));
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN', 'TEACHER')")
    @PostMapping("/{courseId}/enroll/{studentId}")
    public ResponseEntity<Void> enrollStudent(@PathVariable Integer courseId, @PathVariable Integer studentId) {
        studyService.enrollStudent(courseId, studentId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @PostMapping("/{courseId}/enroll/me")
    public ResponseEntity<Void> enrollMe(@PathVariable Integer courseId, @AuthenticationPrincipal User student) {
        studyService.enrollStudent(courseId, student.getId());
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/tests/{testId}/start")
    public ResponseEntity<Void> startTest(
            @PathVariable Integer testId,
            @AuthenticationPrincipal User student
    ) {
        testAnswerService.startTest(testId, student);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/tests/{testId}/complete")
    public ResponseEntity<TestAnswerDto> completeTest(
            @PathVariable Integer testId,
            @RequestBody CreateTestAnswerRequest request,
            @AuthenticationPrincipal User student
    ) {
        return ResponseEntity.ok(testAnswerService.completeTest(testId, request, student));
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/homeworks/{homeworkId}/answers")
    public ResponseEntity<HomeworkAnswerDto> completeHomework(
            @PathVariable Integer homeworkId,
            @RequestBody CreateHomeworkAnswerRequest request,
            @AuthenticationPrincipal User student
    ) {
        return ResponseEntity.ok(homeworkAnswerService.completeHomework(homeworkId, request, student));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/homeworks/answers/{answerId}")
    public ResponseEntity<HomeworkAnswerDto> reviewHomework(
            @PathVariable Integer answerId,
            @RequestBody ReviewHomeworkAnswerRequest request,
            @AuthenticationPrincipal User teacher
    ) {
        return ResponseEntity.ok(homeworkAnswerService.reviewHomework(answerId, request, teacher));
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN', 'TEACHER')")
    @PostMapping("/{courseId}/students/{studentId}/score")
    public ResponseEntity<Integer> getStudentScore(@PathVariable Integer courseId, @PathVariable Integer studentId) {
        return ResponseEntity.ok(scoreService.getStudentCourseScore(studentId, courseId));
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/{courseId}/students/me/score")
    public ResponseEntity<Integer> getMyScore(@PathVariable Integer courseId, @AuthenticationPrincipal User me) {
        return ResponseEntity.ok(scoreService.getStudentCourseScore(me.getId(), courseId));
    }
}
