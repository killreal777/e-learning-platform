package itmo.blps.elearningplatform.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "test_answer")
@Getter
@Setter
@NoArgsConstructor
public class TestAnswer extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    User student;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "test_id", referencedColumnName = "id")
    Test test;

    @OneToMany(mappedBy = "testAnswer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    List<QuestionAnswer> questionAnswers;

    @NotNull
    @Column(name = "actual", nullable = false)
    private Boolean actual = true;
}
