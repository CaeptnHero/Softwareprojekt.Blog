package application;
	
import controller.DBConnection;
import controller.MainController;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;

public class Main extends Application {
	
	private MainController mc;
	private DBConnection dbc;
	
	@Override
	public void start(Stage primaryStage) {
		dbc = new DBConnection();
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Main.fxml"));
			BorderPane root = (BorderPane) loader.load();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("../View/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.setTitle("Blog");
			
			mc = loader.getController();
			mc.setDBConnection(dbc);
			
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
