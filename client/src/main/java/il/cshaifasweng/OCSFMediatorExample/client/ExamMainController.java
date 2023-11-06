/**
 * Sample Skeleton for 'examain.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Exams;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Questions;
import il.cshaifasweng.OCSFMediatorExample.entities.ReadyQuestions;
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

public class ExamMainController implements Initializable {

    int sum = 0;
    public Exams exam;
    public List<Questions> finalExamQuestionList = new ArrayList<>();
    @FXML
    private Label header;

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
    private TableView<Questions> TableView; // Value injected by FXMLLoader

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

    @FXML // fx:id="durationTf"
    private TextField durationTf; // Value injected by FXMLLoader

    @FXML // fx:id="gradeBtn"
    private TextField gradeBtn; // Value injected by FXMLLoader

    @FXML
    private Label questionLable;

    @FXML
    private Label SumLabel;

    @FXML // fx:id="questionTf"
    private TextField questionTf; // Value injected by FXMLLoader

    @FXML // fx:id="saveQuestion"
    private Button saveQuestion; // Value injected by FXMLLoader

    @FXML
    private TableColumn<ReadyQuestions, Integer> GradeCol;
    @FXML
    private TableView<ReadyQuestions> SelectedTable;

    @FXML
    private TableColumn<ReadyQuestions, String> selectedCol;

    @FXML
    private Button removeBtn;

    @FXML // fx:id="saveExamBtn"
    private Button saveExamBtn; // Value injected by FXMLLoader

    @FXML // fx:id="studentsNoteTf"
    private TextField studentsNoteTf; // Value injected by FXMLLoader

    @FXML // fx:id="teacherNoteTf"
    private TextField teacherNoteTf; // Value injected by FXMLLoader

    @FXML
    void removeFunc(ActionEvent event) {

        int index = exam.getReadyQuestionsList().indexOf(SelectedTable.getSelectionModel().getSelectedItem());
        sum -= (exam.getReadyQuestionsList().get(index).getGrade());
        SumLabel.setText("Sum of Grades: " + sum );
        SelectedTable.getItems().remove(index);
        exam.getReadyQuestionsList().remove(index);
        updateSubmitButtonState();
        removeBtn.setDisable(true);
        if (exam.getReadyQuestionsList().isEmpty()) {
            subjectBtn.setDisable(false);
            courseBtn.setDisable(false);
            if (courseBtn.getItems().isEmpty()) {
                requestCoursesctNames(subjectBtn.getValue());
            }
        }
    }

    @FXML
    void DurationFunc(ActionEvent event) {
    }

    @FXML
    void Backfunc(ActionEvent event) {
        App.switchScreen("ExamPool");
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
        if (subjectBtn.getValue() != null && !subjectBtn.getValue().isEmpty()) {
            String chosen = subjectBtn.getSelectionModel().getSelectedItem();
            courseBtn.setDisable(false);
            requestCoursesctNames(subjectBtn.getValue());
        }

    }

    @FXML
    void SaveExamFunc(ActionEvent event) {

        if (durationTf.getText().isEmpty()) {
            App.displayError("You need to insert duration");
        }

        if (sum != 100) {
            int left = 100 - sum;
            if (left == 1) {
                App.displayFault("The Exam Can Not Be Saved!", "The grades sum should be 100, please add one extra point");
                return;
            } else if (left > 0) {
                App.displayFault("The Exam Can Not Be Saved!", "The grades sum should be 100, please add " + left + " extra points");
                return;
            } else if (left == -1) {
                App.displayFault("The Exam Can Not Be Saved!", "The grades sum should be 100, please remove one point");
                return;
            } else {
                App.displayFault("The Exam Can Not Be Saved!", "The grades sum should be 100, please remove " + (-left) + " points");
                return;
            }
        }

        if (!(studentsNoteTf.getText() == null)) {
            if (!studentsNoteTf.getText().isEmpty()) {
                exam.setNoteForStudent(studentsNoteTf.getText());
            }
        } else {
            studentsNoteTf.setText("");
        }
        if (!(teacherNoteTf.getText() == null)) {
            if (!teacherNoteTf.getText().isEmpty()) {
                exam.setNoteForTeacher(teacherNoteTf.getText());
            }
        } else {
            teacherNoteTf.setText("");
        }
        exam.setDuration(Integer.parseInt(durationTf.getText()));


        for (ReadyQuestions readyQuestion : exam.getReadyQuestionsList()) {
            String questionId = readyQuestion.getQuestionCode();
            // Find the corresponding question in Our Question list
            for (Questions question : TableView.getItems()) {
                if (question.getQuestionCode().equals(questionId)) {
                    finalExamQuestionList.add(question);
                    break;
                }
            }
        }
        requestNewExam();


    }

    @FXML
    void SaveQuestion(ActionEvent event) {

        if (gradeBtn.getText().isEmpty()) {
            App.displayError("Error! You need to insert a grade for the selected question " + TableView.getSelectionModel().getSelectedItem().getQuestionCode());

        } else if (exam.getReadyQuestionsList().isEmpty()) {
            exam.addReadyQestionToList(new ReadyQuestions(TableView.getSelectionModel().getSelectedItem().getQuestionCode(), Integer.parseInt(gradeBtn.getText()), TableView.getSelectionModel().getSelectedItem().getCorrectAnswer()));
            sum += Integer.parseInt(gradeBtn.getText());
            ShowQuestionOnSelectedTable();
            SumLabel.setText("Sum of Grades: " + sum );
            courseBtn.setDisable(true);
            subjectBtn.setDisable(true);
            gradeBtn.setDisable(true);
            gradeBtn.clear();
        } else {
            exam.addReadyQestionToList(new ReadyQuestions(TableView.getSelectionModel().getSelectedItem().getQuestionCode(), Integer.parseInt(gradeBtn.getText()), TableView.getSelectionModel().getSelectedItem().getCorrectAnswer()));
            sum += Integer.parseInt(gradeBtn.getText());
            ShowQuestionOnSelectedTable();
            SumLabel.setText("Sum of Grades: " + sum );
            gradeBtn.setDisable(true);
            gradeBtn.clear();
        }

        saveQuestion.setDisable(true);
        TableView.getSelectionModel().clearSelection();

    }


    @FXML
    void GradeTFunc(ActionEvent event) {
    }

    @FXML
    // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL url, ResourceBundle resourceBundle) {

        exam = new Exams();

        Answer1Col.setCellValueFactory(new PropertyValueFactory<>("Answer1"));
        Answer2Col.setCellValueFactory(new PropertyValueFactory<>("Answer2"));
        Answer3Col.setCellValueFactory(new PropertyValueFactory<>("Answer3"));
        Answer4Col.setCellValueFactory(new PropertyValueFactory<>("Answer4"));
        correctCol.setCellValueFactory(new PropertyValueFactory<>("correctAnswer"));
        questionBody.setCellValueFactory(new PropertyValueFactory<>("Question"));
        questionNumber.setCellValueFactory(new PropertyValueFactory<>("questionCode"));

        GradeCol.setCellValueFactory(new PropertyValueFactory<>("grade"));
        selectedCol.setCellValueFactory(new PropertyValueFactory<>("questionCode"));


        subjectBtn.setItems(FXCollections.observableArrayList());
        courseBtn.setItems(FXCollections.observableArrayList());

        gradeBtn.setDisable(true);      // disable the grade text field at the start

        gradeBtn.textProperty().addListener((observable, oldValue, newValue) -> {
            updateSubmitButtonState();
            saveQuestion.setDisable(newValue.isEmpty());
        });
        durationTf.textProperty().addListener((observable, oldValue, newValue) -> {
            updateSubmitButtonState();

        });

        TableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2 && TableView.getSelectionModel().getSelectedItem() != null) {
                    // Double-click detected
                    gradeBtn.setDisable(false); // Enable the gradeBtn
                    questionLable.setText("The Selected Question code is: " + TableView.getSelectionModel().getSelectedItem().getQuestionCode()
                            + " Please insert a grade for it-->");
                }
            }
        });

        SelectedTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2) {
                    // Double-click detected
                    removeBtn.setDisable(false);
                }
            }
        });
        TextFormatter<String> textFormatter2 = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (!newText.matches("\\d*")) {
                change.setText(""); // Set the change text to an empty string to reject the change
            }
            return change; // Return the modified change
        });
        gradeBtn.setTextFormatter(textFormatter2);
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
        String name = SimpleClient.getClient().getUser();
        try {
            Message msg = new Message(name, "#Subject_names_request_for_exam");
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void requestQuestionList(String courseName) {
        try {
            Message msg = new Message(courseName, "#question_list_request_for_exam");
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
            Message msg = new Message(names, "#Courses_names_request_for_exam");
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void requestNewExam() {
        try {
            List<Object> listE = new ArrayList<>();
            listE.add(exam);
            listE.add(courseBtn.getValue());
            listE.add(subjectBtn.getValue());
            listE.add(SimpleClient.getClient().getUser());
            listE.add(finalExamQuestionList);

            Message msg = new Message(listE, "#add_a_new_exam_request");
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public void ShowQuestionList(List<Questions> questionList) {
        TableView.getItems().clear();
        if (questionList == null || questionList.isEmpty()) {
            App.displayFault("No questions are available for this course", "");
        } else {
            TableView.getItems().clear();
            ObservableList<Questions> observableList = FXCollections.observableArrayList(questionList);
            TableView.setItems(observableList);
        }
    }

    public void ShowQuestionOnSelectedTable() {
        SelectedTable.getItems().clear();
        ObservableList<ReadyQuestions> observableList = FXCollections.observableArrayList(exam.getReadyQuestionsList());
        SelectedTable.setItems(observableList);
    }

    public void ShowExam(Exams exam_) {
        // Ready Questions list
        header.setText(("Edit an Exam"));
        sum = 100;
        SumLabel.setText("Sum of Grades: " + sum );
        List<ReadyQuestions> newList = new ArrayList<>();
        for (ReadyQuestions oldListItem : exam_.getReadyQuestionsList()) {
            newList.add(new ReadyQuestions(oldListItem.getQuestionCode(), oldListItem.getGrade(), oldListItem.getRightAnswer()));
        }
        exam.setReadyQuestionsList(newList);

        subjectBtn.setValue(exam_.getSubject().getSubjectName());
        subjectBtn.setDisable(true);
        courseBtn.setValue(exam_.getCourse().getCourseName());

        SelectedTable.getItems().clear();
        ObservableList<ReadyQuestions> observableList = FXCollections.observableArrayList(newList);
        SelectedTable.setItems(observableList);

        requestQuestionList(exam_.getCourse().getCourseName());

        durationTf.setText(Integer.toString(exam_.getDuration()));

        studentsNoteTf.setText(exam_.getNoteForStudent());

        teacherNoteTf.setText(exam_.getNoteForTeacher());
    }


    private void updateSubmitButtonState() {
        boolean disableSubmit = durationTf.getText().isEmpty() || SelectedTable.getItems().isEmpty();
        saveExamBtn.setDisable(disableSubmit);
    }
}