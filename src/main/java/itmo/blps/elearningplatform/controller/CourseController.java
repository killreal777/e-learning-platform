package itmo.blps.elearningplatform.controller;

import itmo.blps.elearningplatform.dto.course.CourseDto;
import itmo.blps.elearningplatform.dto.course.request.CreateCourseRequest;
import itmo.blps.elearningplatform.model.User;
import itmo.blps.elearningplatform.service.CourseService;
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
public class CourseController {

    private final CourseService courseService;

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
        courseService.enrollStudent(courseId, studentId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @PostMapping("/{courseId}/enroll/me")
    public ResponseEntity<Void> enrollMe(@PathVariable Integer courseId, @AuthenticationPrincipal User student) {
        courseService.enrollStudent(courseId, student.getId());
        return ResponseEntity.ok().build();
    }
}
