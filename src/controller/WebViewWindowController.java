package controller;

import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import model.*;
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
    private User currUser = null;
    private int currPage = 1;
    private int scrollPosition = 0;

    @FXML
    private WebView webView = new WebView();

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);

        webEngine.getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) { //on window reload
                jsbridge = new Bridge();
                JSObject jso = (JSObject) webEngine.executeScript("window");
                jso.setMember("bridge", jsbridge);

                int usertype = currUser == null ? 0 : (currReader != null ? 1 : -1);
                String username = ((currUser != null) ? currUser.getUsername() : "");
                webEngine.executeScript(String.format("ready(' %s', %d, %d, %d);", username, usertype, currPage, scrollPosition));
            }
        });

        try {
            webEngine.load(new File("www\\index.html").toURI().toURL().toString());
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setUser(User n) {
        if (n == null) {
            System.out.println("webview user: " + "NONE");
            currReader = null;
            currBlogger = null;
            currUser = null;
        }
        else if (n instanceof Blogger) {
            System.out.println("webview user: " + n.getUsername());
            currBlogger = (Blogger) n;
            currUser = currBlogger;
        }
        else {
            System.out.println("webview user: " + n.getUsername());
            currReader = (Reader) n;
            currUser = currReader;
        }
    }

    /**
     * Klasse die als Brueke zwischen Javascript und Java dient.
     * Alle methoden in dieser Klasse können über bridge.methode(); in Javascript/HTML aufgerufen werden.
     *
     * @author Daniel Isaak
     */
    public class Bridge {
        public void reloadSite(int currPage, int scrollPosition) {
            WebViewWindowController.this.currPage = currPage;
            WebViewWindowController.this.scrollPosition = scrollPosition;
            webEngine.reload();
        }

        public void errorLog(String msg, String url, int line) {
            System.out.println("JS error in " + url + " : " + line + "\n" + msg);
        }

        public void consoleLog(String msg) {
            System.out.println("Javascript log: " + msg);
        }

        /**
         * Beiträge werden aus der DB gelesen und anhand der Seitenanzahl die korrekten Beiträge auf der WebView angezeigt, durch die JavaScript funktion "fill"
         *
         * @param seitenzahl
         */
        public void fillWeb(int seitenzahl) {
            System.out.println("Seite: " + seitenzahl);
            ArrayList<Article> allArticles;
            allArticles = DatabaseController.getArticle();

            int startIndex = (seitenzahl - 1) * 5;
            for (int i = startIndex; i < allArticles.size(); i++) {
                if(i >= startIndex + 5) {
                    break;
                }
                Article a = allArticles.get(i);
                System.out.print("Index=" + i);    //FIXME: debug only
                String script = String.format("displayArticle(%d, '%s', '%s', '%s')", a.getId(), a.getAuthor(), a.getTitle(), a.getText());
                webEngine.executeScript(script);

                //Kommentare einfügen
                fillComments(a);
            }
        }

        private void fillComments(Post b) {
            System.out.println(" AID=" + b.getId() + " Kommentare=" +b.getComments().size());    //FIXME: debug only
            for (int j = 0; j < b.getComments().size(); j++) {
                Comment k = b.getComments().get(j);
                System.out.println("Kommentar anzeigen: " + k.getId()); //FIXME: debug only
                String script = String.format("displayComment(%d, %d, '%s', '%s')", k.getId(), b.getId(), k.getAuthor(), k.getText());
                webEngine.executeScript(script);
                fillComments(k);
            }
        }

        public void createArticle(String titel, String text) {
            if (currBlogger != null) {
                titel = DatabaseController.escapeString(titel);
                text = DatabaseController.escapeString(text);

                currBlogger.createArticle(titel, text);
            }
        }

        public void createComment(int oberBeitragID, String text) {
            text = DatabaseController.escapeString(text);

            System.out.println("createComment(oberBeitragID=" + oberBeitragID + ", text="+text+")");
            Post b = DatabaseController.getPost(oberBeitragID); //FIXME: retarded shit
            currUser.createComment(text, b);
        }

        public void deleteBeitrag(String id) {
            int dbid = Integer.parseInt(id.substring(id.indexOf('-') + 1));
            System.out.println("delete Beitrag: " + dbid);

            //FIXME: OBJECT ORIENTED MAYBE?
            String sql = "DELETE FROM artikel WHERE AID = " + dbid;
            DatabaseController.executeUpdate(sql);
            sql = "DELETE FROM kommentar WHERE KID = " + dbid;
            DatabaseController.executeUpdate(sql);
            sql = "DELETE FROM beitrag WHERE BID = " + dbid;
            DatabaseController.executeUpdate(sql);
        }

        /**
         * Die JavaScript Funktion "Page" wird mit dem Wert der Seitenazahl ausgeführt
         */
        public void addPage() {
            String s = String.format("Page('%s')", DatabaseController.getNumberOfPages());
            webEngine.executeScript(s);
        }
    }

}
