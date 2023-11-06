/**
 * Sample Skeleton for 'TeacherWindow.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TeacherController implements Initializable {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="CreateEBtn"
    private Button CreateEBtn; // Value injected by FXMLLoader

    @FXML // fx:id="CreateQBtn"
    private Button CreateQBtn; // Value injected by FXMLLoader

    @FXML // fx:id="UpdateBtn"
    private Button UpdateBtn; // Value injected by FXMLLoader

    @FXML // fx:id="examCheckBTN"
    private Button examCheckBTN; // Value injected by FXMLLoader

    @FXML // fx:id="TeacherGreeting"
    private Label TeacherGreeting; // Value injected by FXMLLoader

    @FXML // fx:id="informationBTN"
    private Button informationBTN; // Value injected by FXMLLoader

    @FXML // fx:id="logOutBTN"
    private Button logOutBTN; // Value injected by FXMLLoader

    @FXML
    private Button publishedExamsBTN;

    @FXML // fx:id="teacherPane"
    private AnchorPane teacherPane; // Value injected by FXMLLoader

    @FXML
    public void ExamPoolFunc(ActionEvent actionEvent) {
        App.switchScreen("ExamPool");
    }

    @FXML
    void CreateQuesFunc(ActionEvent event) {
        App.switchScreen("createQuestion");

    }

    @FXML
    void UpdateFunc(ActionEvent event) {

    }

    @FXML
    void examCheck(ActionEvent event) {
        App.switchScreen("checkedexams");
    }

    @FXML
    void getInformation(ActionEvent event) {
        App.switchScreen("teacherInfo");

    }

    @FXML
    void logOut(ActionEvent event) {
        SimpleClient.getClient().setUserRole("");
        SimpleClient.getClient().publishedExamsController = null;
        requestLogOut();
    }

    @FXML
    void publishedExamsFunc(ActionEvent event) {
        App.switchScreen("PublishedExamsWindow");
    }

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL url, ResourceBundle resourceBundle) {
        assert CreateEBtn != null : "fx:id=\"CreateEBtn\" was not injected: check your FXML file 'TeacherWindow.fxml'.";
        assert CreateQBtn != null : "fx:id=\"CreateQBtn\" was not injected: check your FXML file 'TeacherWindow.fxml'.";
        assert UpdateBtn != null : "fx:id=\"UpdateBtn\" was not injected: check your FXML file 'TeacherWindow.fxml'.";
        assert examCheckBTN != null : "fx:id=\"examCheckBTN\" was not injected: check your FXML file 'TeacherWindow.fxml'.";
        assert TeacherGreeting != null : "fx:id=\"greetTeacher\" was not injected: check your FXML file 'TeacherWindow.fxml'.";
        assert informationBTN != null : "fx:id=\"informationBTN\" was not injected: check your FXML file 'TeacherWindow.fxml'.";
        assert logOutBTN != null : "fx:id=\"logOutBTN\" was not injected: check your FXML file 'TeacherWindow.fxml'.";
        assert publishedExamsBTN != null : "fx:id=\"startAnExamBTN\" was not injected: check your FXML file 'TeacherWindow.fxml'.";
        assert teacherPane != null : "fx:id=\"teacherPane\" was not injected: check your FXML file 'TeacherWindow.fxml'.";

        TeacherGreeting.setText("Welcome, " + SimpleClient.getClient().getUser());
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
