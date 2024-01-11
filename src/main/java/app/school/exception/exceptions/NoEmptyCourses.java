package app.school.exception.exceptions;

public class NoEmptyCourses extends RuntimeException {
    public NoEmptyCourses() {
        super("No empty courses");
    }
}
