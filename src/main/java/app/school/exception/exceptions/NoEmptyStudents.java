package app.school.exception.exceptions;

public class NoEmptyStudents extends RuntimeException {
    public NoEmptyStudents() {
        super("No empty students");
    }
}
