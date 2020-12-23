package controller;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

/**
 * Controller-Klasse, welche das Hauptfenster verwaltet.
 */
public class MainController extends Application {

    private WebViewWindowController wvwc;
    private User currUser;

    @FXML
    private TextField tfNameLogin, tfNameRegister;
    @FXML
    private PasswordField pfPasswordLogin, pfPasswordRegister;
    @FXML
    private Button btLogin;
    @FXML
    private Label lblLoginStatus, lblRegisterStatus;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * laed die FXML-Datei des Hauptfensters und zeigt ihn auf der mitte des Bildschirms an
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Main.fxml"));
            BorderPane root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("../view/application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.setTitle("Blog");
            primaryStage.show();
            primaryStage.setOnCloseRequest(windowEvent -> System.exit(0));

            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            primaryStage.setX((screen.width - primaryStage.getWidth()) / 2);
            primaryStage.setY((screen.height - primaryStage.getHeight()) / 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Setzt einen String in zwei Labels im User Interface um einen Status anzuzeigen
     *
     * @param message anzuzeigende nachricht
     */
    private void setStatus(String message) {
        lblLoginStatus.setText(message);
        lblRegisterStatus.setText(message);
    }

    /**
     * TODO: FINISH JAVADOC COMMENT
     *
     * @param event
     */
    @FXML
    private void handleButtonLoginAction(ActionEvent event) {
        if (currUser != null) {
            setStatus("");
            currUser = null;
            tfNameLogin.setDisable(false);
            pfPasswordLogin.setDisable(false);
            btLogin.setText("Login");
        } else {
            try {
                currUser = AuthenticationController.login(tfNameLogin.getText(), pfPasswordLogin.getText());
                if (currUser != null) {
                    setStatus("Currently logged in as: " + currUser);
                    btLogin.setText("Log off");
                    tfNameLogin.setDisable(true);
                    pfPasswordLogin.setDisable(true);
                    tfNameLogin.setText("");
                    pfPasswordLogin.setText("");
                } else {
                    setStatus("Login Failed! Wrong Username or Password");
                }
            } catch (Exception e) {
                setStatus("Error in login.");
                e.printStackTrace();
            }
        }
    }

    /**
     * TODO: FINISH JAVADOC COMMENT
     *
     * @param event
     */
    @FXML
    private void handleButtonRegisterAction(ActionEvent event) {
        System.out.println(tfNameRegister.getText() + " " + pfPasswordRegister.getText());
        try {
            boolean register = AuthenticationController.register(tfNameRegister.getText(), pfPasswordRegister.getText());
            if (register) {
                setStatus("Nutzer registriert: " + tfNameRegister.getText());
                tfNameRegister.setText("");
                pfPasswordRegister.setText("");
            } else {
                setStatus("Registrierung fehlgeschlagen");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Laed die FMXL des Webviewfensters, setzt den Nutzer fuer die webview und zeigt das webviewfenster an
     *
     * @param event eventobjekt des "Open WebView" Buttons
     */
    @FXML
    private void handleButtonWebViewAction(ActionEvent event) {
        try {
            FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("../view/WebViewWindow.fxml"));
            Parent root = fxmlloader.load();
            wvwc = fxmlloader.getController();
            wvwc.setUser(currUser);
            Stage stage = new Stage();
            stage.setTitle("WebView");
            stage.setScene(new Scene(root));
            stage.show();

            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            stage.setX((screen.width - stage.getWidth()) / 2);
            stage.setY((screen.height - stage.getHeight()) / 2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
