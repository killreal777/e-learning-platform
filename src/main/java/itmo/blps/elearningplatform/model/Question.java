package itmo.blps.elearningplatform.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "question")
@Getter
@Setter
@NoArgsConstructor
public class Question extends BaseEntity {

    @NotBlank
    @Size(max = 64)
    @Column(name = "text", nullable = false, length = 64)
    private String text;

    @NotBlank
    @Size(max = 64)
    @Column(name = "option_1", nullable = false, length = 64)
    private String option1;

    @NotBlank
    @Size(max = 64)
    @Column(name = "option_2", nullable = false, length = 64)
    private String option2;

    @NotBlank
    @Size(max = 64)
    @Column(name = "option_3", nullable = false, length = 64)
    private String option3;

    @NotBlank
    @Size(max = 64)
    @Column(name = "option_4", nullable = false, length = 64)
    private String option4;

    @NotNull
    @Min(1)
    @Max(4)
    @Column(name = "correct_option", nullable = false)
    private Integer correctOption;

    @NotNull
    @Min(1)
    @Column(name = "max_score", nullable = false)
    private Integer maxScore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id", referencedColumnName = "id")
    private Test test;
}
