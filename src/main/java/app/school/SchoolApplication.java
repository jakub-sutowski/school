package app.school;

import app.school.model.request.RegisterRequest;
import app.school.service.RegisterService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static app.school.type.Role.ADMIN;

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
                    .role(ADMIN)
                    .build();
            System.out.println("User token: " + authenticationService.register(user).getAccessToken());

        };
    }
}