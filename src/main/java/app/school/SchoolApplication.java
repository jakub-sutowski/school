package app.school;

import app.school.model.request.RegisterRequest;
import app.school.service.RegisterService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static app.school.type.Role.ADMIN;
import static app.school.type.Role.STUDENT;

@SpringBootApplication
public class SchoolApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchoolApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(
            RegisterService authenticationService

    ) {
        return args -> {
            var user = RegisterRequest.builder()
                    .firstName("Zwyklak")
                    .lastName("User")
                    .email("user@mail.com")
                    .password("password")
                    .role(STUDENT)
                    .build();
            System.out.println("Student token: " + authenticationService.register(user).getAccessToken());

            var admin = RegisterRequest.builder()
                    .firstName("Boss")
                    .lastName("Admin")
                    .email("admin@mail.com")
                    .password("password123")
                    .role(ADMIN)
                    .build();
            System.out.println("Admin token: " + authenticationService.register(admin).getAccessToken());

        };
    }
}