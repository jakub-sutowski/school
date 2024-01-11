package app.school.exception.exceptions;

public class UserNotExist extends RuntimeException {

    public UserNotExist(String email) {

        super("Student with email " + email + " not exist");
    }
}
