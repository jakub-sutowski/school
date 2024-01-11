package app.school.controller;

import app.school.pagination.FilterStudent;
import app.school.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/courses")
    public ResponseEntity<List<String>> getCoursesByLoggedUser(Principal principal) {
        List<String> courses = userService.getCoursesByLoggedUser(principal);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<String>> getFilteredStudents(
            @RequestParam(name = "empty", required = false) boolean isEmpty,
            @RequestParam(name = "code", required = false) Long courseCode) {
        try {
            FilterStudent filterStudent = FilterStudent.builder()
                    .isEmpty(isEmpty)
                    .courseCode(courseCode)
                    .build();
            List<String> filter = userService.filter(filterStudent);
            return ResponseEntity.ok(filter);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
