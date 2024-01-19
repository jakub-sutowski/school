package app.school.service;

import app.school.exception.exceptions.UserNotExist;
import app.school.model.entity.Course;
import app.school.model.entity.User;
import app.school.model.request.ChangePasswordRequest;
import app.school.model.request.UpdateUserRequest;
import app.school.pagination.FilterStudent;
import app.school.repository.CourseRepository;
import app.school.repository.UserRepository;
import app.school.type.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Principal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    public void shouldGetLoggedUser() {
        // Given
        String userEmail = "test@example.com";
        User mockUser = new User();
        mockUser.setEmail(userEmail);
        Principal mockPrincipal = new UsernamePasswordAuthenticationToken(userEmail, "password");

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(mockUser));

        // When
        User resultUser = userService.getLoggedUser(mockPrincipal);

        // Then
        assertEquals(mockUser, resultUser);
        verify(userRepository, times(1)).findByEmail(userEmail);
    }

    @Test
    public void shouldThrowUserNotExistException() {
        // Given
        String userEmail = "test@example.com";
        Principal mockPrincipal = new UsernamePasswordAuthenticationToken(userEmail, "password");

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotExist.class, () -> userService.getLoggedUser(mockPrincipal));
        verify(userRepository, times(1)).findByEmail(userEmail);
    }

    @Test
    void shouldGetUser() {
        // Given
        Principal mockPrincipal = new UsernamePasswordAuthenticationToken("test@example.com", "password");
        User mockUser = new User();
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));

        // When
        userService.getUser(mockPrincipal);

        // Then
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void shouldGetCoursesByLoggedUser() {
        // Given
        Principal mockPrincipal = new UsernamePasswordAuthenticationToken("test@example.com", "password");
        User mockUser = new User();
        Course course1 = new Course();
        Course course2 = new Course();
        mockUser.setCourses(Arrays.asList(course1, course2));

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));

        // When
        List<String> resultCourses = userService.getCoursesByLoggedUser(mockPrincipal);

        // Then
        assertEquals(2, resultCourses.size());
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void shouldFilterWithEmptyStudents() {
        // Given
        FilterStudent filterStudent = new FilterStudent(true, null);
        User emptyUser = new User();
        emptyUser.setEmail("test@example.com");
        when(userRepository.findUserByCoursesEmpty()).thenReturn(Optional.of(Collections.singletonList(emptyUser)));

        // When
        List<String> resultEmptyStudents = userService.filter(filterStudent);

        // Then
        assertEquals(1, resultEmptyStudents.size());
        assertEquals("test@example.com", resultEmptyStudents.get(0));
        verify(userRepository, times(1)).findUserByCoursesEmpty();
    }

    @Test
    void shouldFilterWithCourseCode() {
        // Given
        FilterStudent filterStudent = new FilterStudent(false, 123L);
        Course course = new Course();
        User student = new User();
        student.setEmail("student@example.com");
        course.setStudents(Collections.singletonList(student));
        when(courseRepository.findCourseByCourseCode(123L)).thenReturn(Optional.of(course));

        // When
        List<String> resultStudentsByCourse = userService.filter(filterStudent);

        // Then
        assertEquals(1, resultStudentsByCourse.size());
        assertEquals("student@example.com", resultStudentsByCourse.get(0));
        verify(courseRepository, times(1)).findCourseByCourseCode(123L);
    }

    @Test
    void shouldFilterWithInvalidParameters() {
        // Given
        FilterStudent filterStudent = new FilterStudent(true, 123L);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> userService.filter(filterStudent));
    }

    @Test
    void shouldGetUserByEmail() {
        // Given
        String userEmail = "test@example.com";
        User mockUser = new User();
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(mockUser));

        // When
        userService.getUserByEmail(userEmail);

        // Then
        verify(userRepository, times(1)).findByEmail(userEmail);
    }

    @Test
    void shouldChangePassword() {
        // Given
        ChangePasswordRequest request = new ChangePasswordRequest("oldPassword", "newPassword", "newPassword");
        UsernamePasswordAuthenticationToken mockPrincipal = mock(UsernamePasswordAuthenticationToken.class);
        User mockUser = new User();
        mockUser.setPassword("oldPassword");

        when(passwordEncoder.matches("oldPassword", "oldPassword")).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");
        when(mockPrincipal.getPrincipal()).thenReturn(mockUser);
        when(userRepository.save(mockUser)).thenReturn(mockUser);

        // When
        userService.changePassword(request, mockPrincipal);

        // Then
        assertEquals("encodedNewPassword", mockUser.getPassword());
        verify(userRepository, times(1)).save(mockUser);
    }

    @Test
    void shouldNotChangePassword() {
        // Given
        ChangePasswordRequest request = new ChangePasswordRequest("oldPassword", "newPassword", "Password");
        UsernamePasswordAuthenticationToken mockPrincipal = mock(UsernamePasswordAuthenticationToken.class);
        User mockUser = new User();
        mockUser.setPassword("oldPassword");

        when(passwordEncoder.matches("oldPassword", "oldPassword")).thenReturn(true);
        when(mockPrincipal.getPrincipal()).thenReturn(mockUser);

        // When
        assertThrows(IllegalStateException.class, () -> userService.changePassword(request, mockPrincipal));

        // Then
        assertEquals("oldPassword", mockUser.getPassword());
    }

    @Test
    void shouldUpdateUserDetails() {
        // Given
        UpdateUserRequest request = new UpdateUserRequest("John", "Doe", "new@example.com");
        Principal mockPrincipal = new UsernamePasswordAuthenticationToken("test@example.com", "password");
        User loggedUser = new User();
        loggedUser.setEmail("test@example.com");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(loggedUser));
        when(userRepository.findByEmail("new@example.com")).thenReturn(Optional.empty());

        // When
        userService.updateUserDetails(request, mockPrincipal);

        // Then
        assertEquals("John", loggedUser.getFirstName());
        assertEquals("Doe", loggedUser.getLastName());
        assertEquals("new@example.com", loggedUser.getEmail());
        verify(userRepository, times(1)).save(loggedUser);
    }

    @Test
    void shouldDeleteUserByEmail() {
        // Given
        String userEmail = "test@example.com";
        User mockUser = new User();
        mockUser.setEmail(userEmail);
        Course course = new Course();
        course.setStudents(new ArrayList<>(Collections.singletonList(mockUser)));
        mockUser.setCourses(Collections.singletonList(course));

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(mockUser));

        // When
        Status resultStatus = userService.deleteUserByEmail(userEmail);

        // Then
        assertEquals(Status.USER_DELETED, resultStatus);
        verify(userRepository, times(1)).delete(mockUser);
    }
}
