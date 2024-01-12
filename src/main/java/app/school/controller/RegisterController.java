package app.school.controller;

import app.school.model.request.RegisterRequest;
import app.school.service.RegisterService;
import app.school.type.Status;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/register")
public class RegisterController {

    private final RegisterService registerService;

    @Operation(
            summary = "User Registration",
            description = "Registers a new user with the provided details."
    )
    @PostMapping
    public ResponseEntity<Status> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(registerService.register(request));
    }

    @Operation(
            summary = "Assign Admin Role",
            description = "Assigns admin role to a user by email."
    )
    @PatchMapping("/admin")
    public ResponseEntity<Status> assignAdminRole(@RequestParam("email") String email) {
        Status status = registerService.assignAdminRole(email);
        return ResponseEntity.ok(status);
    }
}
