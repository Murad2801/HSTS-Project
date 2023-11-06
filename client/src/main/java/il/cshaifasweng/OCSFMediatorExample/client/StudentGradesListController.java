/**
 * Sample Skeleton for 'studentGradesList.fxml' Controller Class
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
import java.util.List;
import java.util.ResourceBundle;

public class StudentGradesListController implements Initializable {
    static SolvedExams solvedExam;
    static ReadyExams readyExam;


    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="Anchor"
    private AnchorPane Anchor; // Value injected by FXMLLoader


     @FXML
     private Label doubleLabel;

    private ComboBox<String> ChooseCourse; // Value injected by FXMLLoader

    @FXML // fx:id="CourseCal"
    private TableColumn<SolvedExams, String> CourseCal; // Value injected by FXMLLoader

    @FXML // fx:id="CourseLabel"
    private Label CourseLabel; // Value injected by FXMLLoader

    @FXML // fx:id="ExamContainer"
    private VBox ExamContainer; // Value injected by FXMLLoader

    @FXML // fx:id="GradeCal"
    private TableColumn<SolvedExams, Integer> GradeCal; // Value injected by FXMLLoader


    @FXML
    private TableColumn<SolvedExams, String> dateCal;

    @FXML // fx:id="GradeLabel"
    private Label GradeLabel; // Value injected by FXMLLoader

    @FXML // fx:id="HightSchool"
    private Label HightSchool; // Value injected by FXMLLoader

    @FXML // fx:id="IdCal"
    private TableColumn<SolvedExams, String> IdCal; // Value injected by FXMLLoader

    @FXML // fx:id="InfoTable"
    private TableView<SolvedExams> InfoTable; // Value injected by FXMLLoader

    @FXML // fx:id="NoteLabel"
    private Label NoteLabel; // Value injected by FXMLLoader

    @FXML // fx:id="NoteLabel1"
    private Label NoteLabel1; // Value injected by FXMLLoader

    @FXML // fx:id="ScrollP"
    private ScrollPane ScrollP; // Value injected by FXMLLoader

    @FXML // fx:id="SubjectLabel"
    private Label SubjectLabel; // Value injected by FXMLLoader

    @FXML
    private Label commentLabel;
    @FXML
    private ImageView refreshBtn;

    @FXML // fx:id="TeacherCal"
    private TableColumn<SolvedExams, String> TeacherCal; // Value injected by FXMLLoader

    @FXML // fx:id="backBTN"
    private Button backBTN; // Value injected by FXMLLoader

    @FXML // fx:id="nameLabel"
    private Label nameLabel; // Value injected by FXMLLoader

    @FXML
    void ChooseCourseFunc(ActionEvent event) {

    }
    public void refreshF(javafx.scene.input.MouseEvent mouseEvent) {
        ExamContainer.getChildren().clear();
        nameLabel.setText("");
        commentLabel.setText("");
        NoteLabel.setText("");
        NoteLabel1.setText("");
        GradeLabel.setText("");
        CourseLabel.setText("");
        HightSchool.setText("");
        SubjectLabel.setText("");
        requestInfoForCheckedExams();

    }


    @FXML
    void backFunc(ActionEvent event) {
        App.switchScreen("StudentWindow");
    }


    @FXML // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL url, ResourceBundle resourceBundle) {

        TeacherCal.setCellValueFactory(cellData -> {
            ReadyExams readyExam = cellData.getValue().getReadyExam();
            if (readyExam != null) {
                return new SimpleStringProperty(readyExam.getPublishingTeacher());
            } else {
                return new SimpleStringProperty("");
            }
        });

        IdCal.setCellValueFactory(cellData -> {
            ReadyExams readyExam = cellData.getValue().getReadyExam();
            if (readyExam != null) {
                return new SimpleStringProperty(readyExam.getExamIdentifier());
            } else {
                return new SimpleStringProperty("");
            }
        });
        CourseCal.setCellValueFactory(cellData -> {
            ReadyExams readyExam = cellData.getValue().getReadyExam();
            if (readyExam != null) {
                return new SimpleStringProperty(readyExam.getCourseName());
            } else {
                return new SimpleStringProperty("");
            }
        });
        dateCal.setCellValueFactory(cellData -> {
            ReadyExams readyExam = cellData.getValue().getReadyExam();
            if (readyExam != null) {
                return new SimpleStringProperty(readyExam.getDate());
            } else {
                return new SimpleStringProperty("");
            }
        });

        GradeCal.setCellValueFactory(new PropertyValueFactory<>("studentGrade"));

        InfoTable.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2 && InfoTable.getSelectionModel().getSelectedItem() != null) {
                    // Double-click detected
                    ScrollP.setVisible(true);
                    Anchor.setVisible(true);
                    ShowCheckExam();
                }
            }
        });

        requestInfoForCheckedExams();
    }

    public void ShowInfoList(List<SolvedExams> Info) {
        InfoTable.getItems().clear();
        if (Info != null) {
            if (!Info.isEmpty()) {
                Platform.runLater(() -> {
                    doubleLabel.setText("Double - click to select an exam");
                    ObservableList<SolvedExams> observableList = FXCollections.observableArrayList(Info);
                    InfoTable.setItems(observableList);
                });
                return;
            }
        }
        Platform.runLater(() -> {
            doubleLabel.setText("There are no Available Grades yet");
        });
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
            NoteLabel.setText("note for student: " + readyExam.getExam().getNoteForStudent());
        }
        if (solvedExam.getUpdateGradeReason() != null) {
            if (!solvedExam.getUpdateGradeReason().equals("No Changes needed"))
                NoteLabel1.setText("Reason for changing the garde: " + solvedExam.getUpdateGradeReason());
        }
        GradeLabel.setText("Grade: " + solvedExam.getStudentGrade() + "/" + solvedExam.getFullGrade());

        // set the teacher name with first letter as uppercase
        String teacher = readyExam.getPublishingTeacher();
        Character first = Character.toUpperCase(teacher.charAt(0));
        teacher = first + teacher.substring(1).toLowerCase();
        nameLabel.setText("Teacher Name: "+teacher);
        nameLabel.setFont(Font.font("David", FontWeight.BOLD, 15));

        if (solvedExam.getNoteForStudent() != null) {
            commentLabel.setText("Comments:" + solvedExam.getNoteForStudent());
        }

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


    public void requestInfoForCheckedExams() {
        String user = SimpleClient.getClient().getUser();
        try {
            Message msg = new Message(user, "#Request_Info_for_checked_exam_studentWindow");
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void requestQuestionList(int id) {
        try {
            Message msg = new Message(id, "#Request_questionlist_for_grades_list");
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



}