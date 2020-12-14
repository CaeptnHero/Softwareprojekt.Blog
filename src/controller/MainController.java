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
import model.User;

import java.io.IOException;

public class MainController {

    private WebViewWindowController wvwc;

    private User user;

    @FXML
    private TextField tfNameLogin, tfNameRegister, tfTitel;

    @FXML
    private PasswordField pfPasswordLogin, pfPasswordRegister;

    @FXML
    private Button btLogin, btRegister, btnWW;

    @FXML
    private Label lblLoginStatus, lblRegisterStatus;

    @FXML
    private void handleButtonLoginAction(ActionEvent event) {
        if (user != null) {
            setStatus("");
            user = null;
            tfNameLogin.setDisable(false);
            pfPasswordLogin.setDisable(false);
            btLogin.setText("Login");
        }
        else {
            AuthenticationController ac = new AuthenticationController();
            try {
                user = ac.login(tfNameLogin.getText(), pfPasswordLogin.getText());
                if (user != null) {
                    setStatus("Currently logged in as: " + tfNameLogin.getText() + " (" + user.toString() + ")");
                    btLogin.setText("Log off");
                    tfNameLogin.setDisable(true);
                    pfPasswordLogin.setDisable(true);
                }
                else {
                    setStatus("Login Failed! Wrong Username or Password");
                }
                tfNameLogin.setText("");
                pfPasswordLogin.setText("");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                setStatus("Error in login.");
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleButtonRegisterAction(ActionEvent event) {
        System.out.println(tfNameRegister.getText() + " " + pfPasswordRegister.getText());
        AuthenticationController ac = new AuthenticationController();
        try {
            boolean register = ac.register(tfNameRegister.getText(), pfPasswordRegister.getText());
            if (register)
                setStatus("Nutzer registriert: " + tfNameRegister.getText());
            else
                setStatus("Registrierung fehlgeschlagen");
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


