package app.school.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {
    ADMIN_ROLE_ASSIGNED,
    COURSE_CREATED,
    COURSE_DELETED,
    COURSE_IS_FULL,
    STUDENT_ADDED_TO_COURSE,
    STUDENT_ALREADY_REGISTERED,
    STUDENT_HAS_MAX_COURSES,
    SUCCESS,
    USER_ALREADY_EXIST,
    USER_CREATED,
    USER_DELETED
}