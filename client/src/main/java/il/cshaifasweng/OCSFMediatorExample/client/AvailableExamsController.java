package il.cshaifasweng.OCSFMediatorExample.client;


import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.apache.poi.ooxml.POIXMLProperties;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class AvailableExamsController {

    public ReadyExams selectedExam = null;
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private TableColumn<ReadyExams, String> courseCOL;
    @FXML
    private Label availableLabel;
    @FXML
    private TableColumn<ReadyExams, String> DateCol;

    @FXML
    private TableColumn<ReadyExams, String> ExamIdCol;

    @FXML
    private TableColumn<ReadyExams, String> TeacherCol;
    @FXML
    private Label dateLabel;
    @FXML
    private Label dayLabel;
    @FXML
    private TableColumn<ReadyExams, Integer> durationCOL;
    @FXML
    private Button startExamBTN;
    @FXML
    private TableColumn<ReadyExams, String> subjectCOL;
    @FXML
    private TableColumn<ReadyExams, String> examTypeCOL;
    @FXML
    private TableView<ReadyExams> tableViewExams;
    @FXML
    private Button backBTN;
    @FXML
    private PasswordField passcodeTF;
    @FXML
    private TextField IdTF;
    @FXML
    private Button SubmitCode;

    public String getIdTF() {
        return IdTF.getText();
    }

    @FXML
    void startExamFunc(ActionEvent event) {
        requestValidation();
    }

    @FXML
    void SubmitCodeFunc(ActionEvent event) {
        if (tableViewExams.getSelectionModel().getSelectedItem().getExamType().equals("Traditional Exam")) {
            StartExamAfterValidation(null);
        } else {
            if (selectedExam.getExamPassCode().equals(passcodeTF.getText())) {
                passcodeTF.setDisable(true);
                IdTF.setVisible(true);
                SubmitCode.setDisable(true);
                SubmitCode.setVisible(false);
                startExamBTN.setDisable(false);
                startExamBTN.setVisible(true);
            } else
                App.displayFault("Incorrect Passcode", "Please try again");
        }

    }

    public void StartExamAfterValidation(String result) {
        switch (selectedExam.getExamType()) {
            case "Digital Exam" -> {
                if (IdTF.getText().isEmpty()) {
                    App.displayFault("No Id found", "To start a digital exam you have to enter your Id");
                    return;
                } else if (result == null) {
                    App.displayFault("The Id you entered is incorrect ", "Please try again");
                    return;
                }
                if (selectedExam.getExamPassCode().equals(passcodeTF.getText()))
                    App.switchScreen("DigitalExamStudent");
                else
                    App.displayFault("Incorrect Passcode", "Please try again");

            }
            case "Traditional Exam" -> {
                try {
                    App.switchScreen("TraditionalExamStudent");
                    updateStartedExam();
                    generateWordDocument(selectedExam);
                } catch (IOException e) {
                    e.printStackTrace();
                    App.displayFault("Error", "Failed to generate the exam document.");
                }
            }
        }
    }

    private void generateWordDocument(ReadyExams exam) throws IOException {
        // Create a new Word document
        XWPFDocument document = new XWPFDocument();

        // Set the document properties
        POIXMLProperties.CoreProperties coreProps = document.getProperties().getCoreProperties();
        coreProps.setTitle(exam.getCourseName() + " - Exam");

        // Create a paragraph for the exam title
        XWPFParagraph titleParagraph = document.createParagraph();
        titleParagraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun titleRun = titleParagraph.createRun();
        titleRun.setText("High School Of Haifa");
        titleRun.getUnderline();
        titleRun.setBold(true);
        titleRun.setFontSize(24);

        // Add a blank paragraph for spacing
        document.createParagraph();

        // Iterate over the exam questions and add them to the document
        List<Questions> questions = exam.getExam().getQuestionsList();
        List<ReadyQuestions> readyQuestions = exam.getExam().getReadyQuestionsList();
        for (int i = 0; i < questions.size(); i++) {
            Questions question = questions.get(i);
            // Create a paragraph for the question number and text
            XWPFParagraph questionParagraph = document.createParagraph();
            questionParagraph.setAlignment(ParagraphAlignment.LEFT);
            XWPFRun questionNumberRun = questionParagraph.createRun();
            questionNumberRun.setBold(true);
            questionNumberRun.setText("Question " + (i + 1) + (" (" + readyQuestions.get(i).getGrade() + " pts)"));
            questionNumberRun.setFontSize(17);
            questionNumberRun.getUnderline();
            questionNumberRun.addBreak();
            XWPFRun questionTextRun = questionParagraph.createRun();
            questionTextRun.setText(question.getQuestion());
            questionTextRun.setFontSize(15);
            questionTextRun.addBreak();

            //create the answers
            List<String> answersList = question.getAllAnswers();
            Character answerNum = 'a';
            for (String answer : answersList) {
                XWPFParagraph answerParagraph = document.createParagraph();
                answerParagraph.setAlignment(ParagraphAlignment.LEFT);
                XWPFRun answerNumberRun = answerParagraph.createRun();
                answerNumberRun.setText(answerNum + ") ");
                answerNumberRun.setFontSize(14);
                XWPFRun answerTextRun = answerParagraph.createRun();
                answerTextRun.setText(answer);
                answerTextRun.setFontSize(14);
                answerNum++;
            }
            // Add a blank paragraph for spacing
            document.createParagraph();
        }

        // Save the document to a file

        String projectDirectory = System.getProperty("user.dir");
        String filePath = projectDirectory + "\\WORD_exams\\" + exam.getCourseName() + "_exam.docx";
        try (FileOutputStream out = new FileOutputStream(filePath)) {
            document.write(out);
        }

        // Display a success message
        System.out.format("Exam Generated Successfully!\n The exam questions have been saved to" + filePath);
    }

    @FXML
    void initialize() {
        subjectCOL.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
        courseCOL.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        durationCOL.setCellValueFactory(new PropertyValueFactory<>("duration"));
        examTypeCOL.setCellValueFactory(new PropertyValueFactory<>("examType"));
        DateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        TeacherCol.setCellValueFactory(new PropertyValueFactory<>("publishingTeacher"));
        ExamIdCol.setCellValueFactory(new PropertyValueFactory<>("examIdentifier"));

        passcodeTF.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!passcodeTF.getText().isEmpty()) {
                startExamBTN.setVisible(false);
                SubmitCode.setVisible(true);
                SubmitCode.setDisable(false);
            } else {
                SubmitCode.setVisible(true);
                SubmitCode.setDisable(true);
                startExamBTN.setVisible(false);
            }
        });

        // Get the current day of the week
        LocalDate currentDay = LocalDate.now();
        String dayOfWeek = currentDay.format(DateTimeFormatter.ofPattern("EEEE"));
        dayLabel.setText(dayOfWeek);
        dayLabel.setAlignment(Pos.CENTER);

        // Get the current date
        LocalDate currentDate = LocalDate.now();
        String formattedDate = currentDate.format(DateTimeFormatter.ofPattern("dd MMMM, yyyy"));
        dateLabel.setText(formattedDate);
        dateLabel.setAlignment(Pos.CENTER);
        requestReadyExams();

        tableViewExams.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2 && tableViewExams.getSelectionModel().getSelectedItem() != null) {
                    // Double-click detected
                    selectedExam = tableViewExams.getSelectionModel().getSelectedItem();
                    IdTF.setVisible(false);
                    if (!IdTF.getText().isEmpty()) {
                        IdTF.clear();
                    }
                    if (!passcodeTF.getText().isEmpty()) {
                        passcodeTF.clear();
                    }
                    startExamBTN.setVisible(false);
                    SubmitCode.setVisible(true);
                    passcodeTF.setDisable(false);
                }
            }
        });
        passcodeTF.textProperty().addListener((observable, oldValue, newValue) -> {
            startExamBTN.setDisable((newValue.isEmpty() || selectedExam == null));
        });
        int maxLength = 4;
        TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.length() <= maxLength) {
                return change;
            }
            return null; // Reject the change if it exceeds the maximum length
        });
        passcodeTF.setTextFormatter(textFormatter);
        TextFormatter<String> textFormatter2 = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (!newText.matches("\\d*")) {
                change.setText(""); // Set the change text to an empty string to reject the change
            }
            return change; // Return the modified change
        });
        IdTF.setTextFormatter(textFormatter2);
    }

    @FXML
    void backFunc(ActionEvent event) {
        SimpleClient.getClient().availableExamsController = null;
        App.switchScreen("StudentWindow");
    }

    @FXML
    void refreshFunc(MouseEvent event) {
        passcodeTF.clear();
        selectedExam = null;
        IdTF.setVisible(false);
        requestReadyExams();
    }

    public void requestReadyExams() {
        String name = SimpleClient.getClient().getUser();
        try {
            Message msg = new Message(name, "#get_ready_exams_requests_student");
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void requestValidation() {
        String name = SimpleClient.getClient().getUser();
        int examId = tableViewExams.getSelectionModel().getSelectedItem().getId();
        List<Object> list = new ArrayList<>();
        list.add(name);
        list.add(examId);
        list.add(IdTF.getText());
        try {
            Message msg = new Message(list, "#request_validation_for_availableExams");
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void updateStartedExam() {
        String name = SimpleClient.getClient().getUser();
        int examId = tableViewExams.getSelectionModel().getSelectedItem().getId();
        List<Object> list = new ArrayList<>();
        list.add(name);
        list.add(examId);
        list.add(IdTF.getText());
        try {
            Message msg = new Message(list, "#update_started_exam_Traditional");
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void showReadyExams(List<ReadyExams> data) {
        tableViewExams.getItems().clear();
        if (data != null) {
            if (!data.isEmpty()) {
                Platform.runLater(() -> {
                    availableLabel.setText("Double - click to select an exam");
                    ObservableList<ReadyExams> observableList = FXCollections.observableArrayList(data);
                    tableViewExams.setItems(observableList);
                });
                return;
            }
        }
        Platform.runLater(() -> {
            availableLabel.setText("There are no Available Grades yet");
        });
    }
}