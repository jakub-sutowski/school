package app.school.service;

import app.school.exception.exceptions.CourseNotExist;
import app.school.exception.exceptions.NoEmptyCourses;
import app.school.exception.exceptions.UserNotExist;
import app.school.model.dto.UserDto;
import app.school.model.entity.Course;
import app.school.model.entity.User;
import app.school.model.request.ChangePasswordRequest;
import app.school.model.request.UpdateUserRequest;
import app.school.pagination.FilterStudent;
import app.school.repository.CourseRepository;
import app.school.repository.UserRepository;
import app.school.type.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

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

    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotExist(email));
        return getUserDto(user);
    }

    @Transactional
    public void changePassword(ChangePasswordRequest request, Principal principal) {
        var user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }

        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Passwords are not the same");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Transactional
    public UserDto updateUserDetails(UpdateUserRequest request, Principal principal) {
        User loggedUser = getLoggedUser(principal);

        if (request.getFirstName() != null) {
            loggedUser.setFirstName(request.getFirstName());
        }

        if (request.getLastName() != null) {
            loggedUser.setLastName(request.getLastName());
        }

        if (request.getEmail() != null) {
            if (!request.getEmail().equals(loggedUser.getEmail())) {
                if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                    throw new IllegalStateException("Email is already taken");
                }
                loggedUser.setEmail(request.getEmail());
            }
        }
        userRepository.save(loggedUser);
        return getUserDto(loggedUser);
    }

    @Transactional
    public Status deleteUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotExist(email));

        for (Course course : user.getCourses()) {
            course.getStudents().remove(user);
        }
        userRepository.delete(user);
        return Status.USER_DELETED;
    }
}