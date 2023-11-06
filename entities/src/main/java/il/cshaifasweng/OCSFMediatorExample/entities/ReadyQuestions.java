package il.cshaifasweng.OCSFMediatorExample.entities;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "readyquestions")
public class ReadyQuestions implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String questionCode;

    private String rightAnswer;

    private int grade;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "Exam_Id")
    private Exams exam;

    public ReadyQuestions(String questionCode, int grade, String RightAnswer) {
        this.questionCode = questionCode;
        this.grade = grade;
        this.rightAnswer = RightAnswer;
    }

    public ReadyQuestions() {
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestionCode() {
        return questionCode;
    }

    public void setQuestionCode(String questionCode) {
        this.questionCode = questionCode;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public Exams getExam() {
        return exam;
    }

    public void setExam(Exams exam) {
        this.exam = exam;
    }

    public void setRightAnswer(String rightAnswer) {this.rightAnswer = rightAnswer;}

    public String getRightAnswer() {
        return rightAnswer;
    }
}
