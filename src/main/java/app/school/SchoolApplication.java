package app.school;

import app.school.model.request.RegisterRequest;
import app.school.service.RegisterService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SchoolApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchoolApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(
            RegisterService registerService

    ) {
        return args -> {
            var admin = RegisterRequest.builder()
                    .firstName("Boss")
                    .lastName("Admin")
                    .email("admin@mail.com")
                    .password("password123")
                    .build();
            System.out.println("Registration status: " + registerService.register(admin));
            System.out.printf("Role for %s: %s", admin.getEmail(), registerService.assignAdminRole(admin.getEmail()));
            System.out.println();
        };
    }
}