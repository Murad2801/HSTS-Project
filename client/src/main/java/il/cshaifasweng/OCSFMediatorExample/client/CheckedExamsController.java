/**
 * Sample Skeleton for 'checkedexams.fxml' Controller Class
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
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class CheckedExamsController implements Initializable {

    static SolvedExams solvedExam;
    static ReadyExams readyExam;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="CourseCombo"
    private ComboBox<String> CourseCombo; // Value injected by FXMLLoader
    @FXML
    private TableColumn<SolvedExams, String> DateCol;

    @FXML // fx:id="CourseLabel"
    private Label CourseLabel; // Value injected by FXMLLoader

    @FXML // fx:id="Anchor"
    private AnchorPane Anchor; // Value injected by FXMLLoader

    @FXML // fx:id="ScrollP"
    private ScrollPane ScrollP; // Value injected by FXMLLoader

    @FXML // fx:id="GradeLabel"
    private Label GradeLabel; // Value injected by FXMLLoader

    @FXML // fx:id="ExamCombo"
    private ComboBox<String> ExamCombo; // Value injected by FXMLLoader

    @FXML // fx:id="ExamContainer"
    private VBox ExamContainer; // Value injected by FXMLLoader

    @FXML // fx:id="FinalGradeCol"
    private TableColumn<SolvedExams, Integer> FinalGradeCol; // Value injected by FXMLLoader

    @FXML // fx:id="InfoTable"
    private TableView<SolvedExams> InfoTable; // Value injected by FXMLLoader

    @FXML // fx:id="NoteLabel"
    private Label NoteLabel; // Value injected by FXMLLoader

    @FXML // fx:id="NoteLabel"
    private Label NoteLabel1; // Value injected by FXMLLoader

    @FXML // fx:id="Publish"
    private Button Publish; // Value injected by FXMLLoader

    @FXML // fx:id="ReasonTF"
    private TextField ReasonTF; // Value injected by FXMLLoader

    @FXML // fx:id="HightSchool"
    private Label HightSchool; // Value injected by FXMLLoader

    @FXML // fx:id="StudentIdCol"
    private TableColumn<SolvedExams, String> StudentIdCol; // Value injected by FXMLLoader

    @FXML // fx:id="StudentNameCol"
    private TableColumn<SolvedExams, String> StudentNameCol; // Value injected by FXMLLoader

    @FXML // fx:id="SubjectCombo"
    private ComboBox<String> SubjectCombo; // Value injected by FXMLLoader

    @FXML // fx:id="SubjectLabel"
    private Label SubjectLabel; // Value injected by FXMLLoader

    @FXML // fx:id="backBTN"
    private ImageView backBTN; // Value injected by FXMLLoader

    @FXML // fx:id="changeAllBtn"
    private Button changeAllBtn; // Value injected by FXMLLoader

    @FXML // fx:id="changeOneBtn"
    private Button changeOneBtn; // Value injected by FXMLLoader

    @FXML // fx:id="gradeTF"
    private TextField gradeTF; // Value injected by FXMLLoader

    @FXML // fx:id="nameLabel"
    private Label nameLabel; // Value injected by FXMLLoader

    @FXML
    private ImageView refreshBtn;
    @FXML
    private TextField NoteTF;
    @FXML
    private Label teacherLabel;

    @FXML
    void refreshFunc(MouseEvent event) {
        if(NoteTF != null){
            NoteTF.clear();
        }
        if (ReasonTF != null){
            ReasonTF.clear();
        }
        if(gradeTF != null){
            gradeTF.clear();
        }
        ExamContainer.getChildren().clear();
        nameLabel.setText("");
        NoteLabel.setText("");
        NoteLabel1.setText("");
        GradeLabel.setText("");
        requestSubjectNames();

    }


    @FXML
    void ChangeForAll(ActionEvent event) {


    }

    @FXML
    void PublishFunc(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to Publish grade without changes ?");
        alert.setHeaderText("Confirm Publish");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // User clicked OK, handle the submit action here
            System.out.println("User clicked OK,Publishing garde ...");
            requestChangeGrade("0", "No Changes needed");
            requestStudentsInfo(ExamCombo.getValue());
        }
        gradeTF.clear();
        ReasonTF.clear();
        NoteTF.clear();
        NoteTF.setDisable(true);
        gradeTF.setDisable(true);
        ReasonTF.setDisable(true);
    }


    @FXML
    void ChangeForOne(ActionEvent event) {
        if (!gradeTF.getText().isEmpty() && !ReasonTF.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to Update grade and publish it ?");
            alert.setHeaderText("Confirm Publish");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // User clicked OK, handle the submit action here
                System.out.println("User clicked OK,updating garde and publishing ...");
                requestChangeGrade(gradeTF.getText(), ReasonTF.getText());
                requestStudentsInfo(ExamCombo.getValue());
            }
            NoteTF.clear();
            gradeTF.clear();
            ReasonTF.clear();
        } else {
            App.displayError("Some Info are missing");
        }

    }

    @FXML
    void ChooseCourseFunc(ActionEvent event) {
        try {
            clearPage("course");
            if (CourseCombo.getValue() == null || CourseCombo.getValue().isEmpty()) {
                InfoTable.getItems().clear();
            } else {
                ExamCombo.setDisable(false);
                requestExamCodes(CourseCombo.getValue());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void ChooseExamFunc(ActionEvent event) {
        clearPage("exam");
        if (ExamCombo.getValue() == null) {
            InfoTable.getItems().clear();
        } else if (ExamCombo.getValue().isEmpty()) {
            InfoTable.getItems().clear();
        } else if (!ExamCombo.getValue().equals("No exmas solved yet")) {
            requestStudentsInfo(ExamCombo.getValue());
        }
    }
void clearPage(String Case){
    NoteLabel.setText("");
    NoteLabel1.setText("");
    GradeLabel.setText("");
    CourseLabel.setText("");
    HightSchool.setText("");
    SubjectLabel.setText("");
    NoteTF.clear();
    gradeTF.clear();
    ReasonTF.clear();
    nameLabel.setText("");
    teacherLabel.setText("");
    ExamContainer.getChildren().clear();
    if(Case.equals("course")) {
        ExamCombo.getItems().clear();
    }if(Case.equals("subject")){
        ExamCombo.getItems().clear();
        CourseCombo.getItems().clear();
    }

}
    @FXML
    void ChooseSubjectFunc(ActionEvent event) {
        clearPage("subject");
        if (SubjectCombo.getValue() == null || SubjectCombo.getValue().isEmpty()) {
            InfoTable.getItems().clear();
        } else {
            CourseCombo.setDisable(false);
            requestCoursesctNames(SubjectCombo.getValue());
        }
    }

    @FXML
    void backFUNC(javafx.scene.input.MouseEvent mouseEven) {
        App.switchScreen("TeacherWindow");
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL url, ResourceBundle resourceBundle) {
        requestSubjectNames();
        DateCol.setCellValueFactory(cellData -> {
            ReadyExams readyExam = cellData.getValue().getReadyExam();
            // Convert the LocalDate to a string using the formatter
            if (readyExam != null) {
                return new SimpleStringProperty(readyExam.getDate());
            } else {
                return new SimpleStringProperty("");
            }
        });

        StudentIdCol.setCellValueFactory(new PropertyValueFactory<>("StudentId"));
        StudentNameCol.setCellValueFactory(new PropertyValueFactory<>("StudentName"));
        FinalGradeCol.setCellValueFactory(new PropertyValueFactory<>("ShowThisGrade"));

        gradeTF.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!gradeTF.getText().isEmpty()) {
                Publish.setVisible(false);
                changeOneBtn.setVisible(true);
            } else {
                Publish.setVisible(true);
                changeOneBtn.setVisible(false);
            }
        });


        InfoTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2 && InfoTable.getSelectionModel().getSelectedItem() != null) {
                    // Double-click detected
                    ScrollP.setVisible(true);
                    Anchor.setVisible(true);
                    gradeTF.setDisable(false);
                    ReasonTF.setDisable(false);
                    changeOneBtn.setDisable(false);
                    Publish.setDisable(false);
                    NoteTF.setDisable(false);
                    ShowCheckExam();
                }
            }
        });
    }

    public void UpdateGrade(int grade) {
        int newG = grade;
        Platform.runLater(() -> {
            GradeLabel.setText("Grade: " + newG + "/" + solvedExam.getFullGrade());
        });

    }


    public void SubjectListFunc(List<String> SubjectsName) {
        if (SubjectsName != null && !SubjectsName.isEmpty()) {
            Platform.runLater(() -> {
                ObservableList<String> SubjectList = FXCollections.observableArrayList(SubjectsName);
                SubjectCombo.setItems(SubjectList);
            });
        }
    }

    public void CoursestListFunc(List<String> CourseName) {
        if (CourseName != null && !CourseName.isEmpty()) {
            Platform.runLater(() -> {
                ObservableList<String> CourseList = FXCollections.observableArrayList(CourseName);
                CourseCombo.setItems(CourseList);
            });
        }
    }

    public void ExamCodesListFunc(List<String> Codes) {
        if (Codes != null && !Codes.isEmpty()) {
            Platform.runLater(() -> {
                ObservableList<String> CodeList = FXCollections.observableArrayList(Codes);
                ExamCombo.setItems(CodeList);
            });
        } else {
            Platform.runLater(() -> {
            List<String> comboList = new ArrayList<>();
            comboList.add("No exmas solved yet");
            ObservableList<String> defultM = FXCollections.observableArrayList(comboList);
            ExamCombo.setItems(defultM);
        });
        }
    }


    public void updateTable() {
    }

    public void ShowInfoList(List<SolvedExams> students) {
        List<SolvedExams> show = new ArrayList<>();
        InfoTable.getItems().clear();
        if (students == null || students.isEmpty()) {
            App.displayFault("There aren't any solved exams yet", "");
        } else {
            for (SolvedExams student : students) {
                if (student.getApproved().equals("no")) {
                    show.add(student);
                }
            }
            ObservableList<SolvedExams> observableList = FXCollections.observableArrayList(show);
            InfoTable.setItems(observableList);
        }
    }

    public void ShowCheckExam() {

        ExamContainer.getChildren().clear();
        solvedExam = InfoTable.getSelectionModel().getSelectedItem();
        readyExam = solvedExam.getReadyExam();

        HightSchool.setAlignment(Pos.CENTER);
        HightSchool.setFont(Font.font("David", FontWeight.BOLD, 25));
        SubjectLabel.setText(readyExam.getSubjectName());
        SubjectLabel.setAlignment(Pos.CENTER);
        SubjectLabel.setFont(Font.font("David", FontWeight.BOLD, 20));
        CourseLabel.setText(readyExam.getCourseName() + " - Exam");
        CourseLabel.setAlignment(Pos.CENTER);
        CourseLabel.setFont(Font.font("David", FontWeight.BOLD, 20));

        if (readyExam.getExam().getNoteForStudent() != null) {
            NoteLabel.setText("Note for student: " + readyExam.getExam().getNoteForStudent());
        }
        if (readyExam.getExam().getNoteForTeacher() != null) {
            NoteLabel1.setText("Note for teacher: " + readyExam.getExam().getNoteForTeacher());
        }
        GradeLabel.setText("Grade: " + solvedExam.getStudentGrade() + "/" + solvedExam.getFullGrade());
        teacherLabel.setText("Teacher Name: ");
        // set the teacher name with first letter as uppercase
        String teacher = readyExam.getPublishingTeacher();
        Character first = Character.toUpperCase(teacher.charAt(0));
        teacher = first + teacher.substring(1).toLowerCase();
        nameLabel.setText(teacher);
        nameLabel.setFont(Font.font("David", FontWeight.BOLD, 15));

        // Set spacing between questions
        ExamContainer.setSpacing(20);

        int id = readyExam.getExam().getId();
        requestQuestionList(id);
    }

    public void DisplayQuestions(List<Questions> questions) {
        Exams exam = readyExam.getExam();
        List<ReadyQuestions> readyQuestions = exam.getReadyQuestionsList();

        List<Answer> studentAnswers = InfoTable.getSelectionModel().getSelectedItem().getAnswers();
        Platform.runLater(() -> {
            ExamContainer.getChildren().clear();
            // Iterate over the questions and dynamically create UI components
            for (int i = 0; i < questions.size(); i++) {
                Questions question = questions.get(i);

                String questionTitle = "Question " + (i + 1) + (" (" + readyQuestions.get(i).getGrade() + " pts)");
                Label questionTitleLabel = new Label(questionTitle);
                questionTitleLabel.setStyle("-fx-underline: true;");
                questionTitleLabel.setFont(Font.font("David", FontWeight.BOLD, 17));
                questionTitleLabel.setTextFill(Color.BLACK); // Set the text color

                VBox questionBox = new VBox(questionTitleLabel);
                questionBox.setSpacing(8);

                Label questionLabel = new Label(question.getQuestion());
                questionLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
                VBox answerContainer = new VBox(); // VBox to hold answer choices
                answerContainer.setSpacing(5); // Set spacing between answer choices

                ToggleGroup toggleGroup = new ToggleGroup();
                List<String> answersList = question.getAllAnswers();
                String sAnswer = "";
                for (Answer A : studentAnswers) {
                    if (A.getQuestionNumber() == i) {
                        sAnswer = A.getChosenAnswer();
                    }
                }
                boolean PrintCorrectAnswer = true;
                // Create radio buttons with labels for each choice
                for (String answer : answersList) {
                    Label answerLabel = new Label(answer);
                    RadioButton radioButton = new RadioButton();
                    radioButton.setToggleGroup(toggleGroup);

                    if (answer.equals(sAnswer)) {
                        radioButton.setSelected(true);
                        if (question.getCorrectAnswer().equals(sAnswer)) {
                            answerLabel.setTextFill(Color.GREEN);
                            PrintCorrectAnswer = false;
                        } else {
                            answerLabel.setTextFill(Color.RED);
                        }

                    }
                    radioButton.setDisable(true);

                    HBox choiceBox = new HBox(radioButton, answerLabel);
                    choiceBox.setSpacing(5); // Set spacing between radio button and label

                    answerContainer.getChildren().add(choiceBox);
                }
                questionBox.getChildren().addAll(questionLabel, answerContainer);
                if (PrintCorrectAnswer) {
                    Label answerLabel = new Label("The Correct answer is : " + question.getCorrectAnswer());
                    answerLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
                    answerLabel.setStyle("-fx-underline: true;");
                    answerLabel.setTextFill(Color.RED);

                    questionBox.getChildren().add(answerLabel);
                } else {
                    Label answerLabel = new Label("Correct !");
                    answerLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
                    answerLabel.setStyle("-fx-underline: true;");
                    answerLabel.setTextFill(Color.GREEN);

                    questionBox.getChildren().add(answerLabel);
                }
                ExamContainer.getChildren().add(questionBox);
            }

            // Create a spacer region with a fixed height of 10 centimeters
            Region spacer1 = new Region();
            spacer1.setPrefHeight(10 * 37.8);  // 37.8 pixels per centimeter
            double preferredHeight = (ExamContainer.getChildren().size() + 1) * 37.8 * 5 + 20.0; // Assuming each question takes 4.8 centimeters.
            Anchor.setPrefHeight(preferredHeight);
        });
    }


    public void requestSubjectNames() {
        String name = SimpleClient.getClient().getUser();
        try {
            Message msg = new Message(name, "#Subject_names_request_for_Checked_exams");
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
            Message msg = new Message(names, "#Courses_names_request_for_Checked_exams");
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void requestExamCodes(String CourseName) {
        List<String> names = new ArrayList<>();
        names.add(SimpleClient.getClient().getUser());
        names.add(CourseName);

        try {
            Message msg = new Message(names, "#Exam_code_request_for_Checked_exams");
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void requestStudentsInfo(String ExamCode) {
        List<String> names = new ArrayList<>();
        names.add(ExamCode);
        names.add(SimpleClient.getClient().getUser());
        try {
            Message msg = new Message(names, "#Students_Info_request_for_checked_exams");
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void requestChangeGrade(String bonus, String reason) {
        List<Object> Info = new ArrayList<>();
        Info.add(InfoTable.getSelectionModel().getSelectedItem());
        Info.add(bonus);
        Info.add(reason);
        Info.add(NoteTF.getText());
        try {
            Message msg = new Message(Info, "#Request_for_updating_grade");
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void requestQuestionList(int id) {
        try {
            Message msg = new Message(id, "#Request_questionlist_for_checked_exam");
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
