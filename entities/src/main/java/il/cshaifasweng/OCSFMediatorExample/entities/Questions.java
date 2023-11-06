package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "questionstable")
public class Questions implements Serializable {

    @ManyToMany(mappedBy = "questionList")
    public List<Courses> courses;
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "questionsList")
    public List<Exams> examsList;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    private String Question;
    private String Answer1;
    private String Answer2;
    private String Answer3;
    private String Answer4;
    private String correctAnswer;
    private String questionCode;
    private String question_Identifier;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "SubjectCode")
    private Subjects subject;


    public Questions(String question, String answer1, String answer2, String answer3, String answer4, String correct, Subjects sub, Courses course) {
        super();
        this.Question = question;
        this.Answer1 = answer1;
        this.Answer2 = answer2;
        this.Answer3 = answer3;
        this.Answer4 = answer4;
        this.correctAnswer = correct;
        setSubject(sub);
        this.courses = new ArrayList<Courses>();
        this.examsList = new ArrayList<Exams>();
        setCourse(course);
    }

    public Questions() {
        this.courses = new ArrayList<Courses>();
        this.examsList = new ArrayList<Exams>();
    }

    //getters
    public String getQuestion() {
        return Question;
    }

    public String getQuestionCode() {
        return questionCode;
    }

    public void setQuestionCode(String idd) {

        this.questionCode = idd;
    }

    public String getAnswer1() {
        return Answer1;
    }

    public String getAnswer2() {
        return Answer2;
    }

    public String getAnswer3() {
        return Answer3;
    }

    public String getAnswer4() {
        return Answer4;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    //setters

    public Subjects getSubject() {
        return subject;
    }

    public void setSubject(Subjects subject) {
        this.subject = subject;
        subject.AddQuestionToTheList(this);
    }

    public void setCourse(Courses course) {
        this.courses.add(course);
        course.AddQuestionToList(this);
    }

    public List<Courses> getCourses() {
        return courses;
    }

    public void CreateIdentifier(Courses course) {

        String courseId = course.getCourseCode();
        String questionId = String.format("%03d", this.id);
        this.question_Identifier = courseId.concat(" ").concat(questionId);
        setQuestionCode(questionId);
    }

    public void addExam(Exams exam) {
        this.examsList.add(exam);
    }

    public List<String> getAllAnswers() {
        List<String> answersList = new ArrayList<>();
        answersList.add(this.Answer1);
        answersList.add(this.Answer2);
        answersList.add(this.Answer3);
        answersList.add(this.Answer4);
        return answersList;
    }
}
