/**
 * Sample Skeleton for 'ManagerViewExamsWindow.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Exams;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ManagerViewExamsController implements Initializable {

    public Exams selectedExam = null;
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="TableView"
    private TableView<Exams> TableView; // Value injected by FXMLLoader

    @FXML // fx:id="backBtn"
    private Button backBtn; // Value injected by FXMLLoader
    @FXML
    private Button examResultsBTN;


    @FXML // fx:id="courseBtn"
    private ComboBox<String> courseBtn; // Value injected by FXMLLoader

    @FXML // fx:id="durationCol"
    private TableColumn<Exams, Integer> durationCol; // Value injected by FXMLLoader

    @FXML // fx:id="examIdCol"
    private TableColumn<Exams, String> examIdCol; // Value injected by FXMLLoader

    @FXML // fx:id="examLabel"
    private Label examLabel; // Value injected by FXMLLoader

    @FXML // fx:id="examLabel2"
    private Label examLabel2; // Value injected by FXMLLoader

    @FXML // fx:id="showExamBtn"
    private Button showExamBtn; // Value injected by FXMLLoader

    @FXML // fx:id="subjectBtn"
    private ComboBox<String> subjectBtn; // Value injected by FXMLLoader

    @FXML // fx:id="teacherNotesCol"
    private TableColumn<Exams, String> teacherNotesCol; // Value injected by FXMLLoader

    @FXML
    void Backfunc(ActionEvent event) {
        App.switchScreen("ManagerWindow");
    }

    @FXML
    void ChooseCourse(ActionEvent event) {
        if (courseBtn.getValue() == null) {
            TableView.getItems().clear();
        } else if (courseBtn.getValue().isEmpty()) {
            TableView.getItems().clear();
        } else {
            examLabel.setText("");
            requestExamsList(courseBtn.getValue());
        }
    }

    private void requestExamsList(String course_) { // this value is the chosen course
        try {
            Message msg = new Message(course_, "#exam_list_request_for_pool_manager");
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @FXML
    void ChooseSubj(ActionEvent event) {
        //String chosen = subjectBtn.getSelectionModel().getSelectedItem();
        courseBtn.setDisable(false);
        examLabel.setText("");
        requestCoursesctNames(subjectBtn.getValue());
    }

    public void requestSubjectNames() {
        String name = SimpleClient.getClient().getUser();
        try {
            Message msg = new Message(name, "#manager_subjects_request");
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void CoursestListFunc(List<String> CourseName) {

        if (CourseName != null && !CourseName.isEmpty()) {
            Platform.runLater(() -> {
                ObservableList<String> CourseList = FXCollections.observableArrayList(CourseName);
                courseBtn.setItems(CourseList);
            });
        }
    }

    public void requestCoursesctNames(String SubjectName) {
        try {
            Message msg = new Message(SubjectName, "#manager_courses_request");
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void SubjectListFunc(List<String> SubjectsName) {
        if (SubjectsName != null && !SubjectsName.isEmpty()) {
            Platform.runLater(() -> {
                ObservableList<String> SubjectList = FXCollections.observableArrayList(SubjectsName);
                subjectBtn.setItems(SubjectList);
            });
        }
    }

    @FXML
    void ShowExamTEST(ActionEvent event) {
        App.switchScreen("DigitalExamManager");

    }


    public void showExamsList(List<Exams> examList) {
        Platform.runLater(() -> {

            TableView.getItems().clear();
            if (examList == null || examList.isEmpty()) {
                App.displayFault("No exams are available for this course", "You have to wait for a teacher to publish one");
            } else {
                TableView.getItems().clear();
                ObservableList<Exams> observableList = FXCollections.observableArrayList(examList);
                TableView.setItems(observableList);
            }
        });

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        examIdCol.setCellValueFactory(new PropertyValueFactory<>("ExamIdentifier"));
        durationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
        teacherNotesCol.setCellValueFactory(new PropertyValueFactory<>("noteForTeacher"));
        requestSubjectNames();
        TableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2 && TableView.getSelectionModel().getSelectedItem() != null) {
                    // Double-click detected
                    showExamBtn.setDisable(false);
                    examResultsBTN.setDisable(false);

                    examLabel.setText("The Selected Exam's code is: " + TableView.getSelectionModel().getSelectedItem().getExamIdentifier());
                    selectedExam = TableView.getSelectionModel().getSelectedItem();

                }
            }
        });
    }

    @FXML
    void showExamResults(ActionEvent event) {
        requestExamResults(selectedExam);
        App.switchScreen("ExamResultsWindow");
    }

    public void requestExamResults(Exams selectedExam) {
        int examID = selectedExam.getId();
        try {
            Message msg = new Message(examID, "#solved_exams_manager_request");
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
