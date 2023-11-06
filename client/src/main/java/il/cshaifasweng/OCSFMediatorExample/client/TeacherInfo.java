package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.ReadyExams;
import il.cshaifasweng.OCSFMediatorExample.entities.SolvedExams;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class TeacherInfo implements Initializable {
    @FXML
    private TableView<ReadyExams> ReadyExamsTable;
    @FXML
    private TableColumn<ReadyExams, String> TeacherNameCol;
    @FXML
    private TableColumn<ReadyExams, String> readyExamIdCol;
    @FXML
    private TableView<SolvedExams> GradesTable;
    @FXML
    private TableColumn<SolvedExams, String> completionTimeCol;

    @FXML
    private TableColumn<SolvedExams, String> FinalGradeCol;
    @FXML
    private TableColumn<SolvedExams, String> StudentIdCol;
    @FXML
    private TableColumn<SolvedExams, String> StudentNameCol;
    @FXML
    private BarChart<String, Number> Histogram;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private Label avgLabel;

    @FXML
    private Button backBTN;

    @FXML
    private Label medianLabel;
    @FXML
    private Label doneLabel;

    @FXML
    private Label finnishedLabel;

    @FXML
    private Label startedLabel;
    @FXML
    private Label examTypeLabel;
    @FXML
    private Label typeTraditionalLabel;
    @FXML
    private Label statsHeaderLabel;

    @FXML
    void backFunc(ActionEvent event) {
        App.switchScreen("TeacherWindow");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        TeacherNameCol.setCellValueFactory(new PropertyValueFactory<>("publishingTeacher"));
        readyExamIdCol.setCellValueFactory(new PropertyValueFactory<>("examIdentifier"));

        StudentIdCol.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        StudentNameCol.setCellValueFactory(new PropertyValueFactory<>("StudentName"));
        FinalGradeCol.setCellValueFactory(new PropertyValueFactory<>("ShowThisGrade"));
        completionTimeCol.setCellValueFactory(new PropertyValueFactory<>("completionTime"));
        Histogram.setAnimated(false);
        xAxis.setAnimated(false);
        yAxis.setAnimated(true);
        requestReadyExams();
        ReadyExamsTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2 && ReadyExamsTable.getSelectionModel().getSelectedItem() != null) {
                    ReadyExams selected = ReadyExamsTable.getSelectionModel().getSelectedItem();
                    // Double-click detected
                    if (selected.getExamType().equals("Digital Exam")) {
                        visible(true);

                        ShowHistogram(selected.getSolvedExams());
                        ShowGradesList(selected.getSolvedExams());
                        setLabels(selected);

                    } else {
                        visible(false);
                        setLabels(selected);
                    }
                }
            }
        });

    }

    public void visible(boolean x) {
        Platform.runLater(() -> {
            Histogram.setVisible(x);
            GradesTable.setVisible(x);
            avgLabel.setVisible(x);
            medianLabel.setVisible(x);
            doneLabel.setVisible(true);
            finnishedLabel.setVisible(true);
            startedLabel.setVisible(true);
            examTypeLabel.setVisible(true);
            statsHeaderLabel.setVisible(true);
            typeTraditionalLabel.setVisible(!x);
        });
    }

    public void ShowExamsList(List<ReadyExams> readyExamList) {
        Platform.runLater(() -> {
            ReadyExamsTable.getItems().clear();
            if (readyExamList == null || readyExamList.isEmpty()) {
                System.out.println("no exams are available for you ");
            } else {
                ReadyExamsTable.getItems().clear();
                ObservableList<ReadyExams> observableList = FXCollections.observableArrayList(readyExamList);
                ReadyExamsTable.setItems(observableList);
            }
        });
    }

    public void ShowGradesList(List<SolvedExams> ExamList) {
        Platform.runLater(() -> {
            GradesTable.getItems().clear();
            if (ExamList == null || ExamList.isEmpty()) {
                System.out.println("no grades are available to show ");

            } else {
                GradesTable.getItems().clear();
                ObservableList<SolvedExams> observableList = FXCollections.observableArrayList(ExamList);
                GradesTable.setItems(observableList);

            }
        });
    }

    public void setLabels(ReadyExams readyExam) {
        doneLabel.setText("Didn't finnish in time: " + readyExam.getAutomaticSubmission());
        finnishedLabel.setText("Finished on time: " + readyExam.getFinishedBeforeTime());
        startedLabel.setText("Started The Exam: " + readyExam.getStartedExam());
        examTypeLabel.setText("Exam Type:  " + readyExam.getExamType());
    }


    private void ShowHistogram(List<SolvedExams> data) {
        try {
            if (data == null || data.isEmpty()) {
                Platform.runLater(() -> {
                    Histogram.getData().clear();
                    medianLabel.setText("Median: ");
                    avgLabel.setText("Average: ");

                });
                return;
            }
            int[] decimal = new int[11];

            // Create a new series for the chart
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            for (SolvedExams solvedExam : data) {
                int index = (int) solvedExam.getStudentGrade() / 10;
                decimal[index] += 1;
            }
            for (int i = 0; i < 9; i++) {
                String label = i * 10 + " - " + ((i + 1) * 10 - 1);
                series.getData().add(new XYChart.Data<>(label, decimal[i]));
            }
            String label = String.format("90 - 100");
            series.getData().add(new XYChart.Data<>(label, (decimal[9] + decimal[10])));

// Clear previous data (if any) and add the new series to the chart
            Platform.runLater(() -> {
                Histogram.getData().clear();
                Histogram.getData().add(series);
                yAxis.setLabel("Number of students");
                xAxis.setLabel("Grade Range");
            });

            // Calculate and display the median and average
            List<Integer> grades = data.stream().map(SolvedExams::getStudentGrade).collect(Collectors.toList());

            // Calculate the median
            double median;
            Collections.sort(grades);
            int size = grades.size();
            if (size % 2 == 0) {
                median = (grades.get(size / 2 - 1) + grades.get(size / 2)) / 2.0;
            } else {
                median = grades.get(size / 2);
            }
            Platform.runLater(() -> medianLabel.setText("Median: " + median));

            // Calculate the average
            double sum = grades.stream().mapToDouble(Integer::doubleValue).sum();
            double average = sum / size;
            // Format the average to have only two digits after the decimal point
            DecimalFormat df = new DecimalFormat("#.00");
            Platform.runLater(() -> avgLabel.setText("Average: " + df.format(average)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestReadyExams() {
        try {
            Message msg = new Message(SimpleClient.getClient().getUser(), "#request_readyExams_for_teacherInfo");
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
