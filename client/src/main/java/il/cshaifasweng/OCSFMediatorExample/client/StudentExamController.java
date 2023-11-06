package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class StudentExamController implements Initializable {
    //    Exams exam;
    private ReadyExams readyExam;
    private Map<Integer, String> studentAnswers;
    @FXML
    private Label courseLabel;
    @FXML
    private Label dateLabel;
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
    @FXML
    private Label teacherTITLE;
    @FXML
    private Label timeRemainingLabel;
    @FXML
    private Label timerLabel;
    private Timeline timer;
    private int remainingTimeInSeconds;
    private String studentId;

    public ReadyExams getReadyExam() {
        return readyExam;
    }

    // VBox component in the UI to hold the questions

    public void setReadyExam(ReadyExams readyExam) {
        this.readyExam = readyExam;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        studentAnswers = new HashMap<>();
    }

    // Helper method to update the timer label
    private void updateTimerLabel() {
        int minutes = remainingTimeInSeconds / 60;
        int seconds = remainingTimeInSeconds % 60;
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
        remainingTimeInSeconds--; // Decrement remaining time by 1 second
    }

    // Helper method to display time's up alert
    private void showTimeUpAlert() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Time's Up!");
            alert.setHeaderText("Time is up for the Exam");
            alert.setContentText("Press OK to submit the exam.");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // User clicked OK, handle the submit action here
                System.out.println("User clicked OK, submitting Exam ...");
                // just for now we go back
                App.switchScreen("availableExams");
                requestSubmission();
            }
        });
    }

    public int getRemainingTimeInSeconds() {
        return remainingTimeInSeconds;
    }

    public void setRemainingTimeInSeconds(int remainingTimeInSeconds) {
        this.remainingTimeInSeconds = remainingTimeInSeconds;
    }

    public void BuildExam(Exams exam) {
        // set all the correct labels
        highSchoolLabel.setAlignment(Pos.CENTER);
        highSchoolLabel.setFont(Font.font("David", FontWeight.BOLD, 25));
        subjectLabel.setText(exam.getSubject().getSubjectName());
        subjectLabel.setAlignment(Pos.CENTER);
        subjectLabel.setFont(Font.font("David", FontWeight.BOLD, 20));
        courseLabel.setText(exam.getCourse().getCourseName() + " - Exam");
        courseLabel.setAlignment(Pos.CENTER);
        courseLabel.setFont(Font.font("David", FontWeight.BOLD, 20));

        if (exam.getNoteForStudent() != null) {
            notesLabel.setText("notes: " + exam.getNoteForStudent());
        }
        LocalDate date = LocalDate.now();

        // set the date label
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM, yyyy");
        String dateString = date.format(formatter);
        dateLabel.setText(dateString);
        dateLabel.setFont(Font.font("David", FontWeight.NORMAL, 17));
        dateLabel.setAlignment(Pos.CENTER);

        // set the teacher name with first letter as uppercase
        String teacher = exam.getTeacherName();
        Character first = Character.toUpperCase(teacher.charAt(0));
        teacher = first + teacher.substring(1).toLowerCase();
        teacherName.setText(teacher);
        teacherName.setFont(Font.font("David", FontWeight.NORMAL, 17));
        teacherTITLE.setFont(Font.font("David", FontWeight.NORMAL, 17));

        // set notes label
        notesLabel.setFont(Font.font("David", FontWeight.NORMAL, 17));
        timeRemainingLabel.setFont(Font.font("David", FontWeight.NORMAL, 17));
        timeRemainingLabel.setAlignment(Pos.CENTER);

        // set timer for exam
        int durationInMinutes = exam.getDuration();
        remainingTimeInSeconds = durationInMinutes * 60;
        timerLabel.setFont(Font.font("David", FontWeight.BOLD, 20));
        timerLabel.setTextFill(Color.RED);
        timerLabel.setText(String.format("%02d:%02d", durationInMinutes, 0));
        timerLabel.setAlignment(Pos.CENTER);
        timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            updateTimerLabel();

            if (remainingTimeInSeconds <= 0) {
                timerLabel.setText(String.format("%02d:%02d", remainingTimeInSeconds / 60, remainingTimeInSeconds % 60));
                timer.stop();
                showTimeUpAlert();
            }
        }));

        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();

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
                int finalI = i;
                radioButton.setOnAction((actionEvent -> {
                    if (radioButton.isSelected()) {
                        studentAnswers.put(finalI, answer);
                    }
                }

                ));

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


        submitButton.setOnAction(event -> {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to submit your answers ?");
                alert.setHeaderText("Confirm Submission");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    // User clicked OK, handle the submit action here
                    System.out.println("User clicked OK, submitting Exam ...");
                    // just for now we go back
                    timer.stop();
                    requestSubmission();
                    App.switchScreen("availableExams");
                    SimpleClient.getClient().studentExamController = null;

                }
            });

        });
        // Add the submit button to the exam container
        examContainer.getChildren().addAll(spacer1, submitButton, spacer2);
    }

    // Other methods and functionalities for the exam window
    public void requestSubmission() {
        List<Object> data = new ArrayList<>();
        data.add(readyExam);
        data.add(remainingTimeInSeconds);
        data.add(studentAnswers);
        data.add(studentId);
        data.add(SimpleClient.getClient().getUser());

        try {
            Message msg = new Message(data, "#save_solved_exam_request");
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void addExtraTime(int extraTime, ReadyExams exam) {
        this.remainingTimeInSeconds += 60 * extraTime;
        int newDuration = exam.getDuration() + extraTime;
        exam.setUpdatedDuration(newDuration);
    }

    public void updateExam() {
        try {
            List<Object> data = new ArrayList<>();
            data.add(readyExam);
            data.add(SimpleClient.getClient().getUser());
            Message msg = new Message(data, "#update_that_student_solved_exam");
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}