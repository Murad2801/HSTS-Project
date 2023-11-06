package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Answer implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "answers")
    public SolvedExams solvedExam;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    private int questionNumber;
    private String chosenAnswer;

    public Answer(int questionNumber, String answer) {
        this.questionNumber = questionNumber;
        chosenAnswer = answer;
    }

    public Answer() {
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public String getChosenAnswer() {
        return chosenAnswer;
    }

    public void setChosenAnswer(String answer) {
        chosenAnswer = answer;
    }
}
