package app.school.validation;

import app.school.exception.exceptions.UserAlreadyExist;
import app.school.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;

    public void register(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExist(email);
        }
    }
}
