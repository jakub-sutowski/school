package app.school.exception.exceptions;

public class CourseNotExist extends RuntimeException {

    public CourseNotExist(Long courseCode) {

        super("Course with code" + courseCode + " not exist");
    }
}
