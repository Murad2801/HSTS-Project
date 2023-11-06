package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Exams;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.ReadyExams;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SubmissionBoxController {

    @FXML
    private Label WarningLabel;

    @FXML
    private ImageView dragIMG;

    @FXML
    private Label dragLabel;

    @FXML
    private ListView<File> submissionBox;
    @FXML
    private Label timeRemainingLabel;
    @FXML
    private Button SubmitBTN;

    @FXML
    private Label timerLabel;
    @FXML
    private Label fileLink;
    private Timeline timer;
    private int remainingTimeInSeconds;
    private ReadyExams readyExam;

    @FXML
    void openWordFileFunc(MouseEvent event) {
        String projectDirectory = System.getProperty("user.dir");
        String folderPath = projectDirectory + "\\WORD_exams\\" + readyExam.getCourseName() + "_exam.docx";
        fileLink.setStyle("-fx-text-fill: #551A8B;");
        try {
            File folder = new File(folderPath);

            if (Desktop.isDesktopSupported()) {
                // Open the folder with the default file explorer (to avoid Operating Systems errors)
                Desktop.getDesktop().open(folder);
            } else {
                // Desktop is not supported (e.g., in some headless environments)
                System.out.println("Desktop is not supported.");
                App.displayError("Desktop is not Supported !");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getRemainingTimeInSeconds() {
        return remainingTimeInSeconds;
    }

    public void setRemainingTimeInSeconds(int remainingTimeInSeconds) {
        this.remainingTimeInSeconds = remainingTimeInSeconds;
    }

    public ReadyExams getReadyExam() {
        return readyExam;
    }

    public void setReadyExam(ReadyExams readyExam) {
        this.readyExam = readyExam;
    }

    @FXML
    void submitExamFunc(ActionEvent event) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to submit Exam ?");
            alert.setHeaderText("Confirm Submission!");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // User clicked OK, handle the submit action here
                System.out.println("User clicked OK, submitting Exam ...");
                // just for now we go back
                App.switchScreen("availableExams");
                File file = submissionBox.getItems().get(0);
                timer.stop();
                requestSubmission(file);
            }
            SimpleClient.getClient().submissionBoxController = null;
        });
    }


    @FXML
    void initialize() {
        WarningLabel.setAlignment(Pos.CENTER);
        WarningLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        dragLabel.setAlignment(Pos.CENTER);
        dragLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        timeRemainingLabel.setAlignment(Pos.CENTER);

// Enable drag-and-drop for the ImageView and ListView
        dragIMG.setOnDragOver(event -> {
            if (event.getGestureSource() != dragIMG && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        submissionBox.setOnDragOver(event -> {
            if (event.getGestureSource() != submissionBox && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        EventHandler<DragEvent> dragDroppedHandler = event -> {
            Dragboard dragboard = event.getDragboard();
            boolean success = false;
            if (dragboard.hasFiles()) {
                // Handle the dropped files from the ImageView or ListView
                List<File> files = dragboard.getFiles();
                // Save the files to the desired central location
                if (submissionBox.getItems().isEmpty()) {
                    // If the submissionBox is empty, add the dropped file
                    submissionBox.getItems().addAll(files.subList(0, 1));
                } else {
                    // If the submissionBox already contains a file, replace it with the dropped file
                    submissionBox.getItems().set(0, files.get(0));
                }
                success = true;
                SubmitBTN.setDisable(false);
            }
            event.setDropCompleted(success);
            event.consume();
        };

        dragIMG.setOnDragDropped(dragDroppedHandler);
        submissionBox.setOnDragDropped(dragDroppedHandler);
    }

    public void Timer(Exams exam) {
        // set timer for exam
        int durationInMinutes = exam.getDuration();
        remainingTimeInSeconds = durationInMinutes * 60;
        timerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        timerLabel.setTextFill(Color.RED);
        timerLabel.setText(String.format("%02d:%02d", durationInMinutes, 0));
        timerLabel.setAlignment(Pos.CENTER);

        timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            remainingTimeInSeconds--;
            updateTimerLabel();
            if (remainingTimeInSeconds <= 0) {
                timerLabel.setText(String.format("%02d:%02d", remainingTimeInSeconds / 60, remainingTimeInSeconds % 60));
                timer.stop();
                submissionBox.setDisable(true);
                showTimeUpAlert();

            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    // Helper method to update the timer label
    private void updateTimerLabel() {
        int minutes = remainingTimeInSeconds / 60;
        int seconds = remainingTimeInSeconds % 60;
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }

    private void showTimeUpAlert() {
        Platform.runLater(() -> {
            try {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Time's Up!");
                alert.setHeaderText("Time is up for the Exam");
                alert.setContentText("Submission Box is Closed!");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    // User clicked OK, handle the submit action here
                    System.out.println("User clicked OK");
                    List<File> submission = submissionBox.getItems();
                    File sub = null;
                    if (!submission.isEmpty()) {
                        sub = submission.get(0);
                    }

                    requestSubmission(sub);
                    App.switchScreen("availableExams");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void requestSubmission(File file) {
        try {
            List<Object> data = new ArrayList<>();
            data.add(file);
            data.add(readyExam);
            data.add(SimpleClient.getClient().getUser());
            data.add(remainingTimeInSeconds);
            Message msg = new Message(data, "#Save_traditional_exam_request");
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void submissionBoxFunc(ListView.EditEvent<File> fileEditEvent) {
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
