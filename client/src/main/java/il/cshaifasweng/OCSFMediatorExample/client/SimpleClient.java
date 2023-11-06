package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SimpleClient extends AbstractClient {

    public static StudentGradesListController studentGradesListController = null;
    public static StudentsListController stdctr = null;
    public static GradesListController gradectr = null;
    public static QuestionListView questionctr = null;
    private static SimpleClient client = null;
    public StudentController studentController;
    public TeacherController teacherController;
    public LogInController loginctr = null;
    public CreateQuesController quesctr = null;
    public CheckedExamsController checkedctr = null;
    public ManagerViewGradesController managerViewGradesController = null;
    public ExamMainController examctr1 = null;
    public ManagerViewQuestionsController managerViewQuestionsController = null;
    public ExamPoolController examPoolController = null;
    public StudentExamController studentExamController = null;
    public AvailableExamsController availableExamsController = null;
    public TeacherExamPreviewController teacherExamPreviewController = null;
    public SubmissionBoxController submissionBoxController = null;
    public ManagerViewExamsController managerViewExamsController = null;
    public PublishedExamsController publishedExamsController = null;
    public ManagerController managerController = null;
    public ManagerInformationController managerInformationController = null;
    public ExamResultsController examResultsController = null;
    public TeacherInfo teacherInfoController = null;


    private String windowUsername;
    private String userRole;

    public SimpleClient(String host, int port) {
        super(host, port);
    }

    public static SimpleClient getClient() {
        if (client == null) {
            client = new SimpleClient("localhost", 3006);
        }
        return client;
    }

    public static void setClient(SimpleClient client) {
        SimpleClient.client = client;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    @Override
    protected void handleMessageFromServer(Object msg) throws IOException {
        if (msg.getClass().equals(Warning.class)) {            // for the warning by shir
            EventBus.getDefault().post(new WarningEvent((Warning) msg));
        }
        Message ms = (Message) msg;
        String deliver = ms.getString();
        System.out.println("Message received from server " + deliver);

        if (deliver.startsWith("#students_names_request")) {
            List<String> data;
            data = (List<String>) ms.getObject();
            stdctr.showNamesOnList(data);
        } else if (deliver.startsWith("#manager_students_names_request")) {
            List<String> data = new ArrayList<>();
            List<Users> usersLists = (List<Users>) ms.getObject();
            for (Users user : usersLists) {
                data.add(user.getUsername());
            }
            managerViewGradesController.showNamesOnList(data);

        } else if (deliver.startsWith("#information_manager_students_names_request")) {
            List<Users> data;
            data = (List<Users>) ms.getObject();
            //System.out.println("Data: " + data);
            //System.out.println("information_manager_students_names_request got to simple client");
            managerInformationController.showNamesOnList(data);
            //System.out.println("Simple client ran showNamesOnList function");
        } else if (deliver.startsWith("#information_manager_student_exams_request")) {
            List<SolvedExams> data;
            data = (List<SolvedExams>) ms.getObject();
            Platform.runLater(() -> managerInformationController.showInfo(data));
        } else if (deliver.startsWith("#information_manager_teachers_names_request")) {
            List<Users> data;
            data = (List<Users>) ms.getObject();
            managerInformationController.showNamesOnList(data);
        } else if (deliver.startsWith("#information_manager_teacher_exams_request")) {
            List<SolvedExams> data;
            data = (List<SolvedExams>) ms.getObject();
            managerInformationController.showInfo(data);
        } else if (deliver.startsWith("#information_manager_courses_request")) {
            List<Courses> data;
            data = (List<Courses>) ms.getObject();
            managerInformationController.showCoursesOnList(data);
        } else if (deliver.startsWith("#information_manager_course_exams_request")) {
            List<SolvedExams> data;
            data = (List<SolvedExams>) ms.getObject();
            managerInformationController.showInfo(data);
        } else if (deliver.startsWith("#student_grades_request")) {
            List<String> data;
            data = (List<String>) ms.getObject();
            gradectr.showGradesOnList(data);
        } else if (deliver.startsWith("#Students_Info_request_for_checked_exams")) {
            List<SolvedExams> data;
            data = (List<SolvedExams>) ms.getObject();
            checkedctr.ShowInfoList(data);
        } else if (deliver.startsWith("#Request_for_updating_grade")) {
            int data;
            data = (int) ms.getObject();
            checkedctr.UpdateGrade(data);
        } else if (deliver.startsWith("#Request_questionlist_for_checked_exam")) {
            List<Questions> data;
            data = (List<Questions>) ms.getObject();
            checkedctr.DisplayQuestions(data);
        } else if (deliver.startsWith("#Request_questionlist_for_grades_list")) {
            List<Questions> data;
            data = (List<Questions>) ms.getObject();
            studentGradesListController.DisplayQuestions(data);
        } else if (deliver.startsWith("#Exam_Info_request_for_checked_exams_studentWindow")) {
            List<SolvedExams> data;
            data = (List<SolvedExams>) ms.getObject();
            studentGradesListController.ShowInfoList(data);
        } else if (deliver.startsWith("#request_validation_for_availableExams")) {
            List<Object> data = (List<Object>) ms.getObject();
            String id = (String) data.get(0);
            ReadyExams readyExam = (ReadyExams) data.get(1);
            availableExamsController.selectedExam = readyExam;
            availableExamsController.StartExamAfterValidation(id);
        } else if (deliver.startsWith("#Error")) {
            String data = (String) ms.getObject();
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR, data);
                alert.show();
            });
        } else if (deliver.startsWith("#change_grade_request")) {
            List<String> data;
            data = (List<String>) ms.getObject();
            gradectr.showGradesOnList(data);
        } else if (deliver.startsWith("#solved_exams_manager_request")) {
            List<SolvedExams> data;
            data = (List<SolvedExams>) ms.getObject();
            examResultsController.showResultsOnTable(data);
        } else if (deliver.startsWith("#question_list_request")) {
            List<Questions> data;
            data = (List<Questions>) ms.getObject();
            if (deliver.startsWith("#question_list_request_for_exam")) {
                examctr1.ShowQuestionList(data);
            } else if (deliver.startsWith("#question_list_request_for_createQuest")) {
                quesctr.ShowQuestionList(data);
            } else if (deliver.startsWith("#question_list_request_for_manager")) {
                managerViewQuestionsController.ShowQuestionList(data);
            } else {
                System.out.println("question_list_request_for_else");
            }
        } else if (deliver.startsWith("#exam_list_request_for_pool")) {
            List<Exams> data;
            data = (List<Exams>) ms.getObject();
            if (deliver.startsWith("#exam_list_request_for_pool_manager")) {
                managerViewExamsController.showExamsList(data);
            } else {
                examPoolController.showExamsList(data);
            }
        } else if (deliver.startsWith("#validate_user_request")) {
            List<String> data;
            data = (List<String>) ms.getObject(); // this data contains name and role!

            windowUsername = data.get(0);
            String lowercase = windowUsername.toLowerCase();
            String firstletter = lowercase.substring(0, 1).toUpperCase();
            String remainingLetters = lowercase.substring(1);
            windowUsername = firstletter + remainingLetters;

            String role = data.get(1);
            loginctr.MultipleLogInsCheck(role);

        } else if (deliver.startsWith("#Check_if_user_Online")) {
            String data;
            data = (String) ms.getObject();
            loginctr.CheckIfLoginApproved(data);
        } else if (deliver.startsWith("#Exam_code_request_for_Checked_exams")) {
            List<String> data = (List<String>) ms.getObject();
            checkedctr.ExamCodesListFunc(data);
        } else if (deliver.startsWith("#Subject_names_request")) {
            List<String> data;
            data = (List<String>) ms.getObject();
            if (deliver.startsWith("#Subject_names_request_for_exam")) {
                examctr1.SubjectListFunc(data);
            } else if (deliver.startsWith("#Subject_names_request_for_pool_exam")) {
                examPoolController.SubjectListFunc(data);
            } else if (deliver.startsWith("#Subject_names_request_for_Checked_exams")) {
                checkedctr.SubjectListFunc(data);
            } else if (deliver.startsWith("#Subject_names_request_for_manager")) {
                managerViewQuestionsController.SubjectListFunc(data);
            } else {
                quesctr.SubjectListFunc(data);
            }

        } else if (deliver.startsWith("#manager_subjects_request")) {
            List<String> data;
            data = (List<String>) ms.getObject();
            if (deliver.startsWith("#manager_subjects_request_for_questions")) {
                managerViewQuestionsController.SubjectListFunc(data);
            } else {
                managerViewExamsController.SubjectListFunc(data);
            }
        } else if (deliver.startsWith("#manager_courses_request")) {
            List<String> data;
            data = (List<String>) ms.getObject();
            if (deliver.startsWith("#manager_courses_request_for_questions")) {
                managerViewQuestionsController.CoursestListFunc(data);
            } else {
                managerViewExamsController.CoursestListFunc(data);
            }
        } else if (deliver.startsWith("#Courses_names_request")) {
            List<String> data;
            data = (List<String>) ms.getObject();
            if (deliver.startsWith("#Courses_names_request_for_Checked_exams")) {
                checkedctr.CoursestListFunc(data);
            } else if (deliver.startsWith("#Courses_names_request_for_exam")) {
                examctr1.CoursestListFunc(data);
            } else if (deliver.startsWith("#Courses_names_request_for_pool_exam")) {
                examPoolController.CoursestListFunc(data);
            } else {
                quesctr.CoursestListFunc(data);
            }
        } else if (deliver.startsWith("#Add_new_question_request")) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, String.format("Question added successfully"));
                alert.show();
            });

        } else if (deliver.startsWith("#add_a_new_exam_request")) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, String.format("Exam added successfully"));
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    App.switchScreen("ExamPool");
                }
            });
        } else if (deliver.startsWith("#get_ready_exams_requests")) {
            List<ReadyExams> data = (List<ReadyExams>) ms.getObject();
            if (deliver.startsWith("#get_ready_exams_requests_student")) {
                SimpleClient.getClient().availableExamsController.showReadyExams(data);
            } else if (deliver.startsWith("#get_ready_exams_requests_teacher")) {
                SimpleClient.getClient().publishedExamsController.showReadyExams(data);
            }
        } else if (deliver.startsWith("#Managers_students_grades_request_for_viewGrades")) {
            List<SolvedExams> data = (List<SolvedExams>) ms.getObject();
            managerViewGradesController.ShowInfoList(data);
        } else if (deliver.startsWith(("#publish_exam_request"))) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, String.format("Exam Published successfully"));
                alert.show();
            });
        } else if (deliver.startsWith("#save_solved_exam_request")) {
            System.out.println("Exam submission successful");
        } else if (deliver.startsWith(("#request_log_out"))) {
            App.switchScreen("LogInWindow");
        } else if (deliver.startsWith("#get_manager_requestsList")) {
            List<Request> data = (List<Request>) ms.getObject();
            SimpleClient.getClient().managerController.showRequests(data);
        } else if (deliver.startsWith("#update_manager_request_list")) {
            List<Request> requestList = (List<Request>) ms.getObject();
            if (getUserRole().equals("manager") && managerController != null) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.WARNING, String.format("The Request has been added to the list "));
                    alert.setHeaderText("You got a New Request!");
                    alert.setTitle("Update");
                    alert.show();
                });

                SimpleClient.getClient().managerController.showRequests(requestList);
            }


        } else if (deliver.startsWith("#save&send_request_to_manager")) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, String.format("Request sent To Manager Successfully!"));
                alert.show();
            });
            System.out.println("Request submission successful");

        } else if (deliver.startsWith("#update_requestList_manager")) {
            List<Request> data = (List<Request>) ms.getObject();
            managerController.showRequests(data);
        } else if (deliver.startsWith("#send_alert_teacher")) {
            Request request = (Request) ms.getObject();
            String teacherName = request.getPublishingTeacher();
            String courseName = request.getCourseName();
            if (getUserRole().equals("teacher") && publishedExamsController != null && getUser().equals(teacherName)) {
                if (deliver.startsWith("#send_alert_teacher_accept")) {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, String.format("Extra Time"));
                        alert.setTitle("Approved Request");
                        alert.setHeaderText("Extra Time Approved in Exam Of: " + courseName);
                        alert.setContentText("");
                        alert.show();
                    });
                } else if (deliver.startsWith("#send_alert_teacher_deny")) {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Deny Request");
                        alert.setHeaderText("Request Denied in Exam Of: " + courseName);
                        alert.setContentText("");
                        alert.show();
                    });
                }
                System.out.println("Alert received from Manager to client Teacher");
            }
            if (deliver.startsWith("#send_alert_teacher_accept")) {
                ReadyExams readyExam = request.getReadyExam();
                int extraTime = request.getRequestAddedTime();
                if (studentExamController != null && readyExam.getId() == studentExamController.getReadyExam().getId()) {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Extra Time");
                        alert.setHeaderText("You Got a Total of : " + request.getRequestAddedTime() + "Minutes Extra Time");
                        alert.setContentText("Good Luck !");
                        alert.show();
                    });
                    studentExamController.addExtraTime(extraTime, readyExam);
                }
//                System.out.println("got in accept");
//                if (availableExamsController != null) {
//                    System.out.println("got in if");
//                    try {
//                        for (int i = 0; i < readyExam.getStudentsList().size(); i++) {
//                            System.out.println("got in if");
//                            if (readyExam.getStudentsList().get(i).getUsername().equals(getUser())) {
//                                System.out.println("got student in exam list");
//
//                                Platform.runLater(() -> {
//                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                                    alert.setTitle("Update");
//                                    alert.setHeaderText("There was an update regarding your Exam");
//                                    alert.setContentText("the update was for exam: " + readyExam.getExamIdentifier() + "\nGood Luck !");
//                                    alert.show();
//                                });
//                                return;
//                            }
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    return;
//                }
                if (submissionBoxController != null && readyExam.getId() == submissionBoxController.getReadyExam().getId()) {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Extra Time");
                        alert.setHeaderText("You Got a Total of : " + request.getRequestAddedTime() + "Minutes Extra Time");
                        alert.setContentText("Good Luck !");
                        alert.show();
                    });
                    submissionBoxController.addExtraTime(extraTime, readyExam);
                }
            }
        } else if (deliver.startsWith("#request_readyExams_for_teacherInfo")) {
            List<ReadyExams> data = (List<ReadyExams>) ms.getObject();
            teacherInfoController.ShowExamsList(data);
        }

    }

    public String getUser() {
        return this.windowUsername;
    }

}