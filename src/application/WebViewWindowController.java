package application;

import java.io.File;
import java.net.MalformedURLException;
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
		File f = new File("www\\index.html");
		WebEngine engine = webView.getEngine();
		try {
			engine.load(f.toURI().toURL().toString());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setDBConnection(DBConnection dbc) {
		this.dbc = dbc;
	}
}
