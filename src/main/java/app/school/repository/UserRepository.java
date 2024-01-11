package app.school.repository;

import app.school.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);
    Optional<List<User>> findUserByCoursesEmpty();

    boolean existsByEmail(String email);
}
