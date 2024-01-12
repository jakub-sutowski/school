package app.school.service;

import app.school.exception.exceptions.UserNotExist;
import app.school.model.entity.User;
import app.school.model.request.RegisterRequest;
import app.school.repository.UserRepository;
import app.school.type.Role;
import app.school.type.Status;
import app.school.validation.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class RegisterService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Transactional
    public Status register(RegisterRequest request) {
        userValidator.register(request.getEmail());
        User user = modelMapper.map(request, User.class);

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        return Status.USER_CREATED;
    }

    @Transactional
    public Status assignAdminRole(String email) {
        User user = findByEmail(email);
        user.setRole(Role.ADMIN);
        userRepository.save(user);
        return Status.ADMIN_ROLE_ASSIGNED;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotExist(email));
    }
}

