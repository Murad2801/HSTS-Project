/**
 * Sample Skeleton for 'ManagerViewGradesWindow.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ManagerViewGradesController implements Initializable {

    @FXML
    private TableView<SolvedExams> GradeTable;


    @FXML
    private Button backBTN;

    @FXML
    private TableColumn<SolvedExams, String> courseCol;

    @FXML
    private TableColumn<SolvedExams, String> teacherCol;

    @FXML
    private TableColumn<SolvedExams, String> examIdCol;

    @FXML
    private TableColumn<SolvedExams, String> gradeCol;

    @FXML
    private TableColumn<SolvedExams, String> subjectCol;


    @FXML // fx:id="ManagerStudentsList"
    private ListView<String> ManagerStudentsList; // Value injected by FXMLLoader


    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ManagerStudentsList.setItems(FXCollections.observableArrayList());

        teacherCol.setCellValueFactory(cellData -> {
            ReadyExams readyExam = cellData.getValue().getReadyExam();
            if (readyExam != null) {
                return new SimpleStringProperty(readyExam.getPublishingTeacher());
            } else {
                return new SimpleStringProperty("");
            }
        });

        examIdCol.setCellValueFactory(cellData -> {
            ReadyExams readyExam = cellData.getValue().getReadyExam();
            if (readyExam != null) {
                return new SimpleStringProperty(readyExam.getExamIdentifier());
            } else {
                return new SimpleStringProperty("");
            }
        });
        courseCol.setCellValueFactory(cellData -> {
            ReadyExams readyExam = cellData.getValue().getReadyExam();
            if (readyExam != null) {
                return new SimpleStringProperty(readyExam.getCourseName());
            } else {
                return new SimpleStringProperty("");
            }
        });
        subjectCol.setCellValueFactory(cellData -> {
            ReadyExams readyExam = cellData.getValue().getReadyExam();
            if (readyExam != null) {
                return new SimpleStringProperty(readyExam.getSubjectName());
            } else {
                return new SimpleStringProperty("");
            }
        });

        gradeCol.setCellValueFactory(new PropertyValueFactory<>("ShowThisGrade"));

        ManagerStudentsList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2 && ManagerStudentsList.getSelectionModel().getSelectedItem() != null) {
                    requestStudentGrade();
                }
            }
        });
        requestStudentNames();
    }


    public void ShowInfoList(List<SolvedExams> Info) {
        GradeTable.getItems().clear();
        if (Info != null && !Info.isEmpty()) {
            ObservableList<SolvedExams> observableList = FXCollections.observableArrayList(Info);
            GradeTable.setItems(observableList);
        }
    }

    void requestStudentNames() {
        try {
            Message msg = new Message(null, "#manager_students_names_request");
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void requestStudentGrade(){
        String user = ManagerStudentsList.getSelectionModel().getSelectedItem();
        try {
            Message msg = new Message(user, "#3Managers_students_grades_request_for_viewGrades");
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public  void showNamesOnList(List<String> studentNames) {

        // Clear the existing items in the ListView
        ManagerStudentsList.getItems().clear();

        // Convert the studentNames list to an Observable List
        ObservableList<String> items = FXCollections.observableArrayList(studentNames);

        // Add the student names to the ListView
        ManagerStudentsList.setItems(items);
    }

    @FXML
    void backFunc(ActionEvent event) {
        App.switchScreen("ManagerWindow");
    }


}
