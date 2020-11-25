package controller;

import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import model.Artikel;
import model.Blogger;
import model.Nutzer;
import model.Reader;
import netscape.javascript.JSObject;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class WebViewWindowController implements Initializable {

    private WebEngine webEngine;
    private Bridge jsbridge;
    private Reader currReader = null;
    private Blogger currBlogger = null;

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
                webEngine.executeScript("ready();");
            }
        });

        try {
            webEngine.load(new File("www\\index.html").toURI().toURL().toString());
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setUser(Nutzer n) {
        if (n instanceof Blogger)
            currBlogger = (Blogger) n;
        else
            currReader = (Reader) n;
    }

    /**
     * Klasse die als Brueke zwischen Javascript und Java dient.
     * Alle methoden in dieser Klasse können über bridge.methode(); in Javascript/HTML aufgerufen werden.
     *
     * @author Daniel Isaak
     */
    public class Bridge {
        public int upcall(String msg) {
            System.out.println("JS-UPCALL");
            System.out.println(msg);
            return 15;
			/*
			Artikel a = new Artikel(new Blogger(), "test", "filip ist ne cunt");
			String test = String.format("uebergabe(new Artikel('%s','%s','%s'));","cunt",a.getTitel(),a.getText());
			System.out.println(test);
			webEngine.executeScript(test);
			*/
        }
/*
        public void fillWeb(int i) {
            ArrayList<Artikel> a;
            a = DBConnection.getArtikel();

            for (int j = i - 1; j < i; j++) {
                String test = String.format("fill(new Artikel('%s','%s','%s'));", a.get(j).getVerfasser(), a.get(j).getTitel(), a.get(j).getText());
                webEngine.executeScript(test);
            }
        }

 */
        public void fillWeb(int i) {
            ArrayList<Artikel> a;
            a = DBConnection.getArtikel();

            for (int j = i+((i-1)*4); i <= i*5; j++) {
                System.out.println(j);
                String test1 = String.format("fill(new Artikel('%s','%s','%s') '%s');", a.get(j-1).getVerfasser(), a.get(j-1).getTitel(), a.get(j-1).getText(), j);
                webEngine.executeScript(test1);

            }
        }


        public void createArticle(String titel, String text) {
            if (currBlogger != null)
                currBlogger.createArticle(titel, text);
        }

        public void errorLog(String msg, String url, int line) {
            System.out.println("Javascript error in " + url + " : " + line + "\n" + msg);
        }

        public void consoleLog(String msg) {
            System.out.println("Javascript log: " + msg);
        }

        public void addPage() {
            String s = String.format("Page('%s')", DBConnection.getSeitenanzahl());
            webEngine.executeScript(s);
        }
    }

}
