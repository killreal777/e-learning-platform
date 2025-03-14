package itmo.blps.elearningplatform.controller;

import itmo.blps.elearningplatform.dto.course.CourseDto;
import itmo.blps.elearningplatform.dto.course.request.CreateCourseRequest;
import itmo.blps.elearningplatform.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

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
}
