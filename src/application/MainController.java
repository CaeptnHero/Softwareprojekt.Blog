package application;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.web.*;

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
	private TextField tfTitel;
	
	@FXML
	private TextArea taText;
	
	@FXML
	private Button btVeroeffentlichen;
	
	@FXML
	private WebView webView;
	
	@FXML
	private Button btnWW;

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
	
	@FXML
	private void handleButtonVeroeffentlichenAction(ActionEvent event) {
		new Blogger().createArticle(tfTitel.getText(), taText.getText()); // FIXME: Blogger aus anderer Quelle bekommen.
	}
	
	@FXML
	private void handleButtonWebViewAction(ActionEvent event) {
		System.out.println(tfNameLogin.getText() + " " + pfPasswortLogin.getText());
		AuthentifizierungsController ac = new AuthentifizierungsController();
		try {
			ac.Login(ac.DatenAusDbLesen(), tfNameLogin.getText(), pfPasswortLogin.getText());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		webView = new WebView();
		WebEngine engine = webView.getEngine();
	}
}


