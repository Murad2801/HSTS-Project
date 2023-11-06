package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Exams;
import il.cshaifasweng.OCSFMediatorExample.entities.ReadyExams;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Stage appStage;
    private static Scene scene;

    private SimpleClient client;


    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void setWindowTitle(String title) {
        appStage.setTitle(title);
    }

    public static void setContent(String pageName) throws IOException {
        Parent root = loadFXML(pageName);

        scene = new Scene(root);
        appStage.setScene(scene);
        appStage.show();

    }

    public static void switchScreen(String screenName) {
        switch (screenName) {
            case "TeacherWindow":
                Platform.runLater(() -> {
                    setWindowTitle("TeacherWindow");
                    try {
                        setContent("TeacherWindow");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                break;
            case "ManagerInformationWindow":
                Platform.runLater(() -> {
                    setWindowTitle("Manager Window");
                    try {
                        FXMLLoader loader = new FXMLLoader(App.class.getResource("ManagerInformationWindow.fxml"));
                        Parent root = loader.load();
                        SimpleClient.getClient().managerInformationController = loader.getController();
                        scene = new Scene(root);
                        appStage.setScene(scene);
                        appStage.show();
                        if (SimpleClient.getClient().managerInformationController == null)
                            System.out.println("error");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                break;
            case "ManagerWindow":
                Platform.runLater(() -> {
                    setWindowTitle("Manager Window");
                    try {
                        FXMLLoader loader = new FXMLLoader(App.class.getResource("ManagerWindow.fxml"));
                        Parent root = loader.load();
                        SimpleClient.getClient().managerController = loader.getController();
                        ManagerController savedManager = SimpleClient.getClient().managerController;
                        scene = new Scene(root);
                        appStage.setScene(scene);
                        appStage.show();
                        if (SimpleClient.getClient().managerController == null)
                            System.out.println("error");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                break;
            case "createQuestion":
                Platform.runLater(() -> {
                    setWindowTitle("create Questions");
                    try {
                        FXMLLoader loader = new FXMLLoader(App.class.getResource("createQuestion.fxml"));
                        Parent root = loader.load();
                        SimpleClient.getClient().quesctr = loader.getController();
                        scene = new Scene(root);
                        appStage.setScene(scene);
                        appStage.show();
                        if (SimpleClient.getClient().quesctr == null)
                            System.out.println("error");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                break;
            case "LogInWindow":
                Platform.runLater(() -> {
                    setWindowTitle("Login");
                    try {
                        setContent("LogInWindow");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                break;
            case "StudentWindow":
                Platform.runLater(() -> {
                    setWindowTitle("StudentWindow");
                    try {
                        setContent("StudentWindow");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                break;
            case "ManagerViewExamsWindow":
                Platform.runLater(() -> {
                    setWindowTitle("ManagerViewExamsWindow");
                    try {
                        FXMLLoader loader = new FXMLLoader(App.class.getResource("ManagerViewExamsWindow.fxml"));
                        Parent root = loader.load();
                        SimpleClient.getClient().managerViewExamsController = loader.getController();
                        scene = new Scene(root);
                        appStage.setScene(scene);
                        appStage.show();
                        if (SimpleClient.getClient().managerViewExamsController == null)
                            System.out.println("error");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                break;
            case "StudentListWindow":
                Platform.runLater(() -> {
                    setWindowTitle("Student List");
                    try {
                        FXMLLoader loader = new FXMLLoader(App.class.getResource("studentList.fxml"));
                        Parent root = loader.load();
                        SimpleClient.getClient().stdctr = loader.getController();
                        scene = new Scene(root);
                        appStage.setScene(scene);
                        appStage.show();
                        if (SimpleClient.getClient().stdctr == null)
                            System.out.println("error");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                break;
            case "ManagerViewQuestions":
                Platform.runLater(() -> {
                    setWindowTitle("ManagerViewQuestions");
                    try {
                        FXMLLoader loader = new FXMLLoader(App.class.getResource("ManagerViewQuestions.fxml"));
                        Parent root = loader.load();
                        SimpleClient.getClient().managerViewQuestionsController = loader.getController();
                        scene = new Scene(root);
                        appStage.setScene(scene);
                        appStage.show();
                        if (SimpleClient.getClient().managerViewQuestionsController == null)
                            System.out.println("error");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                break;

            case "examain":
                Platform.runLater(() -> {
                    setWindowTitle("create Exam");
                    try {
                        FXMLLoader loader = new FXMLLoader(App.class.getResource("examain.fxml"));
                        Parent root = loader.load();
                        SimpleClient.getClient().examctr1 = loader.getController();
                        scene = new Scene(root);
                        appStage.setScene(scene);
                        appStage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                break;
            case "examain_to_edit":
                Platform.runLater(() -> {
                    setWindowTitle("Edit an Exam");
                    try {
                        //Exams exam_ = new Exams();
                        Exams exam_ = SimpleClient.getClient().examPoolController.selectedExam;
                        FXMLLoader loader = new FXMLLoader(App.class.getResource("examain.fxml"));
                        Parent root = loader.load();
                        SimpleClient.getClient().examctr1 = loader.getController();
                        SimpleClient.getClient().examctr1.ShowExam(exam_);
                        scene = new Scene(root);
                        appStage.setScene(scene);
                        appStage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                break;
            case "ExamPool":
                Platform.runLater(() -> {
                    setWindowTitle("Exam Pool");
                    try {
                        FXMLLoader loader = new FXMLLoader(App.class.getResource("examPool.fxml"));
                        Parent root = loader.load();
                        SimpleClient.getClient().examPoolController = loader.getController();
                        scene = new Scene(root);
                        appStage.setScene(scene);
                        appStage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                break;
            case "ExamResultsWindow":
                Platform.runLater(() -> {
                    setWindowTitle("Exam Results");
                    try {
                        FXMLLoader loader = new FXMLLoader(App.class.getResource("ExamResultsWindow.fxml"));
                        Parent root = loader.load();
                        SimpleClient.getClient().examResultsController = loader.getController();
                        scene = new Scene(root);
                        appStage.setScene(scene);
                        appStage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                break;
            case "DigitalExamStudent":
                Platform.runLater(() -> {
                    setWindowTitle("Digital Exam");
                    try {
                        ReadyExams readyExam = SimpleClient.getClient().availableExamsController.selectedExam;
                        Exams exam_ = readyExam.getExam();
                        String studentId = SimpleClient.getClient().availableExamsController.getIdTF();
                        FXMLLoader loader = new FXMLLoader(App.class.getResource("studentExam.fxml"));
                        Parent root = loader.load();
                        SimpleClient.getClient().availableExamsController = null;

                        SimpleClient.getClient().studentExamController = loader.getController();
                        SimpleClient.getClient().studentExamController.setStudentId(studentId);
                        SimpleClient.getClient().studentExamController.setReadyExam(readyExam);
                        SimpleClient.getClient().studentExamController.BuildExam(exam_);
                        SimpleClient.getClient().studentExamController.updateExam();

                        // to update the extra time for the exams that were not open!!!
                        if (SimpleClient.getClient().studentExamController.getReadyExam().getUpdatedDuration() > 0) {
                            int updatedTime = SimpleClient.getClient().studentExamController.getReadyExam().getUpdatedDuration() * 60;
                            SimpleClient.getClient().studentExamController.setRemainingTimeInSeconds(updatedTime);
                            int extraTime = SimpleClient.getClient().studentExamController.getReadyExam().getUpdatedDuration()
                                    - SimpleClient.getClient().studentExamController.getReadyExam().getDuration();
                            Platform.runLater(() -> {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Extra Time");
                                alert.setHeaderText("You Got a Total of : " + extraTime + "Minutes Extra Time");
                                alert.setContentText("Good Luck !");
                                alert.show();
                            });
                        }
                        scene = new Scene(root);
                        appStage.setScene(scene);
                        appStage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                break;
            case "TraditionalExamStudent":
                Platform.runLater(() -> {
                    setWindowTitle("Traditional Exam");
                    try {
                        ReadyExams readyExam = SimpleClient.getClient().availableExamsController.selectedExam;
                        Exams exam_ = readyExam.getExam();
                        FXMLLoader loader = new FXMLLoader(App.class.getResource("submissionBox.fxml"));
                        Parent root = loader.load();
                        SimpleClient.getClient().availableExamsController = null;

                        SimpleClient.getClient().submissionBoxController = loader.getController();
                        SimpleClient.getClient().submissionBoxController.Timer(exam_);
                        SimpleClient.getClient().submissionBoxController.setReadyExam(readyExam);
                        SimpleClient.getClient().submissionBoxController.updateExam();

                        // to update the extra time for the exams that were not open!!!
                        if (SimpleClient.getClient().submissionBoxController.getReadyExam().getUpdatedDuration() > 0) {
                            int updatedTime = SimpleClient.getClient().submissionBoxController.getReadyExam().getUpdatedDuration() * 60;
                            SimpleClient.getClient().submissionBoxController.setRemainingTimeInSeconds(updatedTime);
                            int extraTime = SimpleClient.getClient().submissionBoxController.getReadyExam().getUpdatedDuration()
                                    - SimpleClient.getClient().submissionBoxController.getReadyExam().getDuration();
                            Platform.runLater(() -> {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Extra Time");
                                alert.setHeaderText("You Got a Total of : " + extraTime + "Minutes Extra Time");
                                alert.setContentText("Good Luck !");
                                alert.show();
                            });
                        }
                        scene = new Scene(root);
                        appStage.setScene(scene);
                        appStage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                break;
            case "DigitalExamTeacher":
                Platform.runLater(() -> {
                    setWindowTitle("Digital Exam");
                    try {
                        Exams exam_ = SimpleClient.getClient().examPoolController.selectedExam;
                        FXMLLoader loader = new FXMLLoader(App.class.getResource("teacherExam.fxml"));
                        Parent root = loader.load();
                        SimpleClient.getClient().teacherExamPreviewController = loader.getController();
                        SimpleClient.getClient().teacherExamPreviewController.BuildExam(exam_);
                        scene = new Scene(root);
                        appStage.setScene(scene);
                        appStage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                break;
            case "DigitalExamManager":
                Platform.runLater(() -> {
                    setWindowTitle("Digital Exam");
                    try {
                        Exams exam_ = SimpleClient.getClient().managerViewExamsController.selectedExam;
                        FXMLLoader loader = new FXMLLoader(App.class.getResource("teacherExam.fxml"));
                        Parent root = loader.load();
                        SimpleClient.getClient().teacherExamPreviewController = loader.getController();
                        SimpleClient.getClient().teacherExamPreviewController.BuildExam(exam_);
                        SimpleClient.getClient().teacherExamPreviewController.setManager(true);
                        scene = new Scene(root);
                        appStage.setScene(scene);
                        appStage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                break;
            case "ManagerViewGradesWindow":
                Platform.runLater(() -> {
                    setWindowTitle("Student Grades");
                    try {
                        FXMLLoader loader = new FXMLLoader(App.class.getResource("ManagerViewGradesWindow.fxml"));
                        Parent root = loader.load();
                        SimpleClient.getClient().managerViewGradesController = loader.getController();
                        scene = new Scene(root);
                        appStage.setScene(scene);
                        appStage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                break;
            case "availableExams":
                Platform.runLater(() -> {
                    setWindowTitle("Available Exams");
                    try {
                        FXMLLoader loader = new FXMLLoader(App.class.getResource("studentAvailableExams.fxml"));
                        Parent root = loader.load();
                        SimpleClient.getClient().availableExamsController = loader.getController();
                        scene = new Scene(root);
                        appStage.setScene(scene);
                        appStage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                break;
            case "studentGradesList":
                Platform.runLater(() -> {
                    setWindowTitle("Available Grades");
                    try {
                        FXMLLoader loader = new FXMLLoader(App.class.getResource("studentGradesList.fxml"));
                        Parent root = loader.load();
                        SimpleClient.getClient().studentGradesListController = loader.getController();
                        scene = new Scene(root);
                        appStage.setScene(scene);
                        appStage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                break;
            case "PublishedExamsWindow":
                Platform.runLater(() -> {
                    setWindowTitle("Published Exams");
                    try {
                        FXMLLoader loader = new FXMLLoader(App.class.getResource("publishedExams.fxml"));
                        Parent root = loader.load();
                        SimpleClient.getClient().publishedExamsController = loader.getController();
                        scene = new Scene(root);
                        appStage.setScene(scene);
                        appStage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                break;
            case "checkedexams":
                Platform.runLater(() -> {
                    setWindowTitle("Checked Exams");
                    try {
                        FXMLLoader loader = new FXMLLoader(App.class.getResource("checkedexams.fxml"));
                        Parent root = loader.load();
                        SimpleClient.getClient().checkedctr = loader.getController();
                        scene = new Scene(root);
                        appStage.setScene(scene);
                        appStage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                break;
            case "teacherInfo":
                Platform.runLater(() -> {
                    setWindowTitle("Teacher Information");
                    try {
                        FXMLLoader loader = new FXMLLoader(App.class.getResource("teacherinfo.fxml"));
                        Parent root = loader.load();
                        SimpleClient.getClient().teacherInfoController = loader.getController();
                        scene = new Scene(root);
                        appStage.setScene(scene);
                        appStage.show();
                        if (SimpleClient.getClient().teacherInfoController == null)
                            System.out.println("error");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                break;
        }
    }

    public static void displayError(String error) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR, error);
            alert.show();
        });
    }

    public static void displayFault(String header, String error) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR, error);
            alert.setHeaderText(header);
            alert.show();
        });
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        EventBus.getDefault().register(this);
        client = SimpleClient.getClient();
        client.openConnection();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("LogInWindow.fxml"));
        Parent root = loader.load();
        SimpleClient.getClient().loginctr = loader.getController();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        appStage = stage;
    }

    @Override
    public void stop() throws Exception {
        // TODO Auto-generated method stub
        EventBus.getDefault().unregister(this);
        super.stop();
    }

    @Subscribe
    public void onWarningEvent(WarningEvent event) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING, String.format("Message:%s\nTimestamp: %s\n",
                    event.getWarning().getMessage(),
                    event.getWarning().getTime().toString())
            );
            alert.show();
        });

    }

}