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
	private JPasswortField pfPasswort;

	@FXML
	private Button btLogin;

	@FXML
	private void handleButtonLoginAction(ActionEvent event) {
		System.out.println(tfName.getText() + " " + pfPasswort.getText());
	}

}
