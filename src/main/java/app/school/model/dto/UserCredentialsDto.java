package app.school.model.dto;

import app.school.type.Role;

public record UserCredentialsDto(String email, String password, Role role) {
}
