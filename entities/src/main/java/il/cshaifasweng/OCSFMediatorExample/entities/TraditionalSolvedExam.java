package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.File;

@Entity
@Table(name = "Traditionalexamstable")
public class TraditionalSolvedExam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    private File exam;

    private String studentName;

    private String completionTime;

    @ManyToOne
    @JoinColumn(name = "Ready_Exam_Id")
    private ReadyExams readyExam;

    public TraditionalSolvedExam(File exam, String studentName, String completionTime, ReadyExams readyExam) {
        this.exam = exam;
        this.studentName = studentName;
        this.completionTime = completionTime;
        this.readyExam = readyExam;
    }

    public TraditionalSolvedExam() {
        
    }

    public int getId() {
        return id;
    }

    public File getExam() {
        return exam;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getCompletionTime() {
        return completionTime;
    }

    public ReadyExams getReadyExam() {
        return readyExam;
    }
}
