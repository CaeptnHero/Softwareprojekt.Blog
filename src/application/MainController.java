package application;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.*;

public class MainController implements Initializable {

	@FXML
	private TextField tfNameLogin;
	
	@FXML
	private TextField tfNameRegister;

	@FXML
	private PasswordField pfPasswortLogin;
	
	@FXML
	private PasswordField pfPasswortRegister;

	@FXML
	private Button btLogin;

	@FXML
	private Button btRegister;

	@FXML
	private void handleButtonLoginAction(ActionEvent event) {
		
		System.out.println(tfNameLogin.getText() + " " + pfPasswortLogin.getText());
		AuthentifizierungsController ac = new AuthentifizierungsController();
		try {
			ac.Login(ac.DatenAusDbLesen(), tfNameLogin.getText(), pfPasswortLogin.getText());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	private void handleButtonRegisterAction(ActionEvent event) {
		
		System.out.println(tfNameRegister.getText() + " " + pfPasswortRegister.getText());
		AuthentifizierungsController ac = new AuthentifizierungsController();
		try {
			ac.Regestrieren(ac.DatenAusDbLesen(), tfNameRegister.getText(), pfPasswortRegister.getText());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
}


