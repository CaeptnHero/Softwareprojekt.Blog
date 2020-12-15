package controller;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.User;

import java.awt.*;
import java.io.IOException;

public class MainController extends Application {

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

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Main.fxml"));
            BorderPane root = (BorderPane) loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("../view/application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.setTitle("Blog");
            primaryStage.show();

            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            primaryStage.setX((screen.width - primaryStage.getWidth()) / 2);
            primaryStage.setY((screen.height - primaryStage.getHeight()) / 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleButtonLoginAction(ActionEvent event) {
        if (user != null) {
            setStatus("");
            user = null;
            tfNameLogin.setDisable(false);
            pfPasswordLogin.setDisable(false);
            btLogin.setText("Login");
        } else {
            AuthenticationController ac = new AuthenticationController();
            try {
                user = ac.login(tfNameLogin.getText(), pfPasswordLogin.getText());
                if (user != null) {
                    setStatus("Currently logged in as: " + tfNameLogin.getText() + " (" + user.toString() + ")");
                    btLogin.setText("Log off");
                    tfNameLogin.setDisable(true);
                    pfPasswordLogin.setDisable(true);
                } else {
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


