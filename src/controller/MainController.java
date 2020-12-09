package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import model.Nutzer;

import java.io.IOException;

public class MainController {

    private WebViewWindowController wvwc;

    private Nutzer user;

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
    private Label lblLoginStatus;

    @FXML
    private Label lblRegisterStatus;

    @FXML
    private void handleButtonLoginAction(ActionEvent event) {
        if (user != null) {
            setStatus("");
            user = null;
            tfNameLogin.setDisable(false);
            pfPasswortLogin.setDisable(false);
            btLogin.setText("Login");
        }
        else {
            AuthentifizierungsController ac = new AuthentifizierungsController();
            try {
                user = ac.Login(tfNameLogin.getText(), pfPasswortLogin.getText());
                if (user != null) {
                    setStatus("Currently logged in as: " + tfNameLogin.getText() + " (" + user.toString() + ")");
                    btLogin.setText("Log off");
                    tfNameLogin.setDisable(true);
                    pfPasswortLogin.setDisable(true);
                }
                else {
                    setStatus("Login Failed! Wrong Username or Password");
                }
                tfNameLogin.setText("");
                pfPasswortLogin.setText("");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                setStatus("Error in login.");
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleButtonRegisterAction(ActionEvent event) {
        System.out.println(tfNameRegister.getText() + " " + pfPasswortRegister.getText());
        AuthentifizierungsController ac = new AuthentifizierungsController();
        try {
            ac.Registrieren(tfNameRegister.getText(), pfPasswortRegister.getText());
            lblRegisterStatus.setText("Registered new user: " + pfPasswortLogin.getText());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @FXML
    private void handleButtonWebViewAction(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage oldStage = (Stage) source.getScene().getWindow();

        try {
            FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("../view/WebViewWindow.fxml"));
            Parent root = fxmlloader.load();
            wvwc = fxmlloader.getController();
            wvwc.setUser(user);
            Stage stage = new Stage();
            stage.setTitle("WebView");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void setStatus(String txt) {
        lblLoginStatus.setText(txt);
        lblRegisterStatus.setText(txt);
    }
}


