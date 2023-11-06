package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Exams;
import il.cshaifasweng.OCSFMediatorExample.entities.Questions;
import il.cshaifasweng.OCSFMediatorExample.entities.ReadyQuestions;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class TeacherExamPreviewController implements Initializable {

    Exams exam;
    private boolean manager = false;

    @FXML
    private Label courseLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Label teacherLabel;

    @FXML
    private VBox examContainer;

    @FXML
    private Label highSchoolLabel;

    @FXML
    private Label subjectLabel;

    @FXML
    private Label teacherName;
    @FXML
    private Label notesLabel;

    // VBox component in the UI to hold the questions

    @FXML
    public void backFUNC(javafx.scene.input.MouseEvent mouseEvent) {
        if (manager)
            App.switchScreen("ManagerViewExamsWindow");
        else
            App.switchScreen("ExamPool");
    }


    public void setManager(boolean manager) {
        this.manager = manager;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set the title of the exam
        // set all the correct labels

        // Set spacing between questions


    }

    public void BuildExam(Exams exam_) {
        this.exam = exam_;

        // set all the correct labels
        highSchoolLabel.setAlignment(Pos.CENTER);
        highSchoolLabel.setFont(Font.font("David", FontWeight.BOLD, 25));
        subjectLabel.setText(exam.getSubject().getSubjectName());
        subjectLabel.setAlignment(Pos.CENTER);
        subjectLabel.setFont(Font.font("David", FontWeight.BOLD, 20));
        courseLabel.setText(exam.getCourse().getCourseName() + " - Exam");
        courseLabel.setAlignment(Pos.CENTER);
        courseLabel.setFont(Font.font("David", FontWeight.BOLD, 20));

        notesLabel.setText("notes: " + exam.getNoteForStudent());
        teacherLabel.setText("note for teacher: " + exam.getNoteForTeacher());
        LocalDate date = LocalDate.now();

        // set the date label
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM, yyyy");
        String dateString = date.format(formatter);
        dateLabel.setText(dateString);
        dateLabel.setFont(Font.font("David", FontWeight.BOLD, 15));
        dateLabel.setAlignment(Pos.CENTER);

        // set the teacher name with first letter as uppercase
        String teacher = exam.getTeacherName();
        Character first = Character.toUpperCase(teacher.charAt(0));
        teacher = first + teacher.substring(1).toLowerCase();
        teacherName.setText(teacher);
        teacherName.setFont(Font.font("David", FontWeight.BOLD, 15));

        // Set spacing between questions
        examContainer.setSpacing(20);

        List<Questions> questions = exam.getQuestionsList();
        List<ReadyQuestions> readyQuestions = exam.getReadyQuestionsList();

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


            // Create radio buttons with labels for each choice
            for (String answer : answersList) {
                Label answerLabel = new Label(answer);
                RadioButton radioButton = new RadioButton();
                radioButton.setToggleGroup(toggleGroup);

                if (question.getCorrectAnswer().equals(answer)) {
                    answerLabel.setTextFill(Color.GREEN);
                    radioButton.setSelected(true);
                }
                radioButton.setDisable(true);


                HBox choiceBox = new HBox(radioButton, answerLabel);
                choiceBox.setSpacing(5); // Set spacing between radio button and label

                answerContainer.getChildren().add(choiceBox);
            }

            questionBox.getChildren().addAll(questionLabel, answerContainer);
            examContainer.getChildren().add(questionBox);
        }
        // Create a spacer region with a fixed height of 10 centimeters
        Region spacer1 = new Region();
        spacer1.setPrefHeight(10 * 37.8);  // 37.8 pixels per centimeter

        // Create a spacer region after the submit button with a fixed height of 6 centimeters
        Region spacer2 = new Region();
        spacer2.setPrefHeight(6 * 37.8);  // 37.8 pixels per centimeter


        // Create the "Submit Exam" button
        Button submitButton = new Button("Submit Exam");
        submitButton.setStyle("-fx-background-color: #1832dd; -fx-text-fill: white;");
        submitButton.setDisable(true);


        submitButton.setOnAction(event -> {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to submit your answers ?");
                alert.setHeaderText("Confirm Submission");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    // User clicked OK, handle the submit action here
                    System.out.println("User clicked OK, submitting Exam ...");
                    requestSubmission();
                }
            });
        });
        // Add the submit button to the exam container
        examContainer.getChildren().addAll(spacer1, submitButton, spacer2);

    }

    // Other methods and functionalities for the exam window
    public void requestSubmission() {

    }
}
