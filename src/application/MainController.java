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
		System.out.println(tfName.getText() + " " + pfPasswort.getText());
	}

	@FXML
	private void handleButtonRegisterAction(ActionEvent event) {
		System.out.println(tfName.getText() + " " + new String(pfPasswort.getPassword()));
	}


}


