package il.cshaifasweng.OCSFMediatorExample.client;


import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.ReadyExams;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class PublishedExamsController implements Initializable {

    public ReadyExams selectedExam = null;
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private Button backBTN;
    @FXML
    private TableColumn<ReadyExams, String> courseCOL;
    @FXML
    private TableColumn<ReadyExams, String> DateCol;
    @FXML
    private TableColumn<ReadyExams, String> CodeCol;

    @FXML
    private Label doubleCLK;
    @FXML
    private TableColumn<ReadyExams, Integer> durationCOL;
    @FXML
    private TableColumn<ReadyExams, String> examIdCOL;
    @FXML
    private TableColumn<ReadyExams, String> examTypeCOL;
    @FXML
    private Button requestTimeBTN;
    @FXML
    private TableView<ReadyExams> publishedExamsTable;
    @FXML
    private AnchorPane refreshBTN;
    @FXML
    private TableColumn<ReadyExams, String> subjectCOL;
    @FXML
    private Label titleLabel;
    @FXML
    private TextArea explanationTF;
    @FXML
    private Label extraTimeLabel;
    @FXML
    private TextField extraTimeTF;

    @FXML
    void backFunc(ActionEvent event) {
        App.switchScreen("TeacherWindow");
    }

    @FXML
    void RequestTimeFunc(ActionEvent event) {
        requestTimeBTN.setDisable(true);
        explanationTF.setVisible(false);
        extraTimeTF.setVisible(false);
        extraTimeLabel.setVisible(false);
        RequestExtraTimeFromManager();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        explanationTF.setVisible(false);
        extraTimeTF.setVisible(false);
        extraTimeLabel.setVisible(false);

        examIdCOL.setCellValueFactory(new PropertyValueFactory<>("examIdentifier"));
        subjectCOL.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
        courseCOL.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        durationCOL.setCellValueFactory(new PropertyValueFactory<>("duration"));
        examTypeCOL.setCellValueFactory(new PropertyValueFactory<>("examType"));
        DateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        CodeCol.setCellValueFactory(new PropertyValueFactory<>("examPassCode"));

        // title setting
        String teacherName = SimpleClient.getClient().getUser();
        titleLabel.setText(teacherName + "'s " + titleLabel.getText());
        doubleCLK.setAlignment(Pos.CENTER);

        requestReadyExams();

        publishedExamsTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2 && publishedExamsTable.getSelectionModel().getSelectedItem() != null) {
                    // Double-click detected
                    selectedExam = publishedExamsTable.getSelectionModel().getSelectedItem();
                    extraTimeLabel.setVisible(true);
                    extraTimeTF.setVisible(true);
                    explanationTF.setVisible(true);
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
        extraTimeTF.setTextFormatter(textFormatter2);

        explanationTF.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                int caretPosition = explanationTF.getCaretPosition();
                String text = explanationTF.getText();
                String newText = text.substring(0, caretPosition) + "\n" + text.substring(caretPosition);
                explanationTF.setText(newText);
                explanationTF.positionCaret(caretPosition + 1); // Set the caret position after the newline character
                event.consume(); // Consume the event to prevent the default behavior of adding a new line
            }
        });
        extraTimeTF.textProperty().addListener((observable, oldValue, newValue) -> {
            // Enable the explanationTF if the text field is not empty
            explanationTF.setDisable(newValue.isEmpty());
            if (newValue.isEmpty()) {
                requestTimeBTN.setDisable(true);
            }
        });

        explanationTF.textProperty().addListener((observable, oldValue, newValue) -> {
            // Enable the button if the text field is not empty
            requestTimeBTN.setDisable(newValue.isEmpty());
        });

    }

    @FXML
    void refreshFunc(MouseEvent event) {
        selectedExam = null;
        requestTimeBTN.setDisable(true);
        requestReadyExams();
    }

    public void requestReadyExams() {
        String name = SimpleClient.getClient().getUser();
        try {
            Message msg = new Message(name, "#get_ready_exams_requests_teacher");
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void showReadyExams(List<ReadyExams> data) {
        publishedExamsTable.getItems().clear();
        if (data != null) {
            if (!data.isEmpty()) {
                Platform.runLater(() -> {
                    doubleCLK.setText("Double - click to select an exam");
                    ObservableList<ReadyExams> observableList = FXCollections.observableArrayList(data);
                    publishedExamsTable.setItems(observableList);
                });
                return;
            }
        }
        Platform.runLater(() -> {
            doubleCLK.setText("There are no Available Grades yet");
        });
    }

    public void RequestExtraTimeFromManager() {
        List<Object> data = new ArrayList<>();
        data.add(selectedExam);
        data.add(Integer.parseInt(extraTimeTF.getText()));
        data.add(explanationTF.getText());
        try {
            Message msg = new Message(data, "#save&send_request_to_manager");
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        selectedExam = null;
        explanationTF.clear();
        extraTimeTF.clear();
        publishedExamsTable.getSelectionModel().clearSelection();
    }

}
