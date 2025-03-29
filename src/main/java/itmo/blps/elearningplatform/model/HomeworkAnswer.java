package itmo.blps.elearningplatform.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "homework_answer")
@Getter
@Setter
@NoArgsConstructor
public class HomeworkAnswer extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    private User student;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "homework_id", referencedColumnName = "id")
    private Homework homework;

    @NotBlank
    @Size(max = 512)
    @Column(name = "text", nullable = false, length = 512)
    private String text;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reviewer_id", referencedColumnName = "id")
    private User reviewer;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status = Status.SENT;

    @Min(0)
    @Column(name = "score")
    private Integer score;

    @NotNull
    @Column(name = "actual", nullable = false)
    private Boolean actual = true;

    public enum Status {
        SENT, REVIEWED
    }
}
