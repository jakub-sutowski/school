package app.school.exception.exceptions;

public class UserAlreadyExist extends RuntimeException {
    public UserAlreadyExist(String email) {
        super("Student with email " + email + " already exist");
    }
}
