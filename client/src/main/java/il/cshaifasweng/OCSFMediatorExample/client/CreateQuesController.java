/**
 * Sample Skeleton for 'createQuestion.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Questions;
import il.cshaifasweng.OCSFMediatorExample.entities.ReadyQuestions;
import il.cshaifasweng.OCSFMediatorExample.entities.Subjects;
import javafx.application.Platform;
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
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CreateQuesController implements Initializable {

    public String username;
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="Correct1Btn"
    private RadioButton Correct1Btn; // Value injected by FXMLLoader

    @FXML // fx:id="Correct2Btn"
    private RadioButton Correct2Btn; // Value injected by FXMLLoader

    @FXML // fx:id="Correct3Btn"
    private RadioButton Correct3Btn; // Value injected by FXMLLoader

    @FXML // fx:id="Correct4Btn"
    private RadioButton Correct4Btn; // Value injected by FXMLLoader

    @FXML // fx:id="SelectSubBtn"
    private ComboBox<String> SelectSubBtn; // Value injected by FXMLLoader
    @FXML
    private ComboBox<String> SelectCourseBtn;

    @FXML // fx:id="ClearBtn"
    private Button ClearBtn; // Value injected by FXMLLoader

    @FXML // fx:id="answer1Tf"
    private TextField answer1Tf; // Value injected by FXMLLoader

    @FXML // fx:id="answer2Tf"
    private TextField answer2Tf; // Value injected by FXMLLoader

    @FXML // fx:id="answer3Tf"
    private TextField answer3Tf; // Value injected by FXMLLoader

    @FXML // fx:id="answer4Tf"
    private TextField answer4Tf; // Value injected by FXMLLoader

    @FXML // fx:id="questionTf"
    private TextField questionTf; // Value injected by FXMLLoader

    @FXML // fx:id="backBtn"
    private Button backBtn; // Value injected by FXMLLoader

    @FXML // fx:id="rightAnswer"
    private ToggleGroup rightAnswer; // Value injected by FXMLLoader

    @FXML
    private TableView<Questions> questionsTable;


    @FXML
    private TableColumn<ReadyQuestions, String> questionCol;

    @FXML
    void ChooseSubFunc(ActionEvent event) {
        if (SelectSubBtn.getValue() != null && !SelectSubBtn.getValue().isEmpty()) {
            String chosen = SelectSubBtn.getSelectionModel().getSelectedItem();
            SelectCourseBtn.setDisable(false);
            requestCoursesctNames(SelectSubBtn.getValue());
        }
    }
    @FXML
    void ChooseCourseFunc(ActionEvent event) {
        String chosen = SelectSubBtn.getSelectionModel().getSelectedItem();
        if (SelectCourseBtn.getValue() == null) {
            questionsTable.getItems().clear();
        } else if (SelectCourseBtn.getValue().isEmpty()) {
            questionsTable.getItems().clear();
        } else{
            requestQuestionList(SelectCourseBtn.getValue());
        }
    }

    @FXML
    void SaveData(ActionEvent event) {

        if (SelectSubBtn.getValue() == null|| SelectSubBtn.getValue().isEmpty()|| SelectCourseBtn.getValue() == null ||SelectCourseBtn.getValue().isEmpty() ||  rightAnswer.getSelectedToggle() == null ||questionTf.getText().isEmpty() || answer1Tf.getText().isEmpty() || answer2Tf.getText().isEmpty() || answer3Tf.getText().isEmpty() || answer4Tf.getText().isEmpty()) {
            App.displayError("Error! one or more fields are empty, Please fill them all before saving the question");
        }
        else {
            List<String> createQues = new ArrayList<>();
            RadioButton selectedRadioButton = (RadioButton) rightAnswer.getSelectedToggle();
            String right = selectedRadioButton.getText();

            if (right.equals("Correct 1")) {
                right = answer1Tf.getText();
            }
            else if (right.equals("Correct 2")) {
                right = answer2Tf.getText();
            }
            else if (right.equals("Correct 3")) {
                right = answer3Tf.getText();
            }
            else if (right.equals("Correct 4")) {
                right = answer4Tf.getText();
            }
            createQues.add(questionTf.getText());
            createQues.add(answer1Tf.getText());
            createQues.add(answer2Tf.getText());
            createQues.add(answer3Tf.getText());
            createQues.add(answer4Tf.getText());
            createQues.add(right);
            createQues.add(SelectSubBtn.getValue());
            createQues.add(SelectCourseBtn.getValue());
            requestNewQuestion(createQues);
            requestQuestionList(SelectCourseBtn.getValue());
        }
    }

    @FXML
    void ClearFunc(ActionEvent event) {

        answer1Tf.clear();
        answer2Tf.clear();
        answer3Tf.clear();
        answer4Tf.clear();
        questionTf.clear();
        if(rightAnswer.getSelectedToggle() != null){rightAnswer.getSelectedToggle().setSelected(false);}
        SelectSubBtn.setValue(null);
        SelectCourseBtn.setValue(null);
        SelectCourseBtn.getItems().clear();
        SelectCourseBtn.setDisable(true);

    }

    @FXML
    void GetBack(ActionEvent event) {
        App.switchScreen("TeacherWindow");

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        assert Correct1Btn != null : "fx:id=\"Correct1Btn\" was not injected: check your FXML file 'createQuestion.fxml'.";
        assert Correct2Btn != null : "fx:id=\"Correct2Btn\" was not injected: check your FXML file 'createQuestion.fxml'.";
        assert Correct3Btn != null : "fx:id=\"Correct3Btn\" was not injected: check your FXML file 'createQuestion.fxml'.";
        assert Correct4Btn != null : "fx:id=\"Correct4Btn\" was not injected: check your FXML file 'createQuestion.fxml'.";
        assert SelectSubBtn != null : "fx:id=\"SelectSubBtn\" was not injected: check your FXML file 'createQuestion.fxml'.";
        assert backBtn != null : "fx:id=\"backBtn\" was not injected: check your FXML file 'createQuestion.fxml'.";
        assert rightAnswer != null : "fx:id=\"rightAnswer\" was not injected: check your FXML file 'createQuestion.fxml'.";
        questionCol.setCellValueFactory(new PropertyValueFactory<>("Question"));
        SelectSubBtn.setItems(FXCollections.observableArrayList());
        SelectCourseBtn.setItems(FXCollections.observableArrayList());
        username = SimpleClient.getClient().getUser();

        requestSubjectNames();
        questionsTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2 && questionsTable.getSelectionModel().getSelectedItem() != null) {
                    // Double-click detected
                    questionTf.setText(questionsTable.getSelectionModel().getSelectedItem().getQuestion());
                    answer1Tf.setText(questionsTable.getSelectionModel().getSelectedItem().getAnswer1());
                    answer2Tf.setText(questionsTable.getSelectionModel().getSelectedItem().getAnswer2());
                    answer3Tf.setText(questionsTable.getSelectionModel().getSelectedItem().getAnswer3());
                    answer4Tf.setText(questionsTable.getSelectionModel().getSelectedItem().getAnswer4());
                    String correct = questionsTable.getSelectionModel().getSelectedItem().getCorrectAnswer();
                    if( correct.equals(answer1Tf.getText())){Correct1Btn.setSelected(true);}
                    else if (correct.equals(answer2Tf.getText())) {Correct2Btn.setSelected(true);}
                    else if (correct.equals(answer3Tf.getText())) {Correct3Btn.setSelected(true);}
                    else if (correct.equals(answer4Tf.getText())) {Correct4Btn.setSelected(true);}
                }
            }
        });
    }
   public void SubjectListFunc(List<String> SubjectsName) {

       if (SubjectsName != null && !SubjectsName.isEmpty()) {
           Platform.runLater(() -> {
               ObservableList<String> SubjectList = FXCollections.observableArrayList(SubjectsName);
               SelectSubBtn.setItems(SubjectList);
           });
       }
   }
    public void CoursestListFunc(List<String> CourseName) {

        if (CourseName != null && !CourseName.isEmpty()) {
            Platform.runLater(() -> {
                ObservableList<String> CourseList = FXCollections.observableArrayList(CourseName);
                SelectCourseBtn.setItems(CourseList);
            });
        }
    }


    public void requestNewQuestion(List<String> data){
        try {
            Message msg = new Message(data ,"#Add_new_question_request");
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

   public void requestSubjectNames(){
        String name= SimpleClient.getClient().getUser();
        try {
            Message msg = new Message(name, "#Subject_names_request");
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void requestCoursesctNames(String SubjectName){
        List<String>names= new ArrayList<>();
        names.add(SimpleClient.getClient().getUser());
        names.add(SubjectName);
        try {
            Message msg = new Message(names, "#Courses_names_request");
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void requestQuestionList(String courseName){
        try {
            Message msg = new Message(courseName, "#question_list_request_for_createQuest");
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void ShowQuestionList(List<Questions> questionList){
        System.out.println("here QuestCont: "+questionList);
        questionsTable.getItems().clear();
        if (questionList == null || questionList.isEmpty()){
//            App.displayError("no questions are available for this course");
        }
        else {
            questionsTable.getItems().clear();
            ObservableList<Questions> observableList = FXCollections.observableArrayList(questionList);
            questionsTable.setItems(observableList);
        }
    }
}
