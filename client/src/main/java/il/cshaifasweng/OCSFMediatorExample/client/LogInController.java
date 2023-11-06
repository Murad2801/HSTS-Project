/**
 * Sample Skeleton for 'Untitled' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LogInController {
    @FXML // fx:id="logInPane"
    private static AnchorPane logInPane; // Value injected by FXMLLoader
    private String userRole;
    @FXML // fx:id="logInBTN"
    private Button logInBTN; // Value injected by FXMLLoader
    @FXML
    private PasswordField passwordText;

    @FXML // fx:id="usernameText"
    private TextField usernameText; // Value injected by FXMLLoader
    private SimpleClient client;

    public SimpleClient getClient() {
        return client;
    }

    @FXML
    void clickLogInBTN(ActionEvent event) {

        String username = null;
        String password = null;
        if (usernameText.getText().isEmpty()) {
        } else {
            username = usernameText.getText();
        }

        if (passwordText.getText().isEmpty()) {
        } else {
            password = passwordText.getText();
        }

        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            // should we just send a warning of "incorrect..."
            App.displayError("Error! empty username or password, Please try again");
        } else {
            requestValidateUser(username, password);

        }

    }

    public void MultipleLogInsCheck(String Role) {
        userRole = Role;
        requestOnlineUser();
    }

    public void CheckIfLoginApproved(String check) {
        if (check != null) {
            App.displayFault("Duplicate login attempts ! ", "Please log out from your other window/device.\n" +
                    "Only one connection is allowed at a time.");
            passwordText.clear();
            usernameText.clear();
        } else {
            if (userRole.equals("teacher")) {
                App.switchScreen("TeacherWindow");
                SimpleClient.getClient().setUserRole("teacher");
            }
            if (userRole.equals("student")) {
                App.switchScreen("StudentWindow");
                SimpleClient.getClient().setUserRole("student");
            }
            if (userRole.equals("manager")) {
                App.switchScreen("ManagerWindow");
                SimpleClient.getClient().setUserRole("manager");
            }
        }
    }

    private void requestValidateUser(String username, String password) {
        List<Object> usernameAndPassword = new ArrayList<>();
        usernameAndPassword.add(username);
        usernameAndPassword.add(password);

        try {
            Message msg = new Message(usernameAndPassword, "#validate_user_request");
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void requestOnlineUser() {

        try {
            Message msg = new Message(SimpleClient.getClient().getUser(), "#Check_if_user_Online");
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
