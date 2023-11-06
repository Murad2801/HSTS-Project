package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Questions;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class ManagerViewQuestionsController implements Initializable {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="Answer1Col"
    private TableColumn<Questions, String> Answer1Col; // Value injected by FXMLLoader

    @FXML // fx:id="Answer2Col"
    private TableColumn<Questions, String> Answer2Col; // Value injected by FXMLLoader

    @FXML // fx:id="Answer3Col"
    private TableColumn<Questions, String> Answer3Col; // Value injected by FXMLLoader

    @FXML // fx:id="Answer4Col"
    private TableColumn<Questions, String> Answer4Col; // Value injected by FXMLLoader

    @FXML // fx:id="TableView"
    private javafx.scene.control.TableView<Questions> TableView; // Value injected by FXMLLoader

    @FXML // fx:id="correctCol"
    private TableColumn<Questions, String> correctCol; // Value injected by FXMLLoader

    @FXML // fx:id="questionBody"
    private TableColumn<Questions, String> questionBody; // Value injected by FXMLLoader

    @FXML // fx:id="questionNumber"
    private TableColumn<Questions, String> questionNumber; // Value injected by FXMLLoader

    @FXML // fx:id="courseBtn"
    private ComboBox<String> courseBtn; // Value injected by FXMLLoader

    @FXML // fx:id="subjectBtn"
    private ComboBox<String> subjectBtn; // Value injected by FXMLLoader

    @FXML
    void Backfunc(ActionEvent event) {
        App.switchScreen("ManagerWindow");
    }

    @FXML
    void ChooseCourse(ActionEvent event) {
        //String chosen = courseBtn.getSelectionModel().getSelectedItem();
        if (courseBtn.getValue() == null) {
            TableView.getItems().clear();
        } else if (courseBtn.getValue().isEmpty()) {
            TableView.getItems().clear();
        } else {
            requestQuestionList(courseBtn.getValue());
        }
    }

    @FXML
    void ChooseSubj(ActionEvent event) {
        //String chosen = subjectBtn.getSelectionModel().getSelectedItem();
        courseBtn.setDisable(false);
        requestCoursesctNames(subjectBtn.getValue());

    }

    @FXML
    // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL url, ResourceBundle resourceBundle) {
        
        Answer1Col.setCellValueFactory(new PropertyValueFactory<>("Answer1"));
        Answer2Col.setCellValueFactory(new PropertyValueFactory<>("Answer2"));
        Answer3Col.setCellValueFactory(new PropertyValueFactory<>("Answer3"));
        Answer4Col.setCellValueFactory(new PropertyValueFactory<>("Answer4"));
        correctCol.setCellValueFactory(new PropertyValueFactory<>("correctAnswer"));
        questionBody.setCellValueFactory(new PropertyValueFactory<>("Question"));
        questionNumber.setCellValueFactory(new PropertyValueFactory<>("questionCode"));


        subjectBtn.setItems(FXCollections.observableArrayList());
        courseBtn.setItems(FXCollections.observableArrayList());
        requestSubjectNames();

    }


    public void SubjectListFunc(List<String> SubjectsName) {

        if (SubjectsName != null && !SubjectsName.isEmpty()) {
            Platform.runLater(() -> {
                ObservableList<String> SubjectList = FXCollections.observableArrayList(SubjectsName);
                subjectBtn.setItems(SubjectList);
            });
        }
    }

    public void CoursestListFunc(List<String> CourseName) {

        if (CourseName != null && !CourseName.isEmpty()) {
            Platform.runLater(() -> {
                ObservableList<String> SubjectList = FXCollections.observableArrayList(CourseName);
                courseBtn.setItems(SubjectList);
            });
        }
    }

    public void requestSubjectNames() {
        try {
            Message msg = new Message(null, "#manager_subjects_request_for_questions");
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void requestQuestionList(String courseName) {
        try {
            Message msg = new Message(courseName, "#question_list_request_for_manager");
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void requestCoursesctNames(String SubjectName) {
        try {
            Message msg = new Message(SubjectName, "#manager_courses_request_for_questions");
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void ShowQuestionList(List<Questions> questionList) {
        TableView.getItems().clear();
        if (questionList == null || questionList.isEmpty()) {
            App.displayError("no questions are available for this course");
        } else {
            TableView.getItems().clear();
            ObservableList<Questions> observableList = FXCollections.observableArrayList(questionList);
            TableView.setItems(observableList);
        }
    }
}
