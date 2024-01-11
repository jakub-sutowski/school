package app.school.service;

import app.school.exception.exceptions.CourseNotExist;
import app.school.exception.exceptions.NoEmptyCourses;
import app.school.exception.exceptions.UserNotExist;
import app.school.model.Course;
import app.school.model.User;
import app.school.model.dto.UserDto;
import app.school.pagination.FilterStudent;
import app.school.repository.CourseRepository;
import app.school.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final ModelMapper modelMapper;

    public UserDto getUser(Principal principal) {
        User user = getLoggedUser(principal);
        return getUserDto(user);
    }

    public UserDto getUserDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    public User getLoggedUser(Principal principal) {
        String email = principal.getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotExist(email));
    }

    public List<String> getCoursesByLoggedUser(Principal principal) {
        User loggedUser = getLoggedUser(principal);
        List<Course> courses = loggedUser.getCourses();
        return courses.stream().map(Course::getName).toList();
    }

    private List<User> getEmptyUsers() {
        return userRepository.findUserByCoursesEmpty().orElseThrow(NoEmptyCourses::new);
    }

    private List<User> getStudentsByCourse(Long courseCode) {
        Course course = courseRepository.findCourseByCourseCode(courseCode).orElseThrow(() -> new CourseNotExist(courseCode));
        return course.getStudents();
    }

    public List<String> filter(FilterStudent filterStudent) {
        if (filterStudent.getIsEmpty() && filterStudent.getCourseCode() != null) {
            throw new IllegalArgumentException("Both empty and student parameters cannot be specified together");
        } else if (filterStudent.getIsEmpty()) {
            List<User> emptyStudents = getEmptyUsers();
            return emptyStudents.stream().map(User::getEmail).toList();
        } else if (!filterStudent.getCourseCode().toString().isEmpty()) {
            List<User> coursesByUserEmail = getStudentsByCourse(filterStudent.getCourseCode());
            return coursesByUserEmail.stream().map(User::getEmail).toList();
        } else {
            throw new IllegalArgumentException("Either empty or student parameter should be specified");
        }
    }
}
