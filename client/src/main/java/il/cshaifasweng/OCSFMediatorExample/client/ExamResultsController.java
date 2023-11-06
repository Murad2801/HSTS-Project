/**
 * Sample Skeleton for 'ExamResultsWindow.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.SolvedExams;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ExamResultsController implements Initializable {

    @FXML // fx:id="TableView"
    private TableView<SolvedExams> TableView; // Value injected by FXMLLoader

    @FXML // fx:id="backBtn"
    private Button backBtn; // Value injected by FXMLLoader

    @FXML // fx:id="examLabel"
    private Label examLabel; // Value injected by FXMLLoader

    @FXML // fx:id="examLabel2"
    private Label examLabel2; // Value injected by FXMLLoader

    @FXML // fx:id="gradeCol"
    private TableColumn<SolvedExams, String> gradeCol; // Value injected by FXMLLoader

    @FXML // fx:id="nameCol"
    private TableColumn<SolvedExams, Integer> nameCol; // Value injected by FXMLLoader

    @FXML
    void Backfunc(ActionEvent event) {
        App.switchScreen("ManagerViewExamsWindow");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("StudentName"));
        gradeCol.setCellValueFactory(new PropertyValueFactory<>("ShowThisGrade"));
    }

    public void showResultsOnTable(List<SolvedExams> solvedExams) {
        try {
            TableView.getItems().clear();
            if (solvedExams != null) {
                if (!solvedExams.isEmpty()) {
                    TableView.getItems().clear();
                    ObservableList<SolvedExams> observableList = FXCollections.observableArrayList(solvedExams);
                    TableView.setItems(observableList);
                    return;
                }
            }
            App.displayFault("No results are available", "please return at a later date to view any changes");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
