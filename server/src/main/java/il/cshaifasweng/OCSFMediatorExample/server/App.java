package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.io.File;
import java.util.*;


/**
 * Hello world!
 */
public class App {
    static List<String> OnlineUsers = new ArrayList<>();
    private static SessionFactory sessionFactory; // Injected or obtained through HibernateUtil

    private static Session session;
    private static SimpleServer server;


    public static List<String> getSubjectNames(String name) {

        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        try {
            Query<TeachersUser> userQuery = session.createQuery("FROM TeachersUser WHERE username = :name", TeachersUser.class);
            userQuery.setParameter("name", name);
            TeachersUser teachersUser = userQuery.uniqueResult();

            List<Subjects> subjectsList = teachersUser.getSubject();
            List<String> subjectNames = new ArrayList<>();

            for (Subjects subject : subjectsList) {
                subjectNames.add(subject.getSubjectName());
            }

            session.getTransaction().commit();
            session.close();

            System.out.println(subjectNames); // Print the subject names for debugging

            return subjectNames;
        } catch (Exception e) {
            // Handle exceptions and print the error details
            System.out.println("Exception thrown in getSubjectNames: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


    public static List<Questions> getQuestionList(String courseName) {
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            Query<Courses> query = session.createQuery("SELECT c FROM Courses c WHERE c.CourseName = :course", Courses.class);
            query.setParameter("course", courseName);

            Courses cour = query.uniqueResult();

            List<Questions> QuestionList = cour.getQuestionList();
            System.out.println("from course-" + courseName + " got qlist: " + QuestionList);
            session.getTransaction().commit();
            session.close();
            if (QuestionList == null || QuestionList.isEmpty()) {
                return null;
            } else {
                return QuestionList;
            }
        } catch (Exception e) {
            // Handle exceptions
            System.out.println("Exception thrown in getQuestionList" + e.getMessage());
            return null;
        }
    }

    public static List<String> getCoursesNames(List<String> name) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        try {
            String username = name.get(0);
            String subject = name.get(1);
            Query<TeachersUser> userQuery = session.createQuery("FROM TeachersUser WHERE username = :name", TeachersUser.class);
            userQuery.setParameter("name", username);
            TeachersUser teachersUser = userQuery.uniqueResult();


            List<Courses> coursesList = teachersUser.getCourse();
            List<String> courseNames = new ArrayList<>();

            for (Courses course : coursesList) {
                if (Objects.equals(course.getSubject().getSubjectName(), subject)) {
                    courseNames.add(course.getCourseName());
                }
            }

            session.getTransaction().commit();
            session.close();

            System.out.println(courseNames); // Print the course names for debugging

            return courseNames;
        } catch (Exception e) {
            // Handle exceptions and print the error details
            System.out.println("Exception thrown in getCourseNames: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public static List<String> getExamsCode(String user, String course) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        try {
            List<String> codes = new ArrayList<String>();
            Query<SolvedExams> Query = session.createQuery("FROM SolvedExams ", SolvedExams.class);
            List<SolvedExams> results = Query.list();
            for (SolvedExams solvedExam : results) {
                if (solvedExam.getReadyExam().getCourseName().equals(course) && solvedExam.getReadyExam().getPublishingTeacher().equals(user)) {
                    if (solvedExam.getApproved().equals("no")) {
                        if (!codes.contains(solvedExam.getReadyExam().getExamIdentifier()))
                            codes.add(solvedExam.getReadyExam().getExamIdentifier());
                    }
                }
            }
            session.getTransaction().commit();
            return codes;
        } catch (Exception e) {
            // Handle exceptions and print the error details
            System.out.println("Exception thrown in getExamsCode: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


    public static int UpdateGrade(SolvedExams exam, String bonus, String reason, String note) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        int id = exam.getId();
        Query<SolvedExams> query = session.createQuery("FROM SolvedExams where id =:id", SolvedExams.class);
        query.setParameter("id", id);
// Execute the query and get the result
        SolvedExams result = query.uniqueResult();
        int newGrade = Integer.parseInt(bonus);
        try {
                result.setStudentGrade(newGrade);
            result.setUpdateGradeReason(reason);
            result.setApproved("yes");
            result.setNoteForStudent(note);
            result.setShowThisGrade();
            session.flush();
            session.getTransaction().commit();


        } catch (Exception e) {
            // Handle exceptions
            System.out.println("Exception thrown in UpdateGrade");
        }
        return newGrade;
    }

    public static List<Questions> getQuestionListUsingId(int id) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query<Exams> query = session.createQuery("FROM Exams where id =:id", Exams.class);
        query.setParameter("id", id);
// Execute the query and get the result
        Exams result = query.uniqueResult();
        List<Questions> Qlist = result.getQuestionsList();
        ;
        System.out.println(Qlist.get(0));
        session.flush();
        session.getTransaction().commit();
        return Qlist;
    }

    public static List<String> getStudentNames() {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        try {
            List<String> studentNames = new ArrayList<>();
            Query<Students> query = session.createQuery("SELECT s FROM Students s", Students.class);
            if (query.list() != null) {
                for (Students student : query.list()) {
                    studentNames.add(student.getName());
                }
            } else {
                studentNames.add("-1");
            }
            session.flush();
            session.getTransaction().commit();
            session.close();
            return studentNames;
        } catch (Exception e) {
            // Handle exceptions
            System.out.println("Exception thrown in getStudents");
            return null;

        }
    }

    public static List<Users> getStudentUsernames() {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        try {
            Query<StudentsUser> query = session.createQuery("SELECT s FROM StudentsUser s", StudentsUser.class);
            List<Users> studentNames = new ArrayList<>(query.list());

            session.flush();
            session.getTransaction().commit();
            session.close();

            return studentNames;
        } catch (Exception e) {
            // Handle exceptions
            System.out.println("Exception thrown in getStudents");
            return null;

        }
    }

    public static List<Users> getStudentGrades() {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        try {
            Query<StudentsUser> query = session.createQuery("SELECT s FROM StudentsUser s", StudentsUser.class);
            List<Users> studentNames = new ArrayList<>(query.list());

            session.flush();
            session.getTransaction().commit();
            session.close();
            return studentNames;
        } catch (Exception e) {
            // Handle exceptions
            System.out.println("Exception thrown in getStudents");
            return null;

        }
    }


    public static List<String> getStudentGrades(String name) {
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            String fullname;

            Query<Users> newQuery = session.createQuery("SELECT u FROM Users u WHERE u.username = :username", Users.class);
            newQuery.setParameter("username", name);
            Users user = newQuery.uniqueResult();
            fullname = user.getFullname();

            List<String> grades = new ArrayList<>();
            Query<Students> query = session.createQuery("SELECT s FROM Students s WHERE s.studentName = :fullname", Students.class);
            query.setParameter("fullname", fullname);
            List<Students> students = query.getResultList();
            if (students != null && !students.isEmpty()) {
                Students student = students.get(0);
                grades = student.getAllGrades();
            } else {
                grades.add("-1");
            }
            grades.add(name);
            session.getTransaction().commit();
            session.close();
            return grades;
        } catch (Exception e) {
            // Handle exceptions
            System.out.println("Exception thrown in getStudentGrades: " + e.getMessage());
            return null;
        }
    }

    public static Exams createNewExam(Exams tempexam, String Subject, String Course, String user, List<Questions> questionsList) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        Query<Subjects> query = session.createQuery("SELECT S FROM Subjects S WHERE S.subjectName = :subj", Subjects.class);
        query.setParameter("subj", Subject);
        Subjects sub = (Subjects) query.uniqueResult();

        Query<Courses> query2 = session.createQuery("SELECT c FROM Courses c WHERE c.CourseName = :course", Courses.class);
        query2.setParameter("course", Course);
        Courses cour = (Courses) query2.uniqueResult();

        Exams exam = new Exams(tempexam.getDuration(), tempexam.getNoteForTeacher(), tempexam.getGetNoteForStudent(), user, cour, sub);
        exam.setReadyQuestionsList(tempexam.getReadyQuestionsList());
        for (Questions question : questionsList) {
            System.out.println("-------" + questionsList);
            exam.addQuestions(question);
        }

        session.save(exam);

        exam.CreateIdentifier(cour, sub);

        session.getTransaction().commit();
        session.close();
        return exam;
    }

    public static List<Exams> getExamsByCourseName(String courseName) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        try {
            Query<Courses> courseQuery = session.createQuery("FROM Courses WHERE CourseName = :courseName", Courses.class);
            courseQuery.setParameter("courseName", courseName);
            Courses course = courseQuery.uniqueResult();

            List<Exams> exams = course.getExamsList();

            for (Exams exam : exams) {
                System.out.print(exam.getExamIdentifier());
                System.out.print(exam.getDuration());
                System.out.print(exam.getReadyQuestionsList());
                System.out.print(exam.getQuestionsList());
                System.out.print(exam.getNoteForTeacher());
                System.out.print(exam.getNoteForStudent());
                System.out.print(exam.getTeacherName());
                System.out.print(exam.getExamcode());
                System.out.print(exam.getCourse());
                System.out.println(exam.getSubject());
            }
            session.getTransaction().commit();
            session.close();

            return exams;
        } catch (Exception e) {
            // Handle exceptions and print the error details
            System.out.println("Exception thrown in getExamsByCourseName: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


    public static void createNewQuestion(List<String> list) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        Query<Subjects> query = session.createQuery("SELECT S FROM Subjects S WHERE S.subjectName = :subj", Subjects.class);
        query.setParameter("subj", list.get(6));
        Subjects sub = (Subjects) query.uniqueResult();

        Query<Courses> query2 = session.createQuery("SELECT c FROM Courses c WHERE c.CourseName = :course", Courses.class);

        query2.setParameter("course", list.get(7));
        Courses cour = (Courses) query2.uniqueResult();
        Questions ques = new Questions(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4), list.get(5), sub, cour);
        session.save(ques);
        ques.CreateIdentifier(cour);
        session.getTransaction().commit();
        session.close();

    }

    public static void changeStudentGrade(String name, double newGrade, String chosenGrade) { // we need to check usages of this func, is it okay to have 2 parameters? ( simpleServer stuff )
        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session = sessionFactory.openSession();
            session.beginTransaction();

            Query query = session.createQuery("UPDATE Students SET " + chosenGrade + " = :newGrade WHERE studentName = :name");


            query.setParameter("newGrade", newGrade); // please check query! i just added chosenGrade
            query.setParameter("name", name);
            int rowsAffected = query.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Grade updated successfully for student: " + name);
            } else {
                System.out.println("No student found with the name: " + name);
            }

            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            // Handle exceptions
            System.out.println("Exception thrown in changeStudentGrades: " + e.getMessage());
        }
    }

    public static void getAvailableExams(String username) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();


    }

    public static String validateUser(String username, String password) {

        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        Query query = session.createQuery("FROM Users WHERE username = :username AND password = :password");
        query.setParameter("username", username);
        query.setParameter("password", password);


        Users user = (Users) query.uniqueResult();
        session.close();
        if (user != null) {
            // User exists
            String role = user.getRole();
            return role;
        } else {
            return null;
            // User doesn't exist

        }
    }


    public static String validateId(String username, int id, String StudentId) {
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            Query<StudentsUser> userQuery = session.createQuery("FROM StudentsUser WHERE username = :name", StudentsUser.class);
            userQuery.setParameter("name", username);
            StudentsUser studentsUser = userQuery.uniqueResult();

            Query<ReadyExams> query = session.createQuery("FROM ReadyExams WHERE id =:Id", ReadyExams.class);
            query.setParameter("Id", id);
            ReadyExams Rexam = query.uniqueResult();
            if (Rexam.getExamType().equals("Digital Exam")) {
                if (studentsUser != null && StudentId.equals(studentsUser.getStudentId())) {
                    // User exists
                    String Id = studentsUser.getStudentId();
                    Rexam.setStartedExam();
                    session.getTransaction().commit();
                    return Id;
                } else {
                    session.getTransaction().rollback();
                    return null;
                    // User doesn't exist
                }
            } else {
                String Id = studentsUser.getStudentId();
                Rexam.setStartedExam();
                session.getTransaction().commit();
                return Id;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void LogMeOut(String username) {
        if (OnlineUsers.contains(username)) {
            OnlineUsers.remove(username);
        }
    }

    public static String CheckOnline(String username) {
        if (OnlineUsers.contains(username)) {
            return username;
        } else {
            OnlineUsers.add(username);
            return null;
        }
    }

    public static void generate() throws Exception {

        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();


        Subjects sub1 = new Subjects("Math");
        session.save(sub1);
        sub1.setSubjectCode();

        Subjects sub2 = new Subjects("Computer Science");
        session.save(sub2);
        sub2.setSubjectCode();

        Courses cour1 = new Courses("Object Oriented Programming", sub2);
        Courses cour2 = new Courses("Operating System ", sub2);
        Courses cour3 = new Courses("Hardware", sub2);
        Courses cour4 = new Courses("Data Structures", sub2);
        Courses cour5 = new Courses("Algebra 1", sub1);
        Courses cour6 = new Courses("Algebra 2", sub1);
        Courses cour7 = new Courses("Discrete Math", sub1);
        Courses cour8 = new Courses("Probability", sub1);

        session.save(cour1);
        cour1.setCourseCode();
        session.save(cour2);
        cour2.setCourseCode();
        session.save(cour3);
        cour3.setCourseCode();
        session.save(cour4);
        cour4.setCourseCode();
        session.save(cour5);
        cour5.setCourseCode();
        session.save(cour6);
        cour6.setCourseCode();
        session.save(cour7);
        cour7.setCourseCode();
        session.save(cour8);
        cour8.setCourseCode();


        TeachersUser teachersUser1 = new TeachersUser("murad", "murad123", "teacher", "Murad Khoury");
        teachersUser1.AddCourseToTheList(cour1);
        teachersUser1.AddCourseToTheList(cour4);
        teachersUser1.AddSubjectToTheList(sub2);

        session.save(teachersUser1);


        TeachersUser teachersUser2 = new TeachersUser("lana", "lana123", "teacher", "Lana Shehab");
        teachersUser2.AddCourseToTheList(cour5);
        teachersUser2.AddCourseToTheList(cour6);
        teachersUser2.AddCourseToTheList(cour1);
        teachersUser2.AddSubjectToTheList(sub1);
        teachersUser2.AddSubjectToTheList(sub2);
        session.save(teachersUser2);


        StudentsUser studentsUser1 = new StudentsUser("ghassan", "ghassan123", "student", "Ghassan Gharzuzy", "000000001");
        studentsUser1.AddCourseToTheList(cour1);
        studentsUser1.AddCourseToTheList(cour4);
        studentsUser1.AddCourseToTheList(cour5);
        studentsUser1.AddSubjectToTheList(sub2);
        studentsUser1.AddSubjectToTheList(sub1);

        session.save(studentsUser1);

        StudentsUser studentsUser2 = new StudentsUser("lillian", "lillian123", "student", "Lillian Mansour", "000000002");
        studentsUser2.AddCourseToTheList(cour1);
        studentsUser2.AddCourseToTheList(cour4);
        studentsUser2.AddCourseToTheList(cour5);
        studentsUser2.AddSubjectToTheList(sub2);
        studentsUser2.AddSubjectToTheList(sub1);

        session.save(studentsUser2);


        ManagerUser managerUser1 = new ManagerUser("lawrence", "lawrence123", "manager", "Lawrence Khoury");
        ManagerUser managerUser2 = new ManagerUser("law", "law123", "manager", "Lawrence Khoury");
        session.save(managerUser1);
        session.save(managerUser2);


        Questions question1 = new Questions("What is a vector?", "A quantity that has both magnitude and direction.", "A quantity that only has magnitude.", "A quantity that only has direction.", "A quantity that has neither magnitude nor direction.", "A quantity that has both magnitude and direction.", sub1, cour5);
        Questions question2 = new Questions("Solve the equation 2x + 5 = 15 for x.", "x = 5", "x = 6", "x = 7", "x = 8", "x = 5", sub1, cour5);
        Questions question3 = new Questions("Simplify the expression: 3(x + 2) - 2(x - 4).", "5x + 2", "5x - 2", "5x + 8", "5x - 8", "5x + 2", sub1, cour5);
        Questions question4 = new Questions("Expand the expression: (x + 3)(x - 2).", "x^2 + x - 6", "x^2 - x + 6", "x^2 - x - 6", "x^2 + x + 6", "x^2 + x - 6", sub1, cour5);
        Questions question5 = new Questions("Solve the quadratic equation x^2 - 4x + 3 = 0 for x.", "x = 1, x = 3", "x = -1, x = -3", "x = 1, x = -3", "x = -1, x = 3", "x = 1, x = 3", sub1, cour5);
        Questions question6 = new Questions("Find the slope of the line passing through the points (2, 3) and (4, 7).", "2", "4", "6", "8", "2", sub1, cour5);
        Questions question7 = new Questions("Simplify the expression: (2x^2 - 3x + 5) + (4x^2 + 2x - 1).", "6x^2 - x + 4", "6x^2 - x + 6", "6x^2 + x + 6", "6x^2 + x + 4", "6x^2 - x + 4", sub1, cour5);
        Questions question8 = new Questions("Factorize the expression: x^2 - 9.", "(x + 3)(x - 3)", "(x + 3)(x + 3)", "(x - 3)(x - 3)", "(x - 3)(x + 3)", "(x + 3)(x - 3)", sub1, cour5);
        Questions question9 = new Questions("Solve the inequality 2x - 5 < 7 for x.", "x < 6", "x > 6", "x < -1", "x > -1", "x < 6", sub1, cour5);
        Questions question10 = new Questions("Evaluate the expression if x = 3: 2x^2 - 5x + 4.", "13", "19", "21", "23", "13", sub1, cour5);


        session.save(question1);
        session.save(question2);
        session.save(question3);
        session.save(question4);
        session.save(question5);
        session.save(question6);
        session.save(question7);
        session.save(question8);
        session.save(question9);
        session.save(question10);


        question1.CreateIdentifier(cour5);
        question2.CreateIdentifier(cour5);
        question3.CreateIdentifier(cour5);
        question4.CreateIdentifier(cour5);
        question5.CreateIdentifier(cour5);
        question6.CreateIdentifier(cour5);
        question7.CreateIdentifier(cour5);
        question8.CreateIdentifier(cour5);
        question9.CreateIdentifier(cour5);
        question10.CreateIdentifier(cour5);


        Questions question01 = new Questions("What is encapsulation in object-oriented programming?", "A process of hiding internal implementation details and providing access only through defined interfaces.", "A process of making objects communicate with each other.", "A process of creating objects from classes.", "A process of reusing code from existing classes.", "A process of hiding internal implementation details and providing access only through defined interfaces.", sub2, cour1);
        Questions question02 = new Questions("What is inheritance in object-oriented programming?", "A mechanism that allows a class to inherit properties and behaviors from another class.", "A mechanism that allows a class to have multiple instances.", "A mechanism that allows a class to contain objects of other classes.", "A mechanism that allows a class to be divided into smaller parts.", "A mechanism that allows a class to inherit properties and behaviors from another class.", sub2, cour1);
        Questions question03 = new Questions("What is polymorphism in object-oriented programming?", "The process of creating multiple objects from a single class.", "The process of breaking a complex problem into smaller parts.", "The ability of an object to take on many forms.", "The process of creating objects based on existing classes.", "The ability of an object to take on many forms.", sub2, cour1);
        Questions question04 = new Questions("What is a constructor in Java?", "A keyword used to define variables.", "A special method that is used to initialize objects.", "A type of loop used to iterate over collections.", "A keyword used to control the flow of a program.", "A special method that is used to initialize objects.", sub2, cour1);
        Questions question05 = new Questions("What is the difference between an abstract class and an interface?", "An abstract class is used for single inheritance, while an interface is used for multiple inheritance.", "An abstract class can be instantiated, while an interface cannot.", "An abstract class can inherit from multiple classes, while an interface cannot.", "An abstract class can have method implementations, while an interface only defines method signatures.", "An abstract class can have method implementations, while an interface only defines method signatures.", sub2, cour1);

        session.save(question01);
        session.save(question02);
        session.save(question03);
        session.save(question04);
        session.save(question05);

        question01.CreateIdentifier(cour1);
        question02.CreateIdentifier(cour1);
        question03.CreateIdentifier(cour1);
        question04.CreateIdentifier(cour1);
        question05.CreateIdentifier(cour1);


//        ReadyQuestions readyQuestion1 = new ReadyQuestions(question1.getQuestionCode(), 10, "1");
//        ReadyQuestions readyQuestion2 = new ReadyQuestions(question2.getQuestionCode(), 10, "2");
//        ReadyQuestions readyQuestion3 = new ReadyQuestions(question3.getQuestionCode(), 10, "3");
//        ReadyQuestions readyQuestion4 = new ReadyQuestions(question4.getQuestionCode(), 10, "1");
//        ReadyQuestions readyQuestion5 = new ReadyQuestions(question5.getQuestionCode(), 10, "1");
//        ReadyQuestions readyQuestion6 = new ReadyQuestions(question6.getQuestionCode(), 10, "1");
//        ReadyQuestions readyQuestion7 = new ReadyQuestions(question7.getQuestionCode(), 10, "1");
//        ReadyQuestions readyQuestion8 = new ReadyQuestions(question8.getQuestionCode(), 10, "1");
//        ReadyQuestions readyQuestion9 = new ReadyQuestions(question9.getQuestionCode(), 10, "2");
//        ReadyQuestions readyQuestion10 = new ReadyQuestions(question10.getQuestionCode(), 10, "3");
//
//        Exams exam1 = new Exams(10, "High Level Exam", "Good Luck Ram", "lana", cour5, sub1);
//        Exams exam2 = new Exams(10, "High Level Exam", "Good Luck Ram", "murad", cour5, sub1);
//
//
//        exam1.addQuestions(question1);
//        exam1.addQuestions(question2);
//        exam1.addQuestions(question3);
//        exam1.addQuestions(question4);
//        exam1.addQuestions(question5);
//        exam1.addQuestions(question6);
//        exam1.addQuestions(question7);
//        exam1.addQuestions(question8);
//        exam1.addQuestions(question9);
//        exam1.addQuestions(question10);
//
//        exam2.addQuestions(question1);
//        exam2.addQuestions(question2);
//        exam2.addQuestions(question3);
//        exam2.addQuestions(question4);
//        exam2.addQuestions(question5);
//        exam2.addQuestions(question6);
//        exam2.addQuestions(question7);
//        exam2.addQuestions(question8);
//        exam2.addQuestions(question9);
//        exam2.addQuestions(question10);
//
//        session.save(readyQuestion1);
//        session.save(readyQuestion2);
//        session.save(readyQuestion3);
//        session.save(readyQuestion4);
//        session.save(readyQuestion5);
//        session.save(readyQuestion6);
//        session.save(readyQuestion7);
//        session.save(readyQuestion8);
//        session.save(readyQuestion9);
//        session.save(readyQuestion10);
//        session.save(exam1);
//        session.save(exam2);
//
//        exam1.CreateIdentifier(cour5, sub1);
//        exam2.CreateIdentifier(cour5, sub1);
//
//
//        List<Answer> exam1Answers = new ArrayList<>();
//        Answer answer1 = new Answer(1, "1");
//        Answer answer2 = new Answer(2, "2");
//        Answer answer3 = new Answer(3, "3");
//        Answer answer4 = new Answer(4, "4");
//        Answer answer5 = new Answer(5, "1");
//        Answer answer6 = new Answer(6, "2");
//        Answer answer7 = new Answer(7, "3");
//        Answer answer8 = new Answer(8, "4");
//        Answer answer9 = new Answer(9, "1");
//        Answer answer10 = new Answer(10, "2");
//        exam1Answers.add(answer1);
//        exam1Answers.add(answer2);
//        exam1Answers.add(answer3);
//        exam1Answers.add(answer4);
//        exam1Answers.add(answer5);
//        exam1Answers.add(answer6);
//        exam1Answers.add(answer7);
//        exam1Answers.add(answer8);
//        exam1Answers.add(answer9);
//        exam1Answers.add(answer10);
//
//        List<Answer> exam2Answers = new ArrayList<>();
//        exam2Answers.add(answer1);
//        exam2Answers.add(answer2);
//        exam2Answers.add(answer3);
//        exam2Answers.add(answer4);
//        exam2Answers.add(answer5);
//        exam2Answers.add(answer6);
//        exam2Answers.add(answer7);
//        exam2Answers.add(answer8);
//        exam2Answers.add(answer9);
//        exam2Answers.add(answer10);

//        ReadyExams readyExams1 = new ReadyExams(exam1,"l4n4","digital","lana");
//        ReadyExams readyExams2 = new ReadyExams(exam2,"l4n4","digital","murad");
//        session.save(readyExams1);
//        session.save(readyExams2);
//
//        SolvedExams solvedExam1 = new SolvedExams(readyExams1, "2023-07-16 12:30:00", exam1Answers, "3", 100, 90, "ghassan");
//        SolvedExams solvedExam2 = new SolvedExams(readyExams2, "2023-07-16 12:30:00", exam1Answers, "3", 100, 80, "ghassan");
//        SolvedExams solvedExam3 = new SolvedExams(readyExams1, "2023-07-16 12:30:00", exam1Answers, "3", 100, 60, "ghassan");
//        SolvedExams solvedExam4 = new SolvedExams(readyExams1, "2023-07-16 12:30:00", exam1Answers, "4", 100, 100, "lillian");
//        SolvedExams solvedExam5 = new SolvedExams(readyExams2, "2023-07-16 12:30:00", exam1Answers, "4", 100, 95, "lillian");
//        SolvedExams solvedExam6 = new SolvedExams(readyExams1, "2023-07-16 12:30:00", exam1Answers, "4", 100, 79, "lillian");
//
//        session.save(solvedExam1);
//        session.save(solvedExam2);
//        session.save(solvedExam3);
//        session.save(solvedExam4);
//        session.save(solvedExam5);
//        session.save(solvedExam6);

        session.flush();

        session.getTransaction().commit();
    }


    public static void establishConnection() {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.getTransaction().commit();

    }

    public static void main(String[] args) throws Exception {
        server = new SimpleServer(3006);
        establishConnection();

//        generate();

        System.out.println("server is listening");

        server.listen();
    }

    public static List<Courses> getStudentCourses(String userName) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query<StudentsUser> userQuery = session.createQuery("FROM StudentsUser WHERE username = :name", StudentsUser.class);
        userQuery.setParameter("name", userName);
        StudentsUser studentsUser = userQuery.uniqueResult();
        List<Courses> courseList = studentsUser.getCoursesList();
        for (Courses course : courseList) {
            System.out.print(course.getCourseName());
        }
        session.getTransaction().commit();
        return courseList;
    }

    public static List<Courses> getTeacherCourses(String userName) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query<TeachersUser> userQuery = session.createQuery("FROM TeachersUser  WHERE username = :name", TeachersUser.class);
        userQuery.setParameter("name", userName);
        TeachersUser teachersUser = userQuery.uniqueResult();
        List<Courses> courseList = teachersUser.getCourse();
        for (Courses course : courseList) {
            System.out.print(course.getCourseName());
        }
        session.getTransaction().commit();
        return courseList;
    }

    public static List<ReadyExams> getReadyExams(List<Courses> courseList) {
        List<ReadyExams> returnList = new ArrayList<>();
        for (Courses course : courseList) {
            String courseName = course.getCourseName();
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            Query<ReadyExams> userQuery = session.createQuery("FROM ReadyExams WHERE courseName = :course_name", ReadyExams.class);
            userQuery.setParameter("course_name", courseName);
            List<ReadyExams> exams = userQuery.getResultList();
            returnList.addAll(exams);
            Exams exam = null;
            for (ReadyExams ready : returnList) {
                exam = ready.getExam();
                System.out.print(ready.getSolvedExams());
                System.out.print(ready.getId() + ", ");
                if (exam != null) {
                    System.out.print(exam.getExamIdentifier());
                    System.out.print(exam.getDuration());
                    System.out.print(exam.getReadyQuestionsList());
                    System.out.print(exam.getQuestionsList());
                    System.out.print(exam.getNoteForTeacher());
                    System.out.print(exam.getNoteForStudent());
                    System.out.print(exam.getTeacherName());
                    System.out.print(exam.getExamcode());
                    System.out.print(exam.getCourse());
                    System.out.println(exam.getSubject());
                }
            }


            session.getTransaction().commit();
        }

        return returnList;
    }

    public static List<ReadyExams> getReadyExamsStudents(String studentName) {

        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query<StudentsUser> userQuery = session.createQuery("FROM StudentsUser WHERE username = :name", StudentsUser.class);
        userQuery.setParameter("name", studentName);
        StudentsUser student = userQuery.uniqueResult();
        Query<ReadyExams> examsQuery = session.createQuery("FROM ReadyExams WHERE :student MEMBER OF students", ReadyExams.class);
        examsQuery.setParameter("student", student);
        List<ReadyExams> returnList = examsQuery.getResultList();
        Exams exam = null;
        for (ReadyExams ready : returnList) {
            exam = ready.getExam();
            for (StudentsUser students : ready.getStudentsList()) {
                System.out.print(students.getUsername());
                System.out.println(students.getId());
            }
            System.out.println(ready.getStudentsList());
            System.out.println(ready.getRequestList());
            System.out.print(ready.getSolvedExams());
            System.out.print(ready.getId() + ", ");
            if (exam != null) {
                System.out.print(exam.getExamIdentifier());
                System.out.print(exam.getDuration());
                System.out.print(exam.getReadyQuestionsList());
                System.out.print(exam.getQuestionsList());
                System.out.print(exam.getNoteForTeacher());
                System.out.print(exam.getNoteForStudent());
                System.out.print(exam.getTeacherName());
                System.out.print(exam.getExamcode());
                System.out.print(exam.getCourse());
                System.out.println(exam.getSubject());
                System.out.println(exam.getReadyExamsList());

            }
        }
        session.getTransaction().commit();
        return returnList;
    }

    public static ReadyExams UpdateExam(int readyid) {
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            Query<ReadyExams> query = session.createQuery("FROM ReadyExams WHERE id =: ID ", ReadyExams.class);
            query.setParameter("ID", readyid);
            ReadyExams ready = query.uniqueResult();

            Exams exam = ready.getExam();
            for (StudentsUser students : ready.getStudentsList()) {
                System.out.print(students.getUsername());
                System.out.println(students.getId());
            }
            System.out.println(ready.getStudentsList());
            System.out.println(ready.getRequestList());
            System.out.print(ready.getSolvedExams());
            System.out.print(ready.getId() + ", ");
            if (exam != null) {
                System.out.print(exam.getExamIdentifier());
                System.out.print(exam.getDuration());
                System.out.print(exam.getReadyQuestionsList());
                System.out.print(exam.getQuestionsList());
                System.out.print(exam.getNoteForTeacher());
                System.out.print(exam.getNoteForStudent());
                System.out.print(exam.getTeacherName());
                System.out.print(exam.getExamcode());
                System.out.print(exam.getCourse());
                System.out.println(exam.getSubject());
                System.out.println(exam.getReadyExamsList());

            }

            session.getTransaction().commit();
            return ready;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
//    public static void SaveReadyExam(Exams exam, String type, String passCode, String name) {
//
//        session = HibernateUtil.getSessionFactory().getCurrentSession();
//        session.beginTransaction();
//        ReadyExams readyExam = new ReadyExams(exam, passCode, type, name);
//        session.save(readyExam);
//        session.getTransaction().commit();
//        session.close();
//
//    }

    public static void SaveReadyExam(Exams exam, String type, String passCode, String name, String courseName) {

        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query<Courses> query1 = session.createQuery("FROM Courses WHERE CourseName = :name", Courses.class);
        query1.setParameter("name", courseName);
        Courses course = query1.uniqueResult();
        Query<StudentsUser> query2 = session.createQuery("FROM StudentsUser WHERE :cour MEMBER OF coursesList", StudentsUser.class);
        query2.setParameter("cour", course);
        List<StudentsUser> studentsList = query2.getResultList();
        ReadyExams readyExam = new ReadyExams(exam, passCode, type, name, studentsList);
        session.save(readyExam);
        session.getTransaction().commit();
        session.close();

    }

    public static void UpdateThatStudentSolvedExam(int readyExamId, String studentName) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        Query<StudentsUser> query = session.createQuery("FROM StudentsUser WHERE username = :name", StudentsUser.class);
        query.setParameter("name", studentName);
        StudentsUser student = query.uniqueResult();

        Query<ReadyExams> query2 = session.createQuery("FROM ReadyExams WHERE id = :ID", ReadyExams.class);
        query2.setParameter("ID", readyExamId);
        ReadyExams ready = query2.uniqueResult();


        student.getAvailableExamsList().remove(ready);
        ready.getStudentsList().remove(student);

        session.saveOrUpdate(student);
        session.saveOrUpdate(ready);
        session.getTransaction().commit();

    }


    public static void SaveSolvedExam(ReadyExams readyExam, int remainingTime, HashMap<Integer, String> answers, String StudentId, String UserName) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query<ReadyExams> query = session.createQuery("FROM ReadyExams WHERE id = :ID", ReadyExams.class);
        query.setParameter("ID", readyExam.getId());
        ReadyExams UpToDate_Exam = query.uniqueResult();

        int duration = UpToDate_Exam.getDuration();
        if (UpToDate_Exam.getUpdatedDuration() > 0) {
            duration = UpToDate_Exam.getUpdatedDuration();
        }
        int time = ((duration * 60 - remainingTime));
        System.out.println(UpToDate_Exam.getUpdatedDuration());
        System.out.println(remainingTime);

        session.getTransaction().commit();

        int minutes = time / 60;
        int seconds = time % 60;
        String completionTime = String.format("%02d:%02d", minutes, seconds);
        int sumGrade = 0;
        int studentGrade = 0;
        Exams exam = readyExam.getExam();
        List<ReadyQuestions> readyQuesList = exam.getReadyQuestionsList();

        for (int i = 0; i < readyQuesList.size(); i++) {
            ReadyQuestions question = readyQuesList.get(i);
            String expectedAnswer = question.getRightAnswer();
            String studentAnswer = answers.get(i);
            sumGrade += question.getGrade();

            if ((expectedAnswer.equals(studentAnswer))) {
                studentGrade += question.getGrade();
            }
        }


        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<Answer> answerList = new ArrayList<>();
        Query<ReadyExams> query1 = session.createQuery("FROM ReadyExams WHERE id = :ID", ReadyExams.class);
        query1.setParameter("ID", readyExam.getId());
        ReadyExams Updated_Exam = query1.uniqueResult();

        if (remainingTime == 0) {
            Updated_Exam.setAutomaticSubmission();
        } else {
            Updated_Exam.setFinishedBeforeTime();
        }
        for (HashMap.Entry<Integer, String> entry : answers.entrySet()) {
            Integer questionNumber = entry.getKey();
            String chosenAnswer = entry.getValue();

            Answer answer = new Answer(questionNumber, chosenAnswer);
            session.save(answer);
            answerList.add(answer);
        }
        SolvedExams solvedExam = new SolvedExams(readyExam, completionTime, answerList, StudentId, sumGrade, studentGrade, UserName);// add the final grade to the constructor
        session.save(solvedExam);
        solvedExam.setShowThisGrade();
        session.getTransaction().commit();
        session.close();

    }

    public static List<SolvedExams> getSolvedExams(String user) {

        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        try {
            Query<SolvedExams> query = session.createQuery("SELECT s FROM SolvedExams s WHERE s.StudentName = :username AND s.approved = :yes", SolvedExams.class);
            query.setParameter("username", user);
            query.setParameter("yes", "yes");

            List<SolvedExams> solvedExams = new ArrayList<>(query.list());

            for (SolvedExams solvedExam : solvedExams) {
                System.out.println(solvedExam.getAnswerList());
                System.out.println(solvedExam.getReadyExam().getExam().getReadyQuestionsList());
            }
            session.getTransaction().commit();

            return solvedExams;

        } catch (Exception e) {
            // Handle exceptions
            System.out.println("Exception thrown in get SolvedExams");
            return null;

        }
    }

    public static List<SolvedExams> getStudentsInfo(String code, String username) {
        List<SolvedExams> finalist = new ArrayList<>();
        List<ReadyExams> templist = new ArrayList<>();
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query<ReadyExams> query = session.createQuery("FROM ReadyExams WHERE examIdentifier = :examcode", ReadyExams.class);
        query.setParameter("examcode", code);
        List<ReadyExams> exams = query.getResultList();
        int i;
        //get the readyExams for the specific teacher
        for (i = 0; i < exams.size(); i++) {
            if (exams.get(i).getPublishingTeacher().equals(username)) {
                templist.add(exams.get(i));
            }
        }

        int j;
        for (i = 0; i < templist.size(); i++) {
            if (!templist.get(i).getSolvedExams().isEmpty()) {
                int size = templist.get(i).getSolvedExams().size();
                finalist.addAll(templist.get(i).getSolvedExams());

            }

        }
        //dont delete it
        for (i = 0; i < finalist.size(); i++) {
            System.out.println(finalist.get(i).getAnswerList());
            System.out.println(finalist.get(i).getReadyExam().getExam().getReadyQuestionsList());
        }

        session.getTransaction().commit();
        session.close();
        return finalist;
    }

    public static List<String> getSubjectsForManager() {
        List<String> subjectNames = new ArrayList<>();

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        Query<String> query = session.createQuery("SELECT S.subjectName FROM Subjects S", String.class);
        List<String> results = query.getResultList();

        subjectNames.addAll(results);

        session.getTransaction().commit();
        session.close();

        return subjectNames;
    }

    public static List<String> getCoursesForManager(String subjectName) {
        List<String> courseNames = new ArrayList<>();

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        // Retrieve the subject ID based on the subject name
        Query<Integer> subjectIdQuery = session.createQuery("SELECT S.id FROM Subjects S WHERE S.subjectName = :subjectName", Integer.class);
        subjectIdQuery.setParameter("subjectName", subjectName);
        Integer subjectId = subjectIdQuery.uniqueResult();

        if (subjectId != null) {
            // Retrieve course names based on the subject ID
            Query<String> courseNamesQuery = session.createQuery("SELECT C.CourseName FROM Courses C WHERE C.subject.id = :subjectId", String.class);
            courseNamesQuery.setParameter("subjectId", subjectId);
            List<String> results = courseNamesQuery.getResultList();

            courseNames.addAll(results);
        }

        session.getTransaction().commit();
        session.close();

        return courseNames;
    }

    public static List<Request> getRequestListForManager(List<ReadyExams> readyExamsList) {
        List<Request> returnList = new ArrayList<>();
        for (ReadyExams readyExams : readyExamsList) {
            int readyExamID = readyExams.getId();
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            Query<Request> userQuery = session.createQuery("FROM Request WHERE readyExam.id = :readyExam_id", Request.class);
            userQuery.setParameter("readyExam_id", readyExamID);
            List<Request> requests = userQuery.getResultList();
            returnList.addAll(requests);
            Exams exam = null;
            for (Request request : returnList) {
                exam = request.getReadyExam().getExam();
                System.out.print(request.getId());
                System.out.print(request.getExamId());
                System.out.print(request.getPublishingTeacher());
                System.out.print(request.getRequestText());
                System.out.print(request.getRequestAddedTime());
                System.out.print(request.getCourseName());
                System.out.print(request.getReadyExam());


                if (exam != null) {
                    System.out.print(exam.getExamIdentifier());
                    System.out.print(exam.getDuration());
                    System.out.print(exam.getReadyQuestionsList());
                    System.out.print(exam.getQuestionsList());
                    System.out.print(exam.getNoteForTeacher());
                    System.out.print(exam.getNoteForStudent());
                    System.out.print(exam.getTeacherName());
                    System.out.print(exam.getExamcode());
                    System.out.print(exam.getCourse());
                    System.out.println(exam.getSubject());
                }
            }
            session.getTransaction().commit();
        }
        return returnList;
    }

    public static List<Courses> getAllCourse() {
        List<Courses> allCoursesList = new ArrayList<>();
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query<Courses> userQuery = session.createQuery("FROM Courses", Courses.class);
        List<Courses> courses = userQuery.getResultList();
        allCoursesList.addAll(courses);
        session.getTransaction().commit();
        return allCoursesList;
    }

    public static List<Request> SaveRequests(ReadyExams readyExam, int extraTime, String explanation) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Request newRequest = new Request(readyExam, extraTime, explanation);
        session.save(newRequest);
        session.getTransaction().commit();
        session.close();
        List<Request> requestList = updatedRequestList();
        return requestList;
    }

    public static List<Request> updatedRequestList() {
        List<Request> requestList = new ArrayList<>();
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query<Request> userQuery = session.createQuery("FROM Request ", Request.class);
        List<Request> requests = userQuery.getResultList();
        requestList.addAll(requests);
        session.getTransaction().commit();
        return requestList;
    }

    public static void UpdateExamDuration(Request request, ReadyExams exam, int extraTime) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        int x = exam.getUpdatedDuration();
        if (x > 0) {
            x += extraTime;
        } else {
            x = extraTime + exam.getDuration();
        }
        int examID = exam.getId();
        Query query = session.createQuery("UPDATE ReadyExams SET updatedDuration = :newValue WHERE id = :exam_id");
        query.setParameter("exam_id", examID);
        query.setParameter("newValue", x);
//        query.setParameter("newValue", extraTime + exam.getDuration());

        query.executeUpdate();
        session.getTransaction().commit();

        DeleteRequestFromDB(request);
    }

    public static void DeleteRequestFromDB(Request request) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        int requestId = request.getId();
        Query deleteRequest = session.createQuery("DELETE FROM Request WHERE id = :request_id");
        deleteRequest.setParameter("request_id", requestId);
        deleteRequest.executeUpdate();

        session.getTransaction().commit();
    }


    public static List<Users> getTeachersUsernames() {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        try {
            Query<TeachersUser> query = session.createQuery("SELECT s FROM TeachersUser s", TeachersUser.class);
            List<Users> teacherNames = new ArrayList<>(query.list());

            session.flush();
            session.getTransaction().commit();
            session.close();
            return teacherNames;
        } catch (Exception e) {
            // Handle exceptions
            System.out.println("Exception thrown in getStudents");
            return null;

        }
    }

    public static List<SolvedExams> getManagerSolvedExams(int examIdentifier) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        try {
            Query<ReadyExams> readyExamsQuery = session.createQuery("FROM ReadyExams re WHERE re.id = :identifier", ReadyExams.class);
            readyExamsQuery.setParameter("identifier", examIdentifier);
            ReadyExams readyExams = readyExamsQuery.uniqueResult();

            if (readyExams == null) {
                // If the ReadyExams object with the given ready exam id is not found, return an empty list
                session.getTransaction().commit();
                return Collections.emptyList();
            }

            int readyExamsId = readyExams.getId();

            Query<SolvedExams> solvedExamsQuery = session.createQuery("FROM SolvedExams se WHERE se.readyExam.id  = :readyExamsId AND se.approved = :yes", SolvedExams.class);
            solvedExamsQuery.setParameter("readyExamsId", readyExamsId);
            solvedExamsQuery.setParameter("yes", "yes");

            List<SolvedExams> solvedExamsList = solvedExamsQuery.list();

            session.getTransaction().commit();

            return solvedExamsList;
        } catch (Exception e) {
            // Handle exceptions
            e.printStackTrace();
            return null;
        }
    }

    public static void SaveTraditionalExam(File file, ReadyExams readyExam_old, String name, int solutionTime) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query<ReadyExams> query = session.createQuery("FROM ReadyExams WHERE id=:ID", ReadyExams.class);
        query.setParameter("ID", readyExam_old.getId());
        ReadyExams readyExam = query.uniqueResult();
        int duration = 60 * (readyExam.getDuration());
        if (readyExam.getUpdatedDuration() > 0) {
            duration = readyExam.getUpdatedDuration();
        }

        int timeTaken = duration - solutionTime;
        int minutes = timeTaken / 60;
        int seconds = timeTaken % 60;
        String timeString = String.format("%02d:%02d", minutes, seconds);

        if (solutionTime == 0) {
            readyExam.setAutomaticSubmission();
        } else {
            readyExam.setFinishedBeforeTime();
        }
        TraditionalSolvedExam traditionalSolvedExam = new TraditionalSolvedExam(file, name, timeString, readyExam);
        session.save(traditionalSolvedExam);
        readyExam.addTraditionalExams(traditionalSolvedExam);
        session.saveOrUpdate(readyExam);
        session.saveOrUpdate(traditionalSolvedExam);
        session.getTransaction().commit();
    }

    public static List<SolvedExams> getSolvedExamsStudent(String studentsUser) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        try {
            Query<SolvedExams> query = session.createQuery("SELECT s FROM SolvedExams s WHERE s.StudentName = :username AND s.approved = :yes", SolvedExams.class);
            query.setParameter("username", studentsUser);
            query.setParameter("yes", "yes");

            List<SolvedExams> solvedExams = new ArrayList<>(query.list());

            session.getTransaction().commit();

            return solvedExams;

        } catch (Exception e) {
            // Handle exceptions
            System.out.println("Exception thrown in get SolvedExams");
            return null;

        }
    }

    public static List<SolvedExams> getSolvedExamsTeacher(String teacherName) {

        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            Query<SolvedExams> query = session.createQuery("SELECT s FROM SolvedExams s WHERE s.readyExam.publishingTeacher = :username AND s.approved = :yes", SolvedExams.class);
            query.setParameter("username", teacherName);
            query.setParameter("yes", "yes");

            List<SolvedExams> solvedExams = new ArrayList<>(query.list());

            session.getTransaction().commit();
            return solvedExams;

        } catch (Exception e) {
            // Handle exceptions
            System.out.println("Exception thrown in get SolvedExams");
            return null;

        }
    }

    public static List<SolvedExams> getSolvedExamsCourse(String course) {

        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        try {

            Query<SolvedExams> query = session.createQuery("SELECT s FROM SolvedExams s WHERE s.readyExam.courseName = :course AND s.approved = :yes", SolvedExams.class);
            query.setParameter("course", course);
            query.setParameter("yes", "yes");

            List<SolvedExams> solvedExams = new ArrayList<>(query.list());

            session.getTransaction().commit();
            return solvedExams;

        } catch (Exception e) {
            // Handle exceptions
            System.out.println("Exception thrown in get SolvedExams");
            return null;
        }
    }

    public static List<ReadyExams> getReadyExamsForCreator(String name) {

        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        try {

            Query<ReadyExams> query = session.createQuery("SELECT r FROM ReadyExams r WHERE r.exam.teacherName = :name OR r.publishingTeacher = :name", ReadyExams.class);
            query.setParameter("name", name);
            List<ReadyExams> readyExams = query.getResultList();
            Exams exam;
            for (ReadyExams ready : readyExams) {
                exam = ready.getExam();
                System.out.println(ready.getStudentsList());
                System.out.println(ready.getSolvedExams());
                System.out.print(ready.getRequestList());
                System.out.print(ready.getId() + ", ");
                if (exam != null) {
                    System.out.print(exam.getExamIdentifier());
                    System.out.print(exam.getDuration());
                    System.out.print(exam.getReadyQuestionsList());
                    System.out.print(exam.getCourse());
                    System.out.print(exam.getQuestionsList());
                    System.out.print(exam.getNoteForTeacher());
                    System.out.print(exam.getNoteForStudent());
                    System.out.print(exam.getTeacherName());
                    System.out.print(exam.getExamcode());
                    System.out.println(exam.getSubject());
                    System.out.println(exam.getReadyExamsList());
                }
            }
            session.getTransaction().commit();
            return readyExams;

        } catch (Exception e) {
            // Handle exceptions
            System.out.println("Exception thrown in get SolvedExams");
            return null;
        }

    }
}

