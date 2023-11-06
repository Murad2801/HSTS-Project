package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "coursestable")
public class Courses implements Serializable {
    @ManyToMany(mappedBy = "coursesList")
    public List<StudentsUser> studentsUsersList;
    @ManyToMany(mappedBy = "coursesList")
    public List<TeachersUser> teachersUserList;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String CourseName;
    private String CourseCode;
    @ManyToMany
    @JoinTable(
            name = "course_question",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    private List<Questions> questionList;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "SubjectID")
    private Subjects subject;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "CourseID")
    private List<Exams> examsList;

    public Courses(String name, Subjects sub) {

        this.CourseName = name;
        this.questionList = new ArrayList<Questions>();
        this.examsList = new ArrayList<Exams>();
        setSubject(sub);
    }

    public Courses() {
    }

    public String getCourseName() {
        return CourseName;
    }

    public String getCourseCode() {
        return CourseCode;
    }

    public List<Questions> getQuestionList() {
        return this.questionList;
    }

    public void AddQuestionToList(Questions ques) {
        this.questionList.add(ques);
    }

    public void addExamToTheList(Exams exam) {
        this.examsList.add(exam);
    }

    public int getId() {
        return id;
    }

    public void setCourseCode() {
        String courseId = String.format("%02d", this.id);
        this.CourseCode = courseId;
    }

    public List<Exams> getExamsList() {
        return examsList;
    }

    public Subjects getSubject() {
        return subject;
    }

    public void setSubject(Subjects subject) {
        this.subject = subject;
        subject.AddCourseToTheList(this);
    }

    public void setTeacher(TeachersUser teacher) {
        this.teachersUserList.add(teacher);
        teacher.AddCourseToTheList(this);
    }

}

