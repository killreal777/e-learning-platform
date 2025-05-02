package itmo.blps.elearningplatform.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "study")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Study extends BaseEntity {

    @Min(1)
    @NotNull
    @Column(name = "student_id", nullable = false)
    private Integer studentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cource_id", referencedColumnName = "id")
    private Course course;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Mark mark = Mark.FX;

    public Study(Integer studentId, Course course) {
        setStudentId(studentId);
        setCourse(course);
    }

    @Getter
    @RequiredArgsConstructor
    public enum Mark {

        A(0.9),
        B(0.8),
        C(0.7),
        D(0.65),
        E(0.6),
        FX(0);

        private final double minPercentage;
    }
}
