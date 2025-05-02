package itmo.blps.elearningplatform.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "test_answer")
@Getter
@Setter
@NoArgsConstructor
public class TestAnswer extends BaseEntity {

    @Min(1)
    @NotNull
    @Column(name = "student_id", nullable = false)
    private Integer studentId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "test_id", referencedColumnName = "id")
    Test test;

    @OneToMany(mappedBy = "testAnswer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    List<QuestionAnswer> questionAnswers;

    @CreatedDate
    @NotNull
    @Column(name = "startTime", nullable = false, updatable = false)
    private LocalDateTime startTime;

    @Column(name = "endTime", nullable = true)
    private LocalDateTime endTime;

    @NotNull
    @Column(name = "actual", nullable = false)
    private Boolean actual = false;

    // TODO ABC mark
    // repo.save(testAnswer)
}
