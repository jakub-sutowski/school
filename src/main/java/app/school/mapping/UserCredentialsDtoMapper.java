package app.school.mapping;

import app.school.model.dto.UserCredentialsDto;
import app.school.model.entity.User;
import app.school.type.Role;
import org.springframework.stereotype.Component;

@Component
public class UserCredentialsDtoMapper {
    public static UserCredentialsDto map(User user) {
        String email = user.getEmail();
        String password = user.getPassword();
        Role role = user.getRole();

        return new UserCredentialsDto(email, password, role);
    }
}
