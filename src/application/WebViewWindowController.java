package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.*;
import javafx.scene.web.*;

public class WebViewWindowController implements Initializable {
	
	private DBConnection dbc;
	
	@FXML
	private WebView webView = new WebView();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		WebEngine engine = webView.getEngine();
		engine.load("http://www.i5ucc.me");
	}
	
	public void setDBConnection(DBConnection dbc) {
		this.dbc = dbc;
	}
}
