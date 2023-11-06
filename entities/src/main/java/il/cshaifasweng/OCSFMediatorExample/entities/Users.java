package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "userstable")
public class Users implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;



    private String password;

    private String role;
    private String fullname;

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Users(String username, String password, String role, String fullname){
        this.username=username;
        this.password=password;
        this.role=role;
        this.fullname=fullname;

    }


    public Users() {}
    public String getUsername(){return this.username;}
    public String getPassword(){return this.password;}
    public String getRole(){return this.role;}
}


//    CREATE TABLE users(
//        id INT AUTO_INCREMENT PRIMARY KEY,
//        username VARCHAR(255) UNIQUE,
//    role VARCHAR(255)
//);
//
//        INSERT INTO users(username, role)
//        VALUES
//        ('ghassan', 'student'),
//        ('law', 'student'),
//        ('alec', 'teacher');