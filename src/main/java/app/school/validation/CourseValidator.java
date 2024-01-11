package app.school.validation;

import app.school.exception.exceptions.CourseAlreadyExist;
import app.school.model.Course;
import app.school.model.request.CourseRequest;
import app.school.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseValidator {

    private final CourseRepository courseRepository;

    public void createCourse(CourseRequest request) {
        Optional<List<Course>> courseByNameOrCourseCode = courseRepository.findCourseByNameOrCourseCode(request.getName(), request.getCourseCode());
        if (courseByNameOrCourseCode.isPresent() && !courseByNameOrCourseCode.get().isEmpty()) {
            throw new CourseAlreadyExist(request.getName(), request.getCourseCode());
        }
    }
}
