package app.school.service;

import app.school.exception.exceptions.CourseNotExist;
import app.school.model.entity.Course;
import app.school.model.entity.User;
import app.school.model.request.CourseRequest;
import app.school.model.request.UpdateCourseRequest;
import app.school.model.response.CourseResponse;
import app.school.repository.CourseRepository;
import app.school.repository.UserRepository;
import app.school.type.Status;
import app.school.validation.CourseValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    @Mock
    private UserService userService;
    @Mock
    private CourseValidator courseValidator;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CourseService courseService;

    @Test
    public void shouldCreateCourse() {
        // Given
        CourseRequest request = new CourseRequest();
        when(modelMapper.map(request, Course.class)).thenReturn(new Course());

        // When
        Status resultStatus = courseService.createCourse(request);

        // Then
        assertEquals(Status.COURSE_CREATED, resultStatus);
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void shouldJoinUserToCourse() {
        // Given
        Principal mockPrincipal = mock(Principal.class);
        User user = new User();
        Course course = new Course();
        user.setCourses(new ArrayList<>());
        course.setStudents(new ArrayList<>());
        ReflectionTestUtils.setField(courseService, "MAX_STUDENTS_IN_COURSE", 50);
        ReflectionTestUtils.setField(courseService, "MAX_COURSES_FOR_STUDENT", 5);

        when(userService.getLoggedUser(mockPrincipal)).thenReturn(user);
        when(courseRepository.findCourseByCourseCode(123L)).thenReturn(Optional.of(course));

        // When
        Status resultStatus = courseService.joinUserToCourse(mockPrincipal, 123L);

        // Then
        assertEquals(Status.STUDENT_ADDED_TO_COURSE, resultStatus);
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void shouldGetRelations() {
        // Given
        Page<Course> coursesPage = mock(Page.class);
        when(courseRepository.findAll((Pageable) any())).thenReturn(coursesPage);
        when(coursesPage.map(any())).thenReturn(mock(Page.class));

        // When
        Page<Map<String, List<String>>> resultPage = courseService.getRelations(0, 10);

        // Then
        assertNotNull(resultPage);
        verify(courseRepository, times(1)).findAll((Pageable) any());
    }

    @Test
    void shouldGetCourseByCode() {
        // Given
        Long courseCode = 123L;
        Course mockCourse = new Course();
        mockCourse.setStudents(new ArrayList<>());
        when(courseRepository.findCourseByCourseCode(courseCode)).thenReturn(Optional.of(mockCourse));

        // When
        CourseResponse courseByCode = courseService.getCourseByCode(courseCode);

        // Then
        assertNotNull(courseByCode);
        verify(courseRepository, times(1)).findCourseByCourseCode(courseCode);
    }

    @Test
    void shouldThrowCourseNotExistException() {
        // When & Then
        assertThrows(CourseNotExist.class, () -> courseService.getCourseByCode(123L));
    }

    @Test
    void shouldUpdateCourseName() {
        // Given
        Long courseCode = 123L;
        Course course = new Course();
        course.setName("Name");
        UpdateCourseRequest mockRequest = new UpdateCourseRequest();
        mockRequest.setName("New name");

        when(courseRepository.findCourseByCourseCode(courseCode)).thenReturn(Optional.of(course));

        // When
        courseService.updateCourseDetails(courseCode, mockRequest);

        // Then
        assertEquals(course.getName(), mockRequest.getName());
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void shouldDeleteCourse() {
        // Given
        Long courseCode = 123L;
        Course course = new Course();
        course.setCourseCode(courseCode);
        course.setStudents(new ArrayList<>());

        when(courseRepository.findCourseByCourseCode(courseCode)).thenReturn(Optional.of(course));

        // When
        Status resultStatus = courseService.deleteCourseByCode(courseCode);

        // Then
        assertEquals(Status.COURSE_DELETED, resultStatus);
        verify(courseRepository, times(1)).delete(course);
    }
}