package app.school.exception.exceptions;

public class CourseAlreadyExist extends RuntimeException {
    public CourseAlreadyExist(String courseName, Long courseCode) {
        super("Course with name: " + courseName + " or code: " + courseCode + " already exist ");
    }

}
