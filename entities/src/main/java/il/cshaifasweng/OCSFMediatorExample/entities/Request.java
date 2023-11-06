package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "requestsTable")
public class Request implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ready_exams_ID")
    private ReadyExams readyExam;

    private int requestAddedTime;
    private String requestText;
    private String courseName;
    private String publishingTeacher;
    private String ExamId;


    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setPublishingTeacher(String publishingTeacher) {
        this.publishingTeacher = publishingTeacher;
    }

    public String getPublishingTeacher() {
        return publishingTeacher;
    }

    public String getExamId() {
        return ExamId;
    }

    public void setExamId(String examId) {
        ExamId = examId;
    }

    public Request(ReadyExams readyExam, int requestAddedTime, String requestText) {
        this.readyExam = readyExam;
        this.requestAddedTime = requestAddedTime;
        this.requestText = requestText;
        this.courseName = readyExam.getCourseName();
        this.publishingTeacher = readyExam.getPublishingTeacher();
        this.ExamId = readyExam.getExamIdentifier();
    }
    public Request(){}

    public void setReadyExam(ReadyExams readyExam) {
        this.readyExam = readyExam;
    }

    public void setRequestAddedTime(int requestAddedTime) {
        this.requestAddedTime = requestAddedTime;
    }

    public void setRequestText(String requestText) {
        this.requestText = requestText;
    }

    public ReadyExams getReadyExam() {
        return readyExam;
    }

    public int getRequestAddedTime() {
        return requestAddedTime;
    }

    public String getRequestText() {
        return requestText;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}