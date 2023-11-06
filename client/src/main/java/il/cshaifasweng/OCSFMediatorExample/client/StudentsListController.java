/**
 * Sample Skeleton for 'studentList.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client;

import com.sun.javafx.charts.Legend;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class StudentsListController  implements Initializable {
    @FXML // fx:id="BackBTN"
    private Button BackBTN; // Value injected by FXMLLoader
    @FXML
    public  ListView<String> studentListView;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize the ListView if needed
        if (studentListView != null) {
            studentListView.setItems(FXCollections.observableArrayList());
            requestStudentNames();
            studentListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent click) {
                    if (click.getClickCount() == 2 && studentListView.getSelectionModel().getSelectedItem() != null){
                        try {
                            showStudentGrades(studentListView.getSelectionModel().getSelectedItem());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });

        }
    }

    public  void showNamesOnList(List<String> studentNames) {

        // Clear the existing items in the ListView
        studentListView.getItems().clear();

        // Convert the studentNames list to an Observable List
        ObservableList<String> items = FXCollections.observableArrayList(studentNames);

        // Add the student names to the ListView
        studentListView.setItems(items);
    }
    @FXML
    void requestStudentNames() {
        try {
            Message msg = new Message(null, "#students_names_request");
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    void requestStudentGrades(String name) {
        try {
            Message msg = new Message( name, "#student_grades_request");
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    @FXML
    void showStudentGrades(String name) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("gradesList.fxml"));

            Parent root = loader.load();

            // Get the controller instance from the FXMLLoader
            SimpleClient.gradectr =loader.getController();

            // Call a method on the studentListController to populate the ListView
			requestStudentGrades(name);

//			 Create a new stage for the student list window
            Stage studentListStage = new Stage();
            studentListStage.setTitle(name + "'s Grades");
            studentListStage.setScene(new Scene(root));
            studentListStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goBack(ActionEvent actionEvent) {
        App.switchScreen("TeacherWindow");
    }
}