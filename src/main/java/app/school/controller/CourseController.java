package app.school.controller;

import app.school.model.dto.CourseDto;
import app.school.model.request.CourseRequest;
import app.school.pagination.FilterCourse;
import app.school.service.CourseService;
import app.school.type.Status;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    public ResponseEntity<Status> createCourse(@Valid @RequestBody CourseRequest request) {
        Status status = courseService.createCourse(request);
        return ResponseEntity.ok(status);
    }

    @PostMapping("/join/{courseCode}")
    public ResponseEntity<Status> joinUserToCourse(@PathVariable("courseCode") Long courseCode,
                                              Principal principal) {
        Status status = courseService.joinUserToCourse(principal, courseCode);
        return ResponseEntity.ok(status);
    }

    @GetMapping("/{courseCode}")
    public ResponseEntity<CourseDto> getCourseByCode(@PathVariable("courseCode") Long courseCode) {
        CourseDto course = courseService.getCourseByCode(courseCode);
        return ResponseEntity.ok(course);
    }

    @GetMapping
    public ResponseEntity<Map<String, List<String>>> getRelations() {
        Map<String, List<String>> allRelations = courseService.getRelations();
        //TODO zobacz jak mozna zrobic Page z mapy
        return ResponseEntity.ok(allRelations);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<String>> getFilteredCourses(
            @RequestParam(name = "empty", required = false) boolean isEmpty,
            @RequestParam(name = "student", required = false) String studentEmail) {
        try {
            FilterCourse filterCourse = FilterCourse.builder()
                    .isEmpty(isEmpty)
                    .studentEmail(studentEmail)
                    .build();
            List<String> filter = courseService.filter(filterCourse);
            return ResponseEntity.ok(filter);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
