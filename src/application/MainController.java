package application;

import java.awt.Button;
import java.awt.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javax.swing.*;

public class MainController {

	@FXML
	private TextField tfName;

	@FXML
	private JPasswordField pfPasswort;

	@FXML
	private Button btLogin;

	@FXML
	private Button btRegister;

	@FXML
	private void handleButtonLoginAction(ActionEvent event) {
<<<<<<< HEAD
		System.out.println(tfName.getText() + " " + pfPasswort.getText());

	@FXML
	private void handleButtonRegisterAction(ActionEvent event) {
		System.out.println(tfName.getText() + " " + pfRegister.getText());
	}
=======
		System.out.println(tfName.getText() + " " + new String(pfPasswort.getPassword()));
>>>>>>> 5c95a5ba308e52d55718a93ce67834b5e5a33dc4
	}

}
