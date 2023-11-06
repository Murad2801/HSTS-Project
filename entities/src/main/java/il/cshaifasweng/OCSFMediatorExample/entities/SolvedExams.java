package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


@Entity
@Table(name = "solvedexamstable")
public class SolvedExams implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    private int studentGrade;
    private String StudentName;

    private int fullGrade;
    private String completionTime;
    private String studentId; // Remember it's a String !!!!

    private String ShowThisGrade;

    private String noteForStudent;

    private String updateGradeReason;

    private String approved;
    @ManyToOne
    @JoinColumn(name = "readyExam_Id")
    private ReadyExams readyExam;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "answers")
    private List<Answer> answerList;

    public SolvedExams(ReadyExams readyExam, String completionTime, List<Answer> answers, String studentId, int fullGrade, int studentGrade, String name) {
        this.readyExam = readyExam;
        this.completionTime = completionTime;
        this.studentId = studentId;
        this.answerList = answers;
        this.fullGrade = fullGrade;
        this.studentGrade = studentGrade;
        this.StudentName = name;
        this.approved = "no";
        readyExam.addSolvedExams(this);
    }

    public SolvedExams() {
    }

    public String getCompletionTime() {
        return completionTime;
    }

    public List<Answer> getAnswers() {
        return answerList;
    }

    public void setAnswers(List<Answer> answers) {
        this.answerList = answers;
    }

    public int getStudentGrade() {
        return studentGrade;
    }

    public void setStudentGrade(int studentGrade) {
        if(studentGrade > 100){
            studentGrade = 100;
        }
        this.studentGrade = studentGrade;
    }

    public int getFullGrade() {
        return fullGrade;
    }

    public void setFullGrade(int fullGrade) {
        this.fullGrade = fullGrade;
    }

    public ReadyExams getReadyExam() {
        return readyExam;
    }

    public String getStudentName() {
        return StudentName;
    }

    public String getStudentId() {
        return studentId;
    }

    public List<Answer> getAnswerList() {
        return answerList;
    }

    public String getUpdateGradeReason() {
        return updateGradeReason;
    }

    public void setUpdateGradeReason(String updateGradeReason) {
        this.updateGradeReason = updateGradeReason;
    }

    public int getId() {
        return id;
    }

    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }

    public String getShowThisGrade() {
        return ShowThisGrade;
    }

    public void setShowThisGrade() {
        String grade = studentGrade + "/" + fullGrade;
        ShowThisGrade = grade;
    }

    public String getNoteForStudent() {
        return noteForStudent;
    }

    public void setNoteForStudent(String noteForStudent) {
        this.noteForStudent = noteForStudent;
    }
}
