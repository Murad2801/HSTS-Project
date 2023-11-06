package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;

@Entity
public class ManagerUser extends Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String subject;
    private String course;

    public ManagerUser(String username, String password, String role, String fullname) {
        super(username, password, role, fullname);

    }

    public ManagerUser() {
    }

    public String getSubject() {
        return subject;
    }

    public String getCourse() {
        return course;
    }
}