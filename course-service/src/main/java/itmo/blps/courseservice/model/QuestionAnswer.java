package itmo.blps.courseservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "question_answer")
@Getter
@Setter
@NoArgsConstructor
public class QuestionAnswer extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "test_answer_id", referencedColumnName = "id")
    TestAnswer testAnswer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    Question question;

    @NotNull
    @Min(1)
    @Max(4)
    @Column(name = "selected_option", nullable = false)
    Integer selectedOption;

    @Min(0)
    @Column(name = "score")
    private Integer score;
}
