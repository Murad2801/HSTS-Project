/**
 * Sample Skeleton for 'Untitled' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client;
import java.util.Random;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.greenrobot.eventbus.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class StudentController implements Initializable {

    @FXML // fx:id="greetStudent"
    private Label greetStudent; // Value injected by FXMLLoader

    @FXML // fx:id="logOutBTN"
    private Button logOutBTN; // Value injected by FXMLLoader

    @FXML // fx:id="motivationalText"
    private Label motivationalText; // Value injected by FXMLLoader

    @FXML
    private Button availableExamsBTN;

    @FXML // fx:id="studentPane"
    private AnchorPane studentPane; // Value injected by FXMLLoader

    @FXML // fx:id="viewGradesBTN"
    private Button viewGradesBTN; // Value injected by FXMLLoader

    public String getRandomMotivationalSentence() {
        String[] sentences = {
                "Believe you can and you're halfway there.",
                "The only way to do great work is to love what you do.",
                "Don't watch the clock; do what it does. Keep going.",
                "Believe in yourself and all that you are. Know that there is something inside you that is greater than any obstacle.",
                "Success is not final, failure is not fatal: It is the courage to continue that counts.",
                "Your time is limited, don't waste it living someone else's life.",
                "The future belongs to those who believe in the beauty of their dreams.",
                "Hardships often prepare ordinary people for an extraordinary destiny.",
                "The only limit to our realization of tomorrow will be our doubts of today.",
                "The harder you work for something, the greater you'll feel when you achieve it."
        };

        Random random = new Random();
        int index = random.nextInt(sentences.length);
        return sentences[index];
    }

    @FXML
    void clickLogOutBTN(ActionEvent event) {
        SimpleClient.getClient().setUserRole("");
        SimpleClient.getClient().studentController=null;
        requestLogOut();

    }


    @FXML
    void availableExamsFunc(ActionEvent event) {
        App.switchScreen("availableExams");
    }

    @FXML
    void clickViewGradesBTN(ActionEvent event) {
    App.switchScreen("studentGradesList");

    }

    public void setGreetStudent(String s) {
        this.greetStudent.setText(s);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        greetStudent.setText("Welcome, " + SimpleClient.getClient().getUser());
        motivationalText.setText(getRandomMotivationalSentence());
    }
    private void requestLogOut() {

        try {
            Message msg = new Message(SimpleClient.getClient().getUser(), "#request_log_out");
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    }








