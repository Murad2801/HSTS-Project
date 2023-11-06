package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static il.cshaifasweng.OCSFMediatorExample.server.App.*;

public class SimpleServer extends AbstractServer {

    public SimpleServer(int port) {
        super(port);

    }

    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        Message ms = (Message) msg;
        String request = ms.getString();
        String msgString = msg.toString();        // for the warning by shir
        System.out.println("Message received from client " + request);
        if (msgString.startsWith("#warning")) {
            Warning warning = new Warning("Warning from server!");
            try {
                client.sendToClient(warning);
                System.out.format("Sent warning to client %s\n", client.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (request.startsWith("#students_names_request")) {
            List<String> list_of_students = App.getStudentNames();
            Message newMessage = new Message(list_of_students, "#students_names_request");
            try {
                client.sendToClient(newMessage);
                System.out.format("Sent Students' names to client %s\n", client.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (request.startsWith("#3Managers_students_grades_request_for_viewGrades")) {
            System.out.println("111111111");
            String name = (String) (ms.getObject());
            List<SolvedExams> list_of_subjects = App.getSolvedExamsStudent(name);
            Message newMessage = new Message(list_of_subjects, "#Managers_students_grades_request_for_viewGrades");
            try {
                client.sendToClient(newMessage);
                System.out.format("Sent Solved Exams for ViewGrades - Manager , manager %s\n", client.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (request.startsWith("#exam_list_request_for_pool")) {
            String courseName = (String) (ms.getObject());
            List<Exams> list_of_exams = App.getExamsByCourseName(courseName);
            Message newMessage;
            if (request.startsWith(("#exam_list_request_for_pool_manager"))) {
                newMessage = new Message(list_of_exams, "#exam_list_request_for_pool_manager");
            } else {
                newMessage = new Message(list_of_exams, "#exam_list_request_for_pool");
            }
            try {
                client.sendToClient(newMessage);
                System.out.format("Sent exams list to ExamPool %s\n", client.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();

            }

        } else if (request.startsWith("#manager_students_names_request")) {
            List<Users> list_of_students = App.getStudentUsernames(); // this is the one i changed
            Message newMessage = newMessage = new Message(list_of_students, "#manager_students_names_request");
            try {
                client.sendToClient(newMessage);
                System.out.format("Sent Students' names to client, manager %s\n", client.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();

            }
        } else if (request.startsWith("#information_manager_students_names_request")) {
            List<Users> list_of_students = App.getStudentUsernames();
            Message newMessage = new Message(list_of_students, "#information_manager_students_names_request");

            try {
                client.sendToClient(newMessage);
                System.out.format("Sent Students' names to client, manager %s\n", client.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();

            }

        } else if (request.startsWith("#information_manager_student_exams_request")) {
            String studentsUser = (String) ms.getObject();
            List<SolvedExams> list_of_exams = App.getSolvedExamsStudent(studentsUser);


            Message newMessage = new Message(list_of_exams, "#information_manager_student_exams_request");
            try {
                client.sendToClient(newMessage);
                System.out.format("Sent Students' exams to client, manager %s\n", client.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (request.startsWith("#information_manager_teachers_names_request")) {
            List<Users> list_of_teachers = App.getTeachersUsernames();
            Message newMessage = new Message(list_of_teachers, "#information_manager_teachers_names_request");

            try {
                client.sendToClient(newMessage);
                System.out.format("Sent Students' names to client, manager %s\n", client.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();

            }
        } else if (request.startsWith("#information_manager_teacher_exams_request")) {
            String teacherName = (String) ms.getObject();
            List<SolvedExams> list_of_exams = App.getSolvedExamsTeacher(teacherName);
            Message newMessage = new Message(list_of_exams, "#information_manager_teacher_exams_request");
            try {
                client.sendToClient(newMessage);
                System.out.format("Sent Teachers' exams to client, manager %s\n", client.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (request.startsWith("#information_manager_courses_request")) {
            List<Courses> list_of_courses = App.getAllCourse();
            Message newMessage = new Message(list_of_courses, "#information_manager_courses_request");

            try {
                client.sendToClient(newMessage);
                System.out.format("Sent Students' names to client, manager %s\n", client.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (request.startsWith("#information_manager_course_exams_request")) {
            String courseName = (String) ms.getObject();
            List<SolvedExams> list_of_exams = App.getSolvedExamsCourse(courseName);
            Message newMessage = new Message(list_of_exams, "#information_manager_course_exams_request");
            try {
                client.sendToClient(newMessage);
                System.out.format("Sent Teachers' exams to client, manager %s\n", client.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (request.startsWith("#manager_subjects_request")) {
            List<String> list_of_subjects = App.getSubjectsForManager();
            Message newMessage;
            if (request.startsWith("#manager_subjects_request_for_questions")) {
                newMessage = new Message(list_of_subjects, "#manager_subjects_request_for_questions");
            } else {
                newMessage = new Message(list_of_subjects, "#manager_subjects_request");
            }
            try {
                client.sendToClient(newMessage);
                System.out.format("Sent Subjects names to manager %s\n", client.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();

            }
        } else if (request.startsWith("#manager_courses_request")) {
            String subjectName = (String) (ms.getObject());
            List<String> list_of_courses = App.getCoursesForManager(subjectName);
            Message newMessage;
            if (request.startsWith("#manager_courses_request_for_questions")) {
                newMessage = new Message(list_of_courses, "#manager_courses_request_for_questions");
            } else {
                newMessage = new Message(list_of_courses, "#manager_courses_request");
            }
            try {
                client.sendToClient(newMessage);
                System.out.format("Sent Courses names to manager %s\n", client.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();

            }
        } else if (request.startsWith("#question_list_request")) {
            String courseName = (String) (ms.getObject());
            Message newMessage;
            if (request.startsWith("#question_list_request_for_exam")) {
                List<Questions> list_of_question = App.getQuestionList(courseName);
                if (list_of_question == null || list_of_question.isEmpty()) {
                    newMessage = new Message(null, "#question_list_request_for_exam");
                } else {
                    newMessage = new Message(list_of_question, "#question_list_request_for_exam");
                }
            } else if (request.startsWith("#question_list_request_for_createQuest")) {
                List<Questions> list_of_question = App.getQuestionList(courseName);
                if (list_of_question == null || list_of_question.isEmpty()) {
                    newMessage = new Message(null, "#question_list_request_for_createQuest");
                } else {
                    newMessage = new Message(list_of_question, "#question_list_request_for_createQuest");
                }
            } else if (request.startsWith("#question_list_request_for_manager")) {
                List<Questions> list_of_question = App.getQuestionList(courseName);
                if (list_of_question == null || list_of_question.isEmpty()) {
                    newMessage = new Message(null, "#question_list_request_for_manager");
                } else {
                    newMessage = new Message(list_of_question, "#question_list_request_for_manager");
                }
            } else {
                System.out.println("gotten here by mistake simple server");
                //List<String> list_of_question = App.getQuestionList(courseName);
                newMessage = new Message(null, "#question_list_request");

            }
            try {
                client.sendToClient(newMessage);
                System.out.format("Sent Question' List to client %s\n", client.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (request.startsWith("#Subject_names_request")) {
            String name = (String) (ms.getObject());
            List<String> list_of_Subjects = App.getSubjectNames(name);
            Message newMessage;
            if (request.startsWith("#Subject_names_request_for_Checked_exams")) {
                newMessage = new Message(list_of_Subjects, "#Subject_names_request_for_Checked_exams");
            } else if (request.startsWith("#Subject_names_request_for_exam")) {
                newMessage = new Message(list_of_Subjects, "#Subject_names_request_for_exam");
            } else if (request.startsWith("#Subject_names_request_for_manager_questions")) {
                newMessage = new Message(list_of_Subjects, "#Subject_names_request_for_manager");
            } else if (request.startsWith("#Subject_names_request_for_pool_exam")) {
                newMessage = new Message(list_of_Subjects, "#Subject_names_request_for_pool_exam");
            } else {
                newMessage = new Message(list_of_Subjects, "#Subject_names_request");
            }
            try {
                client.sendToClient(newMessage);
                System.out.format("Sent Subjects' names to client %s\n", client.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (request.startsWith("#Courses_names_request")) {
            List<String> names = (List<String>) (ms.getObject()); // takes teacher and sub name
            List<String> list_of_Courses = App.getCoursesNames(names);
            Message newMessage;
            if (request.startsWith("#Courses_names_request_for_Checked_exam")) {
                newMessage = new Message(list_of_Courses, "#Courses_names_request_for_Checked_exams");
            } else if (request.startsWith("#Courses_names_request_for_exam")) {
                newMessage = new Message(list_of_Courses, "#Courses_names_request_for_exam");
            } else if (request.startsWith("#Courses_names_request_for_pool_exam")) {
                newMessage = new Message(list_of_Courses, "#Courses_names_request_for_pool_exam");
            } else if (request.startsWith("#Courses_names_request_for_manager")) {
                newMessage = new Message(list_of_Courses, "#Courses_names_request_for_manager");
            } else {
                newMessage = new Message(list_of_Courses, "#Courses_names_request");
            }
            try {
                client.sendToClient(newMessage);
                System.out.format("Sent Courses' names to client %s\n", client.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();

            }
        } else if (request.startsWith("#student_grades_request")) {
            String name = (String) (ms.getObject());
            List<String> list_of_grades = App.getStudentGrades(name);
            Message newMessage = new Message(null, null);
            if (request.startsWith("#Courses_names_request_for_grade_list")) {
                newMessage = new Message(list_of_grades, "#Courses_names_request_for_grade_list");
            } else {
                newMessage = new Message(list_of_grades, "#student_grades_request");
            }
            try {
                client.sendToClient(newMessage);
                System.out.format("Sent Student Grades to client %s\n", client.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (request.startsWith("#manager_student_grades_request")) {
            String name = (String) (ms.getObject());
            List<String> list_of_grades = App.getStudentGrades(name);
            Message newMessage = new Message(list_of_grades, "#manager_student_grades_request");
            try {
                client.sendToClient(newMessage);
                System.out.format("Sent Student Grades to client, manager %s\n", client.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (request.startsWith("#Add_new_question_request")) {

            List<String> newquestion = (List<String>) ms.getObject();
            App.createNewQuestion(newquestion);

            Message newMessage = new Message(null, "#Add_new_question_request");
            try {
                client.sendToClient(newMessage);
                System.out.format("A new question was created successfully %s\n", client.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (request.startsWith("#add_a_new_exam_request")) {
            List<Object> newList = (List<Object>) ms.getObject();
            Exams exam = (Exams) newList.get(0);
            String course = (String) newList.get(1);
            String subject = (String) newList.get(2);
            String user = (String) newList.get(3);
            List<Questions> questionsList = (List<Questions>) newList.get(4);
            Exams newExam = App.createNewExam(exam, subject, course, user, questionsList);

            Message newMessage = new Message(newExam, "#add_a_new_exam_request");
            try {
                client.sendToClient(newMessage);
                System.out.format("A new Exam was created successfully %s\n", client.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (request.startsWith("#change_grade_request")) {
            Object nameGradeList = (ms.getObject());

            List<Object> nameAndGradeAndChosenGrade = (List<Object>) nameGradeList;

            String name = (String) nameAndGradeAndChosenGrade.get(0); // bringing out the parameters we got from gradeListController..
            String newGrade = (String) nameAndGradeAndChosenGrade.get(1);
            double doubleGrade = Double.parseDouble(newGrade);

            String chosenGrade = (String) nameAndGradeAndChosenGrade.get(2);

            App.changeStudentGrade(name, doubleGrade, chosenGrade); // we must add another parameter ( newGrade )
            List<String> list_of_grades = App.getStudentGrades(name);
            Message newMessage = new Message(list_of_grades, "#change_grade_request");
            try {
                client.sendToClient(newMessage);
                System.out.format("changed Grade successfully and sent Student Grades to client %s\n", client.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (request.startsWith("#validate_user_request")) {
            Object usernameAndPasswordObj = (ms.getObject());

            List<Object> usernameAndPassword = (List<Object>) usernameAndPasswordObj;

            String username = (String) usernameAndPassword.get(0); // bringing out the parameters we got from loginWindowController..
            String password = (String) usernameAndPassword.get(1);

            String role = App.validateUser(username, password);
            if (role == null) {
                Message newMessage = new Message("The username or password is incorrect", "#Error");
                try {
                    client.sendToClient(newMessage);
                    System.out.format("(wrong user or pass)Sent warning to client %s\n", client.getInetAddress().getHostAddress());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // if user found, send good request, if not found, send bad request as the msg string
                List<String> nameAndRole = new ArrayList<>();
                nameAndRole.add(username);
                nameAndRole.add(role);
                Message newMessage = new Message(nameAndRole, "#validate_user_request");
                try {
                    client.sendToClient(newMessage);
                    System.out.format(role + " " + username + "'s username and password are correct sending to client %s\n", client.getInetAddress().getHostAddress());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (request.startsWith("#publish_exam_request")) {
            Object data = ms.getObject();
            List<Object> dataList = (List<Object>) data;
            Exams exam = (Exams) dataList.get(0);
            String type = (String) dataList.get(1);
            String passCode = (String) dataList.get(2);
            String name = (String) dataList.get(3);
            String courseName = (String) dataList.get(4);
            App.SaveReadyExam(exam, type, passCode, name, courseName);
            Message newMessage = new Message(null, "#publish_exam_request");
            System.out.format("Published the Exam successfully !%s\n", client.getInetAddress().getHostAddress());
            try {
                client.sendToClient(newMessage);
                System.out.format("All available exams for the Student sent to client:  %s\n", client.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (request.startsWith("#get_ready_exams_requests")) {
            Object user = (ms.getObject());
            String userName = (String) user;
            if (request.startsWith("#get_ready_exams_requests_student")) {
                List<ReadyExams> readyExams = getReadyExamsStudents(userName);
                Message newMessage = new Message(readyExams, "#get_ready_exams_requests_student");
                try {
                    client.sendToClient(newMessage);
                    System.out.format("All available exams for the Student sent to client:  %s\n", client.getInetAddress().getHostAddress());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (request.startsWith("#get_ready_exams_requests_teacher")) {
                List<Courses> userCourseList = getTeacherCourses(userName);
                List<ReadyExams> readyExams = getReadyExams(userCourseList);
                List<ReadyExams> publishedExams = new ArrayList<>();
                for (ReadyExams readyExam : readyExams) {
                    if (readyExam.getPublishingTeacher().equals(userName)) {
                        publishedExams.add(readyExam);
                    }
                }
                Message newMessage = new Message(publishedExams, "#get_ready_exams_requests_teacher");
                try {
                    client.sendToClient(newMessage);
                    System.out.format("All available exams for the Teacher sent to client:  %s\n", client.getInetAddress().getHostAddress());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (request.startsWith("#Check_if_user_Online")) {
            String data = (String) ms.getObject();
            String check = App.CheckOnline(data);
            Message newMessage = new Message(check, "#Check_if_user_Online");
            try {
                client.sendToClient(newMessage);
                System.out.format("Check for same user on other sources is completed\n", client.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (request.startsWith("#request_validation_for_availableExams")) {
            List<Object> list = (List<Object>) ms.getObject();
            String name = (String) list.get(0);
            int ExamId = (int) list.get(1);
            String studentId = (String) list.get(2);
            String result = App.validateId(name, ExamId, studentId);
            ReadyExams ready = UpdateExam(ExamId);
            List<Object> data = new ArrayList<>();
            data.add(result);
            data.add(ready);
            Message newMessage = new Message(data, "#request_validation_for_availableExams");
            try {
                client.sendToClient(newMessage);
                System.out.format("Check ID existing is done\n", client.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (request.startsWith("#update_started_exam_Traditional")) {
            List<Object> list = (List<Object>) ms.getObject();
            String name = (String) list.get(0);
            int ExamId = (int) list.get(1);
            App.validateId(name, ExamId, "");
            Message newMessage = new Message(null, "#update_started_exam_Traditional");
            try {
                client.sendToClient(newMessage);
                System.out.format("Check ID existing is done\n", client.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (request.startsWith("#Exam_code_request_for_Checked_exams")) {
            List<String> data = (List<String>) ms.getObject();
            String username = data.get(0);
            String course = data.get(1);
            List<String> examCodes = App.getExamsCode(username, course);
            Message newMessage = new Message(examCodes, "#Exam_code_request_for_Checked_exams");
            try {
                client.sendToClient(newMessage);
                System.out.format("Got the codes sending it to client\n", client.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (request.startsWith("#update_that_student_solved_exam")) {
            // as soon as the student enters the exam we update that he can't solve it again
            List<Object> data = (List<Object>) ms.getObject();
            ReadyExams readyExam = (ReadyExams) data.get(0);
            String name = (String) data.get(1);
            App.UpdateThatStudentSolvedExam(readyExam.getId(), name);

            Message newMessage = new Message(null, "#update_that_student_solved_exam");
            try {
                client.sendToClient(newMessage);
                System.out.format("successfully updated that student solved exam: %s\n", client.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (request.startsWith("#save_solved_exam_request")) {

            List<Object> data = (List<Object>) ms.getObject();
            ReadyExams readyExam = (ReadyExams) data.get(0);
            int remainingTime = (Integer) data.get(1);
            HashMap<Integer, String> answers = (HashMap<Integer, String>) data.get(2);
            String studentId = (String) data.get(3);
            String name = (String) data.get(4);

            //App.UpdateThatStudentSolvedExam(readyExam.getId(), name);
            App.SaveSolvedExam(readyExam, remainingTime, answers, studentId, name);

            Message newMessage = new Message(null, "#save_solved_exam_request");
            try {
                client.sendToClient(newMessage);
                System.out.format("exam successfully submitted and saved by StudentUser: %s\n", client.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (request.startsWith("#request_log_out")) {
            String name = (String) ms.getObject();
            App.LogMeOut(name);
            Message newMessage = new Message(null, "#request_log_out");
            try {
                client.sendToClient(newMessage);
                System.out.format("the user has logged out successfully\n", client.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (request.startsWith("#Students_Info_request_for_checked_exams")) {
            List<String> data = (List<String>) ms.getObject();
            String code = data.get(0);
            String username = data.get(1);
            List<SolvedExams> list = App.getStudentsInfo(code, username);
            Message newMessage = new Message(list, "#Students_Info_request_for_checked_exams");
            try {
                client.sendToClient(newMessage);
                System.out.format("the Students Info has been sent\n", client.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (request.startsWith("#Request_Info_for_checked_exam_studentWindow")) {
            String data = (String) ms.getObject();
            List<SolvedExams> list = App.getSolvedExams(data);
            Message newMessage = new Message(list, "#Exam_Info_request_for_checked_exams_studentWindow");
            try {
                client.sendToClient(newMessage);
                System.out.format("the Exams Info has been sent\n", client.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (request.startsWith("#Request_for_updating_grade")) {
            List<Object> data = (List<Object>) ms.getObject();
            SolvedExams exam = (SolvedExams) data.get(0);
            String bonus = (String) data.get(1);
            String reason = (String) data.get(2);
            String NoteForStudent = (String) data.get(3);
            int grade = App.UpdateGrade(exam, bonus, reason, NoteForStudent);
            Message newMessage = new Message(grade, "#Request_for_updating_grade");
            try {
                client.sendToClient(newMessage);
                System.out.format("the student grade was updated successfully \n", client.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (request.startsWith("#Request_questionlist_for")) {
            int id = (int) ms.getObject();
            List<Questions> list = App.getQuestionListUsingId(id);
            Message newMessage = new Message(null, null);
            if (request.startsWith("#Request_questionlist_for_checked_exam")) {
                newMessage = new Message(list, "#Request_questionlist_for_checked_exam");
            } else if (request.startsWith("#Request_questionlist_for_grades_list")) {
                newMessage = new Message(list, "#Request_questionlist_for_grades_list");
            }
            try {
                client.sendToClient(newMessage);
                System.out.format("the Question list was sent \n", client.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (request.startsWith("#get_manager_requestsList")) {
            System.out.println("Got into request SimpleServer");
            List<Courses> courses = App.getAllCourse();
            List<ReadyExams> readyExamsList = App.getReadyExams(courses);
            List<Request> requestList = App.getRequestListForManager(readyExamsList);
            Message newMessage = new Message(requestList, "#get_manager_requestsList");
            try {
                client.sendToClient(newMessage);
                System.out.format("requestList sent To client From Server\n", client.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (request.startsWith("#solved_exams_manager_request")) {
            int examId = (int) ms.getObject();
            List<SolvedExams> solvedExams = App.getManagerSolvedExams(examId);
            Message newMessage = new Message(solvedExams, "#solved_exams_manager_request");
            try {
                client.sendToClient(newMessage);
                System.out.format("solved exams results sent To client From Server\n", client.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (request.startsWith("#save&send_request_to_manager")) {
            List<Object> data = (List<Object>) ms.getObject();
            ReadyExams readyExam = (ReadyExams) data.get(0);
            int extraTime = (Integer) data.get(1);
            String explanation = (String) data.get(2);
            List<Request> requestList = App.SaveRequests(readyExam, extraTime, explanation);
            Message newMessage = new Message(null, "#save&send_request_to_manager");
            try {
                client.sendToClient(newMessage);
                System.out.format("Request successfully submitted and saved by TeacherUser: %s\n", client.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Message newMessageAllClients = new Message(requestList, "#update_manager_request_list");
            sendToAllClients(newMessageAllClients);
            System.out.format("Request successfully updated in managerWindow\n");

        } else if (request.startsWith("#add_extra_time")) {
            Request selectedRequest = (Request) ms.getObject();
            int extraTime = selectedRequest.getRequestAddedTime();
            ReadyExams readyExam = selectedRequest.getReadyExam();
            App.UpdateExamDuration(selectedRequest, readyExam, extraTime);
            List<Request> requestList = App.updatedRequestList();
            Message newMessage = new Message(requestList, "#update_requestList_manager");
            try {
                client.sendToClient(newMessage);
                System.out.format("Request accepted by Manager : %s\n", client.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Request newRequest = App.fetchDataFromRequest(selectedRequest);
            // send RealTime alert to Teacher !
            Message acceptAlert = new Message(selectedRequest, "#send_alert_teacher_accept");
            sendToAllClients(acceptAlert);
            System.out.format("Accept Alert Sent to teacher Client \n");


        } else if (request.startsWith("#deny_request")) {
            Request selectedRequest = (Request) ms.getObject();
            App.DeleteRequestFromDB(selectedRequest);
            List<Request> requestList = App.updatedRequestList();
            Message newMessage = new Message(requestList, "#update_requestList_manager");
            try {
                client.sendToClient(newMessage);
                System.out.format("Request denied by Manager : %s\n", client.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
            // send RealTime alert to Teacher !
            Message acceptAlert = new Message(selectedRequest, "#send_alert_teacher_deny");
            sendToAllClients(acceptAlert);
            System.out.format("Deny Alert Sent to teacher Client \n");
        } else if (request.startsWith("#Save_traditional_exam_request")) {
            List<Object> data = (List<Object>) ms.getObject();
            File solved_WORD_exam = (File) data.get(0);
            ReadyExams readyExam = (ReadyExams) data.get(1);
            String studentName = (String) data.get(2);
            int time = (Integer) data.get(3);
            App.SaveTraditionalExam(solved_WORD_exam, readyExam, studentName, time);
            Message newMessage = new Message(null, "#Save_traditional_exam_request");
            try {
                client.sendToClient(newMessage);
                System.out.format("Submission of traditional exam was successful : %s\n", client.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (request.startsWith("#request_readyExams_for_teacherInfo")) {
            String techernames = (String) ms.getObject();
            List<ReadyExams> returnList = App.getReadyExamsForCreator(techernames);
            Message newMessage = new Message(returnList, "#request_readyExams_for_teacherInfo");
            try {
                client.sendToClient(newMessage);
                System.out.format("got all exams created/published by the teacher : %s\n", client.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
