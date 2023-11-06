package il.cshaifasweng.OCSFMediatorExample.client;
/**
 * Sample Skeleton for 'ManagerWindow.fxml' Controller Class
 */


import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.ReadyExams;
import il.cshaifasweng.OCSFMediatorExample.entities.Request;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ManagerController implements Initializable  {

    @FXML // fx:id="greetManager"
    private Label greetManager; // Value injected by FXMLLoader

    @FXML // fx:id="LogOutBTN"
    private Button LogOutBTN; // Value injected by FXMLLoader

    @FXML // fx:id="managerTable"
    private TableView<Request> managerTable; // Value injected by FXMLLoader

    @FXML // fx:id="moreInformationBTN"
    private Button moreInformationBTN; // Value injected by FXMLLoader
    @FXML // fx:id="viewExamsBTN"
    private Button viewExamsBTN; // Value injected by FXMLLoader

    @FXML // fx:id="viewGradesBTN"
    private Button viewGradesBTN; // Value injected by FXMLLoader

    @FXML // fx:id="viewQuestionsBTN"
    private Button viewQuestionsBTN; // Value injected by FXMLLoader
    @FXML
    private Button acceptRequestBTN;
    @FXML
    private Button denyRequestBTN;

    @FXML
    private Label doubleClkLabel;
    @FXML
    private TableColumn<Request, String> courseCOL;
    @FXML
    private TableColumn<Request, String> examIdCOL;

    @FXML
    private TableColumn<Request, String> teacherCOL;
    @FXML
    private TableColumn<Request, String> requestCOL;
    @FXML
    private TableColumn<Request, String> explanationCOL;

    @FXML
    private TableColumn<Request, Integer> extraTimeCOL;

    @FXML
    void DenyRequestFunc(ActionEvent event) {
        try {
            Message msg = new Message(selectedRequest, "#deny_request");
            SimpleClient.getClient().sendToServer(msg);
            selectedRequest = null;
            denyRequestBTN.setDisable(true);
            acceptRequestBTN.setDisable(true);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @FXML
    void acceptRequestFunc(ActionEvent event) {
        try {
            Message msg = new Message(selectedRequest, "#add_extra_time");
            SimpleClient.getClient().sendToServer(msg);
            selectedRequest = null;
            acceptRequestBTN.setDisable(true);
            denyRequestBTN.setDisable(true);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @FXML
    void clickLogOutBTN(ActionEvent event) {
        SimpleClient.getClient().setUserRole("");
        SimpleClient.getClient().managerController=null;
        requestLogOut();
    }

    @FXML
    void clickMoreInformationBTN(ActionEvent event) {
        App.switchScreen("ManagerInformationWindow");
    }

    @FXML
    void clickViewExamsBTN(ActionEvent event) {
        System.out.println("View Exams button clicked");
        App.switchScreen("ManagerViewExamsWindow");
    }

    @FXML
    void clickViewGradesBTN(ActionEvent event) {
    System.out.println("View Grades button clicked");
    App.switchScreen("ManagerViewGradesWindow");
    }

    @FXML
    void clickViewQuestionsBTN(ActionEvent event) {
        System.out.println("View Questions button clicked");
        App.switchScreen("ManagerViewQuestions");
    }

    public Request selectedRequest = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        greetManager.setText("Welcome, " + SimpleClient.getClient().getUser());
        doubleClkLabel.setAlignment(Pos.CENTER);

        examIdCOL.setCellValueFactory(new PropertyValueFactory<>("ExamId"));
        courseCOL.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        explanationCOL.setCellValueFactory(new PropertyValueFactory<>("requestText"));
        extraTimeCOL.setCellValueFactory(new PropertyValueFactory<>("requestAddedTime"));
        teacherCOL.setCellValueFactory(new PropertyValueFactory<>("publishingTeacher"));

        requestRequestsList();

        managerTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2) {
                    // Double-click detected
                    selectedRequest = managerTable.getSelectionModel().getSelectedItem();
                    acceptRequestBTN.setDisable(false);
                    denyRequestBTN.setDisable(false);
                }
            }
        });
    };

    public void requestRequestsList() {
        String name = SimpleClient.getClient().getUser();
        try {
            Message msg = new Message(name, "#get_manager_requestsList");
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void showRequests(List<Request> data) {
        managerTable.getItems().clear();
        if (data != null) {
            ObservableList<Request> observableList = FXCollections.observableArrayList(data);
            managerTable.setItems(observableList);
        }
    }
    private void requestLogOut() {

        try {
            Message msg = new Message(SimpleClient.getClient().getUser(), "#request_log_out");
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}

