package app.school.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {
    SUCCESS,
    USER_CREATED,
    USER_ALREADY_EXIST,
    COURSE_CREATED,
    STUDENT_HAS_MAX_COURSES,
    COURSE_IS_FULL, STUDENT_ADDED_TO_COURSE
}
