package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "examstable")
public class Exams implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int duration;

    private String noteForTeacher;

    private String noteForStudent;

    private String teacherName;


    private String Examcode;

    private String ExamIdentifier;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "CourseID")
    private Courses course;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "SubjectID")
    private Subjects subject;

    @ManyToMany
    @JoinTable(
            name = "exam_question",
            joinColumns = @JoinColumn(name = "exam_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    private List<Questions> questionsList;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "Exam_ID")
    private List<ReadyQuestions> readyQuestionsList;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "Exam_ID")
    private List<ReadyExams> readyExamsList;
    public Exams() {
        readyQuestionsList = new ArrayList<ReadyQuestions>();
        questionsList = new ArrayList<Questions>();
    }

    public Exams(int duration, String noteForTeacher, String NoteForStudent, String teacherName, Courses course, Subjects subject) {

        this.duration = duration;
        this.noteForTeacher = noteForTeacher;
        this.noteForStudent = NoteForStudent;
        this.teacherName = teacherName;
        setCourse(course);
        setSubject(subject);
        this.questionsList = new ArrayList<Questions>();

    }

    public Subjects getSubject() {
        return subject;
    }

    public void setSubject(Subjects subject) {
        this.subject = subject;
        subject.addExamToTheList(this);
    }

    public List<Questions> getQuestionsList() {
        return questionsList;
    }

    public void setQuestionsList(List<Questions> questionsList) {
        this.questionsList = questionsList;
    }

    public Courses getCourse() {
        return course;
    }

    public void setCourse(Courses course) {
        this.course = course;
        course.addExamToTheList(this);
    }

    // Constructor and other methods
    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getNoteForTeacher() {
        return noteForTeacher;
    }

    public void setNoteForTeacher(String noteForTeacher) {
        this.noteForTeacher = noteForTeacher;
    }

    public String getGetNoteForStudent() {
        return noteForStudent;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<ReadyQuestions> getReadyQuestionsList() {
        return readyQuestionsList;
    }

    public void setReadyQuestionsList(List<ReadyQuestions> readyQuestionsList) {
        this.readyQuestionsList = readyQuestionsList;
    }

    public void CreateIdentifier(Courses course, Subjects subject) {

        String courseId = course.getCourseCode();
        String subjectId = subject.getSubjCode();
        String ExamId = String.format("%02d", this.id);
        this.ExamIdentifier = subjectId.concat(" ").concat(courseId).concat(" ").concat(ExamId);
        setExamCode(ExamId);
    }

    public void setExamCode(String idd) {
        this.Examcode = idd;
    }

    public void addQuestions(Questions ques) {
        this.questionsList.add(ques);
        ques.addExam(this);
    }

    public void addReadyExam(ReadyExams readyExam) {
        this.readyExamsList.add(readyExam);
    }

    public List<ReadyExams> getReadyExamsList() {
        return readyExamsList;
    }

    public String getExamcode() {
        return Examcode;
    }

    public void setExamcode(String examcode) {
        Examcode = examcode;
    }

    public String getNoteForStudent() {
        return noteForStudent;
    }

    public void setNoteForStudent(String getNoteForStudent) {
        this.noteForStudent = getNoteForStudent;
    }

    public void addReadyQestionToList(ReadyQuestions question) {
        this.readyQuestionsList.add(question);
    }

    public String getExamIdentifier() {
        return ExamIdentifier;
    }

    public void setExamIdentifier(String examIdentifier) {
        ExamIdentifier = examIdentifier;
    }
}
