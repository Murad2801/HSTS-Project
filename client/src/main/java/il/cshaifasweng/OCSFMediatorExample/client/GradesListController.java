package il.cshaifasweng.OCSFMediatorExample.client;

/**
 * Sample Skeleton for 'gradesList.fxml' Controller Class
 */

import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GradesListController implements Initializable {
    ObservableList<String> gradesDropdownList = FXCollections.observableArrayList("grade1", "grade2", "grade3", "grade4", "grade5");
    @FXML // fx:id="editLabel"
    private Label editLabel; // Value injected by FXMLLoader
    @FXML // fx:id="gradesListView"
    ListView<String> gradesListView; // Value injected by FXMLLoader
    @FXML   // fx:id="editGradeBox"
    private ComboBox<String> editGradeBox; // Value injected by FXMLLoader
    @FXML // fx:id="changeGradeBTN"
    private Button changeGradeBTN; // Value injected by FXMLLoader

    @FXML // fx:id="newGradeText"
    private AnchorPane newGradeText; // Value injected by FXMLLoader
    @FXML // fx:id="textF"
    private TextField textF; // Value injected by FXMLLoader

    public String Name;
    int newGrade;

    @FXML
    public void clickChangeBTN(javafx.event.ActionEvent actionEvent) {
        if (textF.getText().isEmpty()) {
        } else {
            requestChangeGrade(textF.getText());
        }
    }

    public void chooseFromList(javafx.event.ActionEvent actionEvent) {

    }


    private void requestChangeGrade(String grade) { // ram, my boy, did i choose the right parameter?
        List<Object> nameAndGradeAndChosenGrade = new ArrayList<>();
        nameAndGradeAndChosenGrade.add(Name);
        nameAndGradeAndChosenGrade.add(grade);
        nameAndGradeAndChosenGrade.add(editGradeBox.getValue()); // what grade was chosen

        try {
            Message msg = new Message(nameAndGradeAndChosenGrade, "#change_grade_request");
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize the ListView if needed
        if (gradesListView != null) {
            gradesListView.setItems(FXCollections.observableArrayList());

            editGradeBox.setValue("Choose a grade");
            editGradeBox.setItems(gradesDropdownList);
        }
    }

    public void showGradesOnList(List<String> gradesList) {
        Platform.runLater(() -> {
        // Clear the existing items in the ListView
        gradesListView.getItems().clear();
        Name = gradesList.get(gradesList.size() - 1);
        gradesList.remove(gradesList.size() - 1);
        // Convert the studentNames list to an ObservableList
        ObservableList<String> items = FXCollections.observableArrayList(gradesList);

        // Add the student names to the ListView
        gradesListView.setItems(items);
        });
    }
}



