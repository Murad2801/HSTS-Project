package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class TeachersUser extends Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToMany
    @JoinTable(
            name = "teacher_subject",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private List<Subjects> subjectsList;
    @ManyToMany
    @JoinTable(
            name = "teacher_course",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Courses> coursesList;



    public TeachersUser(String username, String password, String role, String fullname) {
        super(username, password, role, fullname);
        this.coursesList = new ArrayList<>();
        this.subjectsList = new ArrayList<>();
    }

    public TeachersUser() {
    }
    public void AddCourseToTheList(Courses course){
        if (this.coursesList == null) {
            this.coursesList = new ArrayList<>();
        }
        this.coursesList.add(course);
    }
    public void AddSubjectToTheList(Subjects subject){
        if (this.subjectsList == null) {
            this.subjectsList = new ArrayList<>();
        }
        this.subjectsList.add(subject);
    }

    public List<Subjects> getSubject() {
        return subjectsList;
    }

    public List<Courses> getCourse() {
        return coursesList;
    }

    public String getUsername() {
        return super.getUsername();
    }
}