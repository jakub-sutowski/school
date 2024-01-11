package app.school.service;

import app.school.exception.exceptions.CourseNotExist;
import app.school.exception.exceptions.NoEmptyStudents;
import app.school.exception.exceptions.UserNotExist;
import app.school.model.Course;
import app.school.model.User;
import app.school.model.dto.CourseDto;
import app.school.model.request.CourseRequest;
import app.school.pagination.FilterCourse;
import app.school.repository.CourseRepository;
import app.school.repository.UserRepository;
import app.school.type.Status;
import app.school.validation.CourseValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class CourseService {

    private final UserService userService;
    private final CourseValidator courseValidator;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Value("${max_student_in_course}")
    private int MAX_STUDENTS_IN_COURSE;

    @Value("${max_courses_for_student}")
    private int MAX_COURSES_FOR_STUDENT;

    @Transactional
    public Status createCourse(CourseRequest request) {
        courseValidator.createCourse(request);
        Course createdCourse = modelMapper.map(request, Course.class);
        courseRepository.save(createdCourse);
        return Status.COURSE_CREATED;
    }

    @Transactional
    public Status joinUserToCourse(Principal principal, Long courseCode) {
        User loggedUser = userService.getLoggedUser(principal);
        Course course = courseRepository.findCourseByCourseCode(courseCode).orElseThrow(() -> new CourseNotExist(courseCode));
        if (!(loggedUser.getCourses().size() < MAX_COURSES_FOR_STUDENT)) {
            return Status.STUDENT_HAS_MAX_COURSES;
        } else if (!(course.getStudents().size() < MAX_STUDENTS_IN_COURSE)) {
            return Status.COURSE_IS_FULL;
        } else {
            course.addUserToCourse(loggedUser);
            courseRepository.save(course);
            return Status.STUDENT_ADDED_TO_COURSE;
        }
    }

    private List<Course> getEmptyCourses() {
        return courseRepository.findCoursesByStudentsEmpty().orElseThrow(NoEmptyStudents::new);
    }

    private List<Course> getCoursesByUserEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotExist(email));
        return user.getCourses();
    }

    public List<String> filter(FilterCourse filterCourse) {
        if (filterCourse.getIsEmpty() && filterCourse.getStudentEmail() != null) {
            throw new IllegalArgumentException("Both empty and student parameters cannot be specified together");
        } else if (filterCourse.getIsEmpty()) {
            List<Course> emptyCourses = getEmptyCourses();
            return emptyCourses.stream().map(Course::getName).toList();
        } else if (!filterCourse.getStudentEmail().isEmpty()) {
            List<Course> coursesByUserEmail = getCoursesByUserEmail(filterCourse.getStudentEmail());
            return coursesByUserEmail.stream().map(Course::getName).toList();
        } else {
            throw new IllegalArgumentException("Either empty or student parameter should be specified");
        }
    }

    public Page<Map<String, List<String>>> getRelations(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Course> coursesPage = courseRepository.findAll(pageRequest);
        return coursesPage.map(course -> {
            Map<String, List<String>> map = new HashMap<>();
            map.put(course.getName(), course.getStudents().stream().map(User::getEmail).toList());
            return map;
        });
    }

    public CourseDto getCourseByCode(Long courseCode) {
        Course course = courseRepository.findCourseByCourseCode(courseCode).orElseThrow(() -> new CourseNotExist(courseCode));
        return modelMapper.map(course, CourseDto.class);
    }
}
