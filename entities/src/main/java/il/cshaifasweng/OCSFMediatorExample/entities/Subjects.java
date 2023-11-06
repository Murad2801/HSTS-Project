package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "subjectstable")
public class Subjects implements Serializable {
    @ManyToMany(mappedBy = "subjectsList") //careful
    public List<StudentsUser> studentsUsersList;
    @ManyToMany(mappedBy = "subjectsList", fetch = FetchType.EAGER)//careful
    public List<TeachersUser> teachersUsersList;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String subjectName;
    private String subjectCode;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "SubjectCode")
    private List<Questions> questionList;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "SubjectID")
    private List<Courses> coursesList;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "SubjectID")
    private List<Exams> examsList;

    public Subjects(String name) {

        this.subjectName = name;
        this.questionList = new ArrayList<Questions>();
        this.coursesList = new ArrayList<Courses>();
        this.examsList = new ArrayList<Exams>();
        this.teachersUsersList = new ArrayList<>();
        this.studentsUsersList = new ArrayList<>();
    }

    public Subjects() {
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getSubjCode() {
        return subjectCode;
    }

    public List<Questions> getQuestionList() {
        return questionList;
    }

    public List<Courses> getCoursesList() {
        return coursesList;
    }

    public void AddQuestionToTheList(Questions question) {
        this.questionList.add(question);
    }

    public void AddCourseToTheList(Courses course) {
        this.coursesList.add(course);
    }

    public void addExamToTheList(Exams exam) {
        this.examsList.add(exam);
    }

    public void setSubjectCode() {
        String subjectId = String.format("%02d", this.id);
        this.subjectCode = subjectId;
    }

    public void setTeacher(TeachersUser teacher) {
        this.teachersUsersList.add(teacher);
        teacher.AddSubjectToTheList(this);
    }
}
