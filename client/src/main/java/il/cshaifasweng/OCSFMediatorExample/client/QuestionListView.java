
/**
 * Sample Skeleton for 'questionlistview.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import il.cshaifasweng.OCSFMediatorExample.entities.Questions;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class QuestionListView  implements Initializable {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="CancelBtn"
    private Button CancelBtn; // Value injected by FXMLLoader

    @FXML // fx:id="QuestionListView"
    private ListView<String> QuestionListView; // Value injected by FXMLLoader

    @FXML
    void CancelFunc(ActionEvent event) {
        Stage currentStage = (Stage) CancelBtn.getScene().getWindow();
        currentStage.close();
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL url, ResourceBundle resourceBundle) {
        assert CancelBtn != null : "fx:id=\"CancelBtn\" was not injected: check your FXML file 'questionlistview.fxml'.";
        assert QuestionListView != null : "fx:id=\"QuestionListView\" was not injected: check your FXML file 'questionlistview.fxml'.";
        // Initialize the ListView if needed
        if (QuestionListView != null) {
            QuestionListView.setItems(FXCollections.observableArrayList());
        }

    }

    public void ShowQuestionList(List<String> QuesList){

        Platform.runLater(() -> {
            // Clear the existing items in the ListView
            QuestionListView.getItems().clear();
            // Convert the studentNames list to an ObservableList
            ObservableList<String> items = FXCollections.observableArrayList(QuesList);

            // Add the student names to the ListView
            QuestionListView.setItems(items);
        });
    }

}
