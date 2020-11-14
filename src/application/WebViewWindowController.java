package application;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.*;
import javafx.concurrent.Worker;
import javafx.fxml.*;
import javafx.scene.web.*;
import netscape.javascript.JSObject;

public class WebViewWindowController implements Initializable {

	private DBConnection dbc;
	private WebEngine webEngine;
	private Bridge jsbridge;

	@FXML
	private WebView webView = new WebView();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

		webEngine = webView.getEngine();
		webEngine.setJavaScriptEnabled(true);
		webEngine.getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
			if (newState == Worker.State.SUCCEEDED) {
				jsbridge = new Bridge();
				JSObject jso = (JSObject) webEngine.executeScript("window");
				jso.setMember("bridge", jsbridge);
			}
		});

		try {
			webEngine.load(new File("www\\index.html").toURI().toURL().toString());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setDBConnection(DBConnection dbc) {
		this.dbc = dbc;
	}

	/**
	 * Klasse die als Brueke zwischen Javascript und Java dient.
	 * Alle methoden in dieser Klasse können über bridge.methode(); in Javascript/HTML aufgerufen werden.
	 * @author Daniel Isaak
	 */
	public class Bridge {
		public void upcall() {
			System.out.println("JS-UPCALL");
			Artikel a = new Artikel(new Blogger(), "test", "filip ist ne cunt");
			String test = String.format("uebergabe(new Artikel('%s','%s','%s'));","cunt",a.getTitel(),a.getText());
			System.out.println(test);
			webEngine.executeScript(test);
		}
	}
}
