/**
 * Sample Skeleton for 'ManagerInformationWindow.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Courses;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.SolvedExams;
import il.cshaifasweng.OCSFMediatorExample.entities.Users;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ManagerInformationController implements Initializable {

    public Users chosenUser;
    String selectionFlag = "Empty";
    private boolean isInitialized = false;
    @FXML // fx:id="Histogram"
    private BarChart<String, Number> Histogram; // Value injected by FXMLLoader
    @FXML // fx:id="xAxis"
    private CategoryAxis xAxis; // Value injected by FXMLLoader
    @FXML // fx:id="yAxis"
    private NumberAxis yAxis; // Value injected by FXMLLoader
    @FXML // fx:id="avgLabel"
    private Label avgLabel; // Value injected by FXMLLoader
    @FXML // fx:id="backBTN"
    private Button backBTN; // Value injected by FXMLLoader
    @FXML // fx:id="selectionComboBox"
    private ComboBox selectionComboBox;
    @FXML // fx:id="medianLabel"
    private Label medianLabel; // Value injected by FXMLLoader
    @FXML
    private TableView<String> tableView; // Value injected by FXMLLoader
    @FXML // fx:id="usernameCol"
    private TableColumn<String, String> usernameCol; // Value injected by FXMLLoader

    @FXML
    void backFunc(ActionEvent event) {
        App.switchScreen("ManagerWindow");
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> comboList = FXCollections.observableArrayList("Teacher", "Student", "Course");

        // Set a CellValueFactory to display the values directly
        usernameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));
//            tableView.getColumns().add(usernameCol);
        Histogram.setAnimated(false);
        xAxis.setAnimated(false);
        yAxis.setAnimated(true);
        // Add items to the ComboBox
        selectionComboBox.setItems(comboList);


        // Add the event listener for the ComboBox
        selectionComboBox.setOnAction(event -> {
            String selectedValue = (String) selectionComboBox.getValue();
            if (selectedValue != null) {
                switch (selectedValue) {
                    case "Teacher":
                        requestTeachersNames();
                        selectionFlag = "Teacher";
                        break;
                    case "Student":
                        requestStudentsNames();
                        selectionFlag = "Student";
                        break;
                    case "Course":
                        requestCourses();
                        selectionFlag = "Course";
                        break;
                    default:
                        // Handle any other cases if needed
                        break;
                }
            }
        });

        tableView.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                // Double-click detected
                String selectedItem = tableView.getSelectionModel().getSelectedItem();
                System.out.println("Selected Item = " + selectionFlag + " " + selectedItem);

                if ("Student".equals(selectionFlag)) {
                    requestStudentExams(selectedItem);
                } else if ("Teacher".equals(selectionFlag)) {
                    requestTeachersExams(selectedItem);
                } else if ("Course".equals(selectionFlag)) {
                    requestCourseExams(selectedItem);
                } else {
                    System.out.println("Got to an undefined selection");
                }
            }
        });
    }


    private void requestStudentExams(String studentsUser) {
        try {
            Message msg = new Message(studentsUser, "#information_manager_student_exams_request");
            SimpleClient.getClient().sendToServer(msg);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void requestTeachersExams(String teachersUser) {
        try {
            Message msg = new Message(teachersUser, "#information_manager_teacher_exams_request");
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void requestCourseExams(String course) {
        try {
            Message msg = new Message(course, "#information_manager_course_exams_request");
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    void requestStudentsNames() {
        try {
            Message msg = new Message(null, "#information_manager_students_names_request");
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    void requestTeachersNames() {
        try {
            Message msg = new Message(null, "#information_manager_teachers_names_request");
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void requestCourses() {
        try {
            Message msg = new Message(null, "#information_manager_courses_request");
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void showNamesOnList(List<Users> studentNames) {

        if (studentNames == null || studentNames.isEmpty()) {
            App.displayError("No names to display");
        } else {
            ObservableList<Users> observableList = FXCollections.observableArrayList(studentNames);
            //System.out.println("Observation List: " + observableList);

            List<String> usernameList = new ArrayList<>();
            // Iterate through the List<Users> and extract usernames
            for (Users user : studentNames) {
                String username = user.getUsername();
                usernameList.add(username);
            }
            ObservableList<String> observableListString = FXCollections.observableArrayList(usernameList);

            System.out.println("Observation List String: " + observableListString);
            Platform.runLater(() -> tableView.setItems(observableListString));

        }
    }

    public void showCoursesOnList(List<Courses> coursesList) {

        if (coursesList == null || coursesList.isEmpty()) {
            App.displayError("No names to display");
        } else {
            ObservableList<Courses> observableList = FXCollections.observableArrayList(coursesList);
            //System.out.println("Observation List: " + observableList);

            List<String> usernameList = new ArrayList<>();
            // Iterate through the List<Users> and extract usernames
            for (Courses course : coursesList) {
                String courseName = course.getCourseName();
                usernameList.add(courseName);
            }
            ObservableList<String> observableListString = FXCollections.observableArrayList(usernameList);

            System.out.println("Observation List String: " + observableListString);
            Platform.runLater(() -> tableView.setItems(observableListString));

        }
    }

    public void showInfo(List<SolvedExams> data) {
        try {
            if (data == null || data.isEmpty()) {
                App.displayFault("Error", "No info to display");
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
}
