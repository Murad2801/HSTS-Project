package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;


@Entity
@Table(name = "readyexamstable")
public class ReadyExams implements Serializable {
    @ManyToOne
    @JoinColumn(name = "Exam_Id")
    Exams exam;
    @ManyToMany(mappedBy = "availableExamsList")
    private List<StudentsUser> students;        // the students that can solve this exam
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    private String examPassCode;
    private String examType;
    private String subjectName;
    private String courseName;

    private int startedExam;
    private int finishedBeforeTime;
    private int automaticSubmission;
    private String date;
    private int duration;
    private int updatedDuration;
    private String examIdentifier;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ready_exams_ID")
    private List<Request> requestList;
    private String publishingTeacher;
    @OneToMany
    @JoinColumn(name = "ReadyExam_Id")
    private List<SolvedExams> solvedExams;
    @OneToMany
    @JoinColumn(name = "Ready_Exam_Id")
    private List<TraditionalSolvedExam> traditionalSolvedExamsList;

    public ReadyExams(Exams exam, String examPassCode, String examType, String publishingTeacher, List<StudentsUser> studentsList) {
        this.exam = exam;
        this.examPassCode = examPassCode;
        this.examType = examType;
        this.duration = exam.getDuration();
        this.courseName = exam.getCourse().getCourseName();
        this.subjectName = exam.getSubject().getSubjectName();
        this.examIdentifier = exam.getExamIdentifier();
        this.publishingTeacher = publishingTeacher;
        this.solvedExams = new ArrayList<>();
        this.traditionalSolvedExamsList = new ArrayList<>();
        this.students = studentsList;
        this.date = LocalDate.now().toString();
        this.startedExam = 0;
        this.finishedBeforeTime = 0;
        this.automaticSubmission = 0;
        AddExamForStudents(studentsList);

    }

    public ReadyExams() {
        solvedExams = new ArrayList<>();
    }

    public int getUpdatedDuration() {
        return updatedDuration;
    }

    public void setUpdatedDuration(int updatedDuration) {
        this.updatedDuration = updatedDuration;
    }

    public String getPublishingTeacher() {
        return publishingTeacher;
    }


    public void setPublishingTeacher(String publishingTeacher) {
        this.publishingTeacher = publishingTeacher;
    }

    public Exams getExam() {
        return exam;
    }

    public void setExam(Exams exam) {
        this.exam = exam;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExamPassCode() {
        return examPassCode;
    }

    public void setExamPassCode(String examPassCode) {
        this.examPassCode = examPassCode;
    }

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getExamIdentifier() {
        return examIdentifier;
    }

    public void setExamIdentifier(String examIdentifier) {
        this.examIdentifier = examIdentifier;
    }

    public void addRequest(Request request) {
        this.requestList.add(request);
    }

    public List<Request> getRequestList() {
        return requestList;
    }

    public void setRequestList(List<Request> requestList) {
        this.requestList = requestList;
    }

    public List<SolvedExams> getSolvedExams() {
        return solvedExams;
    }

    public void setSolvedExams(List<SolvedExams> solvedExams) {
        this.solvedExams = solvedExams;
    }

    public void addSolvedExams(SolvedExams solvedExam) {
        this.solvedExams.add(solvedExam);
    }

    public void addTraditionalExams(TraditionalSolvedExam traditionalSolvedExam) {
        this.traditionalSolvedExamsList.add(traditionalSolvedExam);
    }


    public void updateDuration(int extraTime) {
        duration += extraTime;
    }

    public void AddExamForStudents(List<StudentsUser> studentsList) {
        for (StudentsUser student : studentsList) {
            student.AddReadyExamToTheList(this);
        }
    }

    public List<StudentsUser> getStudentsList() {
        return students;
    }

    public String getDate() {
        return date;
    }

    public int getStartedExam() {
        return startedExam;
    }

    public void setStartedExam() {
        this.startedExam++;
    }

    public int getFinishedBeforeTime() {
        return finishedBeforeTime;
    }

    public void setFinishedBeforeTime() {
        this.finishedBeforeTime++;
    }

    public int getAutomaticSubmission() {
        return automaticSubmission;
    }

    public void setAutomaticSubmission() {
        this.automaticSubmission++;
    }
}
