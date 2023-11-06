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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ExamPoolController implements Initializable {

    public Exams selectedExam = null;
    @FXML
    private Button CreateNewExamBtn;
    @FXML
    private Button EditExam;
    @FXML
    private TableView<Exams> TableView;
    @FXML
    private Button backBtn;
    @FXML
    private ComboBox<String> courseBtn;
    @FXML
    private TableColumn<Exams, String> examIdCol;
    @FXML
    private Label examLabel;
    @FXML
    private ComboBox<String> subjectBtn;
    @FXML
    private TableColumn<Exams, Integer> durationCol;
    @FXML
    private TableColumn<Exams, String> teacherNotesCol;
    @FXML
    private Button publishBTN;
    @FXML
    private Label examLabel2;
    @FXML
    private ComboBox<String> examTypeBTN;
    @FXML
    private TextField passcodeTF;
    @FXML
    private Button showExamBtn;
    @FXML
    private Button clearAllBTN;

    @FXML
    void clearAllFunc(ActionEvent event) {
        subjectBtn.setValue(null);
        courseBtn.setValue(null);
        selectedExam = null;
        examTypeBTN.setValue(null);
        passcodeTF.clear();
        showExamBtn.setDisable(true);
        examTypeBTN.setDisable(true);
        passcodeTF.setDisable(true);
        publishBTN.setDisable(true);
        EditExam.setDisable(true);
        examLabel2.setVisible(false);
    }

    @FXML
    void Backfunc(ActionEvent event) {
        App.switchScreen("TeacherWindow");
    }

    @FXML
    void PreviewExamFunc(ActionEvent event) {
        App.switchScreen("DigitalExamTeacher");
    }

    @FXML
    void ChooseCourse(ActionEvent event) {
        if (courseBtn.getValue() == null) {
            TableView.getItems().clear();
        } else if (courseBtn.getValue().isEmpty()) {
            TableView.getItems().clear();
        } else {
            EditExam.setDisable(true); // Disable the edit button
            examTypeBTN.setDisable(true);
            passcodeTF.setDisable(true);
            showExamBtn.setDisable(true);
            publishBTN.setDisable(true);
            examLabel.setText("");
            requestExamsList(courseBtn.getValue());
        }
    }

    private void requestExamsList(String course_) { // this value is the chosen course
        try {
            Message msg = new Message(course_, "#exam_list_request_for_pool");
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void showExamsList(List<Exams> examList) {
        TableView.getItems().clear();
        if (examList == null || examList.isEmpty()) {
            App.displayFault("No exams are available for this course", "You can create one using the Create New Exam button");
        } else {
            TableView.getItems().clear();
            ObservableList<Exams> observableList = FXCollections.observableArrayList(examList);
            TableView.setItems(observableList);
        }
    }

    @FXML
    void ChooseSubj(ActionEvent event) {
        //String chosen = subjectBtn.getSelectionModel().getSelectedItem();
        courseBtn.setDisable(false);
        EditExam.setDisable(true); // Disable the edit button
        examTypeBTN.setDisable(true);
        passcodeTF.setDisable(true);
        showExamBtn.setDisable(true);
        publishBTN.setDisable(true);
        examLabel.setText("");
        requestCoursesctNames(subjectBtn.getValue());
    }


    @FXML
    void CreateNewExamFunc(ActionEvent event) {
        App.switchScreen("examain");
    }

    @FXML
    void EditExamFunc(ActionEvent event) {
        App.switchScreen("examain_to_edit");
    }

    @FXML
    void publishExamFunc(ActionEvent event) {
        if (!containsLettersAndNumbers(passcodeTF.getText())) {
            App.displayError("Passcode must be 4-digits containing letters & numbers!");
        } else {
            publishExamRequest();
            clearAllFunc(null);
            // we need to call publish exam request with the selected exam
            // we need to save passcode to exam
        }
    }

    @FXML
    void PasscodeFunc(ActionEvent event) {

    }

    @FXML
    void examTypeFunc(ActionEvent event) {
        examLabel2.setText("Now enter a 4-digit code containing\nletters & numbers");
        passcodeTF.setDisable(false);
        showExamBtn.setDisable(false);
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
                ObservableList<String> CourseList = FXCollections.observableArrayList(CourseName);
                courseBtn.setItems(CourseList);
            });
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        examIdCol.setCellValueFactory(new PropertyValueFactory<>("ExamIdentifier"));
        durationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
        teacherNotesCol.setCellValueFactory(new PropertyValueFactory<>("noteForTeacher"));
        List<String> comboList = new ArrayList<>();
        comboList.add("Digital Exam");
        comboList.add("Traditional Exam");
        ObservableList<String> examTypeList = FXCollections.observableArrayList(comboList);
        examTypeBTN.setItems(examTypeList);

        passcodeTF.textProperty().addListener((observable, oldValue, newValue) -> {
            publishBTN.setDisable(passcodeTF.getText().isEmpty());
        });
        TableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2 && TableView.getSelectionModel().getSelectedItem() != null) {
                    // Double-click detected
                    showExamBtn.setDisable(false);
                    examTypeBTN.setDisable(false);
                    examLabel2.setText("To publish an exam choose an exam type");
                    examLabel2.setVisible(true);
                    EditExam.setDisable(false); // Enable the edit button
                    examLabel.setText("The Selected Exam's code is: " + TableView.getSelectionModel().getSelectedItem().getExamIdentifier());
                    selectedExam = TableView.getSelectionModel().getSelectedItem();
                }
            }
        });

        //limit the textfiled for only 4 characters
        int maxLength = 4;
        TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.length() <= maxLength) {
                return change;
            }
            return null; // Reject the change if it exceeds the maximum length
        });
        passcodeTF.setTextFormatter(textFormatter);

        requestSubjectNames();
    }

    public void requestSubjectNames() {
        String name = SimpleClient.getClient().getUser();
        try {
            Message msg = new Message(name, "#Subject_names_request_for_pool_exam");
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void requestCoursesctNames(String SubjectName) {
        List<String> names = new ArrayList<>();
        names.add(SimpleClient.getClient().getUser());
        names.add(SubjectName);

        try {
            Message msg = new Message(names, "#Courses_names_request_for_pool_exam");
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void publishExamRequest() {
        List<Object> data = new ArrayList<>();
        data.add(selectedExam);
        data.add(examTypeBTN.getValue());
        data.add(passcodeTF.getText());
        data.add(SimpleClient.getClient().getUser());
        data.add(courseBtn.getValue());

        try {
            Message msg = new Message(data, "#publish_exam_request");
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private boolean containsLettersAndNumbers(String text) {
        boolean hasLetters = false;
        boolean hasNumbers = false;

        if (text.length() != 4)
            return false;
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                hasLetters = true;
            } else if (Character.isDigit(c)) {
                hasNumbers = true;
            }

            if (hasLetters && hasNumbers) {
                return true;
            }
        }
        return false;
    }

}
