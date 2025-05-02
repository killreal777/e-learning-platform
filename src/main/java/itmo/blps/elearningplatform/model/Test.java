package itmo.blps.elearningplatform.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "test")
@Getter
@Setter
@NoArgsConstructor
public class Test extends BaseEntity {

    @NotBlank
    @Size(max = 64)
    @Column(name = "name", nullable = false, length = 64)
    private String name;

    @NotBlank
    @Size(max = 64)
    @Column(name = "description", nullable = false, length = 64)
    private String description;

    @OneToMany(mappedBy = "test", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Question> questions;

    @Min(0)
    @Column(name = "time_limit_seconds", nullable = true)
    private Integer timeLimitSeconds;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private Course course;
}
