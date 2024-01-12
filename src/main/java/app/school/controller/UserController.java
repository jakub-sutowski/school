package app.school.controller;

import app.school.model.dto.UserDto;
import app.school.model.request.ChangePasswordRequest;
import app.school.model.request.UpdateUserRequest;
import app.school.pagination.FilterStudent;
import app.school.service.UserService;
import app.school.type.Status;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Get user details by logged user",
            description = "Retrieves details of the authenticated user."
    )
    @GetMapping
    public ResponseEntity<UserDto> getUser(Principal principal) {
        UserDto userRequest = userService.getUser(principal);
        return ResponseEntity.ok(userRequest);
    }

    @Operation(
            summary = "Get user details by email",
            description = "Retrieves details of specific user."
    )
    @GetMapping("/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable("email") String email) {
        UserDto userRequest = userService.getUserByEmail(email);
        return ResponseEntity.ok(userRequest);
    }

    @Operation(
            summary = "Get courses by logged user",
            description = "Retrieves the list of courses associated with the logged-in user."
    )
    @GetMapping("/courses")
    public ResponseEntity<List<String>> getCoursesByLoggedUser(Principal principal) {
        List<String> courses = userService.getCoursesByLoggedUser(principal);
        return ResponseEntity.ok(courses);
    }

    @Operation(
            summary = "Filter students",
            description = "Filters students based on specified criteria."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully filtered students"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request, invalid filter criteria"
            )
    })
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

    @Operation(
            summary = "Update user details",
            description = "Updates the details of the authenticated user."
    )
    @PatchMapping("/update")
    public ResponseEntity<UserDto> updateUserDetails(
            @RequestBody UpdateUserRequest request,
            Principal principal) {
        UserDto updatedUser = userService.updateUserDetails(request, principal);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(
            summary = "Change password",
            description = "Changes the password for the authenticated user."
    )
    @PatchMapping
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal principal
    ) {
        userService.changePassword(request, principal);
        log.info("Password for {} successfully changed", principal.getName());
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Delete user by email",
            description = "Deletes a specific user by email."
    )
    @DeleteMapping("/{email}")
    public ResponseEntity<Status> deleteUserByEmail(@PathVariable("email") String email) {
        Status status = userService.deleteUserByEmail(email);
        return ResponseEntity.ok(status);
    }
}
