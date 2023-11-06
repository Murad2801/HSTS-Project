/**
 * Sample Skeleton for 'primary.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class PrimaryController {

	@FXML // fx:id="PrototypeTF"
	private TextField PrototypeTF; // Value injected by FXMLLoader

	@FXML // fx:id="showStudentsBTN"
	private Button showStudentsBTN; // Value injected by FXMLLoader

	@FXML
	void showStudentsList(ActionEvent event) throws IOException {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("studentList.fxml"));
			Parent root = loader.load();

			// Get the controller instance from the FXMLLoader
			SimpleClient.getClient().stdctr =loader.getController();

//			 Create a new stage for the student list window
			Stage studentListStage = new Stage();
			studentListStage.setTitle("Student List");
			studentListStage.setScene(new Scene(root));
			studentListStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
