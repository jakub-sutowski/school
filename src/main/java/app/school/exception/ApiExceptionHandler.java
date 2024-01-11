package app.school.exception;

import app.school.exception.exceptions.CourseAlreadyExist;
import app.school.exception.exceptions.CourseNotExist;
import app.school.exception.exceptions.UserAlreadyExist;
import app.school.exception.exceptions.UserNotExist;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZonedDateTime;

@RestControllerAdvice
@Log4j2
public class ApiExceptionHandler {

    @ExceptionHandler(value = {UserAlreadyExist.class})
    public ResponseEntity<Object> handlerUserAlreadyExist(UserAlreadyExist exception) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ApiException apiException = new ApiException(
                exception.getMessage(),
                httpStatus,
                ZonedDateTime.now()
        );
        log.error("UserAlreadyExist: {}", exception.getMessage());
        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(value = {UserNotExist.class})
    public ResponseEntity<Object> handlerUserNotExist(UserNotExist exception) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        ApiException apiException = new ApiException(
                exception.getMessage(),
                httpStatus,
                ZonedDateTime.now()
        );
        log.error("UserNotExist: {}", exception.getMessage());
        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(value = {CourseAlreadyExist.class})
    public ResponseEntity<Object> handlerCourseAlreadyExist(CourseAlreadyExist exception) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ApiException apiException = new ApiException(
                exception.getMessage(),
                httpStatus,
                ZonedDateTime.now()
        );
        log.error("CourseAlreadyExist: {}", exception.getMessage());
        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(value = {CourseNotExist.class})
    public ResponseEntity<Object> handlerCourseNotExist(CourseNotExist exception) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        ApiException apiException = new ApiException(
                exception.getMessage(),
                httpStatus,
                ZonedDateTime.now()
        );
        log.error("CourseNotExist: {}", exception.getMessage());
        return new ResponseEntity<>(apiException, httpStatus);
    }
}
