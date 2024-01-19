package app.school.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    private String name;

    @NotNull
    private Long courseCode;

    private String description;

    @ManyToMany(mappedBy = "courses")
    private List<User> students;

    public void addUserToCourse(User user) {
        if (!students.contains(user)) {
            students.add(user);
            user.getCourses().add(this);
        }
    }
}