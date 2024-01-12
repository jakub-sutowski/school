package app.school.service;

import app.school.mapping.UserCredentialsDtoMapper;
import app.school.model.dto.UserCredentialsDto;
import app.school.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserCredentialsService {

    private final UserRepository userRepository;

    public Optional<UserCredentialsDto> findCredentialsByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserCredentialsDtoMapper::map);
    }
}
