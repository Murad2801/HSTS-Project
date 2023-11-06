package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class StudentsUser extends Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String StudentId;

    @ManyToMany
    @JoinTable(
            name = "Student_Availalbe",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "exam_id")
    )
    private List<ReadyExams> availableExamsList;

    @ManyToMany
    @JoinTable(
            name = "student_subject",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private List<Subjects> subjectsList;
    @ManyToMany
    @JoinTable(
            name = "student_course",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Courses> coursesList;

    public StudentsUser(String username, String password, String role, String fullname, String studentId) {
        super(username, password, role, fullname);
        this.coursesList = new ArrayList<>();
        this.subjectsList = new ArrayList<>();
        this.StudentId = studentId;
        this.availableExamsList = new ArrayList<>();

    }

    public StudentsUser() {
    }

    public List<Subjects> getSubjectsList() {
        return subjectsList;
    }

    public List<Courses> getCoursesList() {
        return coursesList;
    }

    public void AddCourseToTheList(Courses course) {
        if (this.coursesList == null) {
            this.coursesList = new ArrayList<>();
        }
        this.coursesList.add(course);
    }

    public void AddSubjectToTheList(Subjects subject) {
        if (this.subjectsList == null) {
            this.subjectsList = new ArrayList<>();
        }
        this.subjectsList.add(subject);
    }

    public String getStudentId() {
        return StudentId;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return super.getUsername();
    }

    public void AddReadyExamToTheList(ReadyExams exam) {

        this.availableExamsList.add(exam);
    }

    public List<ReadyExams> getAvailableExamsList() {
        return availableExamsList;
    }

}