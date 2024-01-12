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
            var user = RegisterRequest.builder()
                    .firstName("Zwyklak")
                    .lastName("User")
                    .email("user@mail.com")
                    .password("password")
                    .build();
            System.out.println("Registration status: " + registerService.register(user));

            var admin = RegisterRequest.builder()
                    .firstName("Boss")
                    .lastName("Admin")
                    .email("admin@mail.com")
                    .password("password123")
                    .build();
            System.out.println("Registration status: " + registerService.register(admin));

            registerService.assignAdminRole(admin.getEmail());
        };
    }
}