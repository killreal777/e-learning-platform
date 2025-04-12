package itmo.blps.elearningplatform.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
