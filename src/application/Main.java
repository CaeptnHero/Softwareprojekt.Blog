package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.awt.*;

public class Main extends Application {

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
}
