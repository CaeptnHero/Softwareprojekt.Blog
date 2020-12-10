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

    private Nutzer user;

    @FXML
    private TextField tfNameLogin, tfNameRegister, tfTitel;

    @FXML
    private PasswordField pfPasswortLogin, pfPasswortRegister;

    @FXML
    private Button btLogin, btRegister, btVeroeffentlichen, btnWW;

    @FXML
    private Label lblLoginStatus, lblRegisterStatus;

    @FXML
    private TextArea taText;

    @FXML
    private WebView webView;
    
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
                    setStatus("Logged in as: " + tfNameLogin.getText() + " (" + user.toString() + ")");
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
            WebViewWindowController wvwc = fxmlloader.getController();
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


