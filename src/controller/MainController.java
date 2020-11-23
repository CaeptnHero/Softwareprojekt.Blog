package controller;

import model.Blogger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
	
	private DBConnection dbc;
	
	private WebViewWindowController mc;

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
			ac.Login(tfNameLogin.getText(), pfPasswortLogin.getText());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	private void handleButtonRegisterAction(ActionEvent event) {
		System.out.println(tfNameRegister.getText() + " " + pfPasswortRegister.getText());
		AuthentifizierungsController ac = new AuthentifizierungsController();
		try {
			ac.Registrieren(tfNameRegister.getText(), pfPasswortRegister.getText());
		} catch (Exception e) {
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
		Node source = (Node) event.getSource();
		Stage oldStage = (Stage) source.getScene().getWindow();
		//oldStage.close();
		try {
			FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("../view/WebViewWindow.fxml"));
			mc = (WebViewWindowController) fxmlloader.getController();
			Parent root = (Parent) fxmlloader.load();
			Stage stage = new Stage();
			stage.setTitle("WebView");
			stage.setScene(new Scene(root));
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setDBConnection(DBConnection dbc) {
		this.dbc = dbc;
	}
}


