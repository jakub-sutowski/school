package app.school.service;

import app.school.model.entity.User;
import app.school.model.request.RegisterRequest;
import app.school.repository.UserRepository;
import app.school.type.Status;
import app.school.validation.UserValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserValidator userValidator;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private RegisterService registerService;

    @Test
    public void shouldRegisterUser() {
        // Given
        RegisterRequest request = new RegisterRequest();
        User mockUser = new User();
        when(modelMapper.map(request, User.class)).thenReturn(mockUser);

        // When
        Status resultStatus = registerService.register(request);

        // Then
        assertEquals(Status.USER_CREATED, resultStatus);
        verify(userRepository, times(1)).save(mockUser);
    }

    @Test
    public void shouldAssignAdminRole() {
        // Given
        String email = "test@example.com";
        User mockUser = new User();
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(mockUser));

        // When
        Status resultStatus = registerService.assignAdminRole(email);

        // Then
        assertEquals(Status.ADMIN_ROLE_ASSIGNED, resultStatus);
        verify(userRepository, times(1)).save(mockUser);
    }
}