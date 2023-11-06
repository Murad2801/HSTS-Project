package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "studentstable")
public class Students implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int id;

    private String studentName;

    private double grade1,grade2,grade3,grade4,grade5;

    public Students(String studentName, double grade1, double grade2, double grade3, double grade4, double grade5) {
        this.studentName = studentName;
        this.grade1 = grade1;
        this.grade2 = grade2;
        this.grade3 = grade3;
        this.grade4 = grade4;
        this.grade5 = grade5;
    }
    public Students(){}
    public String getName(){
        return this.studentName;
    }
    public List<String> getAllGrades() {
        List<String> grades =new ArrayList<>();

        String str =("Grade1:  " + String.valueOf(this.grade1)) ;
        grades.add(str);
        str =("Grade2:  " + String.valueOf(this.grade2)) ;
        grades.add(str);
        str =("Grade3:  " + String.valueOf(this.grade3)) ;
        grades.add(str);
        str =("Grade4:  " + String.valueOf(this.grade4)) ;
        grades.add(str);
        str =("Grade5:  " + String.valueOf(this.grade5)) ;
        grades.add(str);

        return grades;
    }

}