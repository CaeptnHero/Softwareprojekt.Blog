package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.*;

public class MainController implements Initializable {

	@FXML
	private TextField tfName;

	@FXML
	private PasswordField pfPasswort;

	@FXML
	private Button btLogin;

	@FXML
	private Button btRegister;

	@FXML
	private void handleButtonLoginAction(ActionEvent event) {
		System.out.println(tfName.getText() + " " + pfPasswort.getText());
	}

	@FXML
	private void handleButtonRegisterAction(ActionEvent event) {
		System.out.println(tfName.getText() + " " + pfPasswort.getText());
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
}


