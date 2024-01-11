package app.school.controller;

import app.school.model.dto.CourseDto;
import app.school.model.request.CourseRequest;
import app.school.pagination.FilterCourse;
import app.school.service.CourseService;
import app.school.type.Status;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Operation(
            summary = "Create course",
            description = "Creates a new course with the provided details."
    )
    @PostMapping
    public ResponseEntity<Status> createCourse(@Valid @RequestBody CourseRequest request) {
        Status status = courseService.createCourse(request);
        return ResponseEntity.ok(status);
    }

    @Operation(
            summary = "Join user to course",
            description = "Joins a user to a specific course."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User successfully joined to the course"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request, validation failed"
            )
    })
    @PostMapping("/join/{courseCode}")
    public ResponseEntity<Status> joinUserToCourse(@PathVariable("courseCode") Long courseCode,
                                              Principal principal) {
        Status status = courseService.joinUserToCourse(principal, courseCode);
        return ResponseEntity.ok(status);
    }

    @Operation(
            summary = "Get course by code",
            description = "Retrieves details of a specific course by its code."
    )
    @GetMapping("/{courseCode}")
    public ResponseEntity<CourseDto> getCourseByCode(@PathVariable("courseCode") Long courseCode) {
        CourseDto course = courseService.getCourseByCode(courseCode);
        return ResponseEntity.ok(course);
    }

    @Operation(
            summary = "Get relations",
            description = "Retrieves relations between entities."
    )
    @GetMapping
    public ResponseEntity<Page<Map<String, List<String>>>> getRelations(@RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "10") int size) {
        Page<Map<String, List<String>>> allRelations = courseService.getRelations(page, size);
        return ResponseEntity.ok(allRelations);
    }

    @Operation(
            summary = "Filter courses",
            description = "Filters courses based on specified criteria."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully filtered courses"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request, invalid filter criteria"
            )
    })
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
