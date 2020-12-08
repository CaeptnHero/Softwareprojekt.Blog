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
    private Nutzer currUser = null;

    @FXML
    private WebView webView = new WebView();

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);
        webEngine.getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                jsbridge = new Bridge();
                JSObject jso = (JSObject) webEngine.executeScript("window");
                jso.setMember("bridge", jsbridge);

                webEngine.executeScript("ready();");
                if (currBlogger == null && currReader == null)
                    webEngine.executeScript("hideUserFunctions();");
                else if (currReader != null)
                    webEngine.executeScript("hideBloggerFunctions();");
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
        if (n == null) {
            System.out.println("webview user: " + "NONE");
            currReader = null;
            currBlogger = null;
            currUser = null;
        }
        else if (n instanceof Blogger) {
            System.out.println("webview user: " + n.getNutzername());
            currBlogger = (Blogger) n;
            currUser = currBlogger;
        }
        else {
            System.out.println("webview user: " + n.getNutzername());
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
        public void errorLog(String msg, String url, int line) {
            System.out.println("Javascript error in " + url + " : " + line + "\n" + msg);
        }

        public void consoleLog(String msg) {
            System.out.println("Javascript log: " + msg);
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
        /**
         * Beiträge werden aus der DB gelesen und anhand der Seitenanzahl die korrekten Beiträge auf der WebView angezeigt, durch die JavaScript funktion "fill"
         *
         * @param seitenzahl
         */
        public void fillWeb(int seitenzahl) {
            System.out.println("Seite: " + seitenzahl);
            ArrayList<Artikel> allArticles;
            allArticles = DatabaseController.getArtikel();

            int startIndex = (seitenzahl - 1) * 5;
            for (int i = startIndex; i < allArticles.size(); i++) {
                if(i >= startIndex + 5) {
                    break;
                }
                Artikel a = allArticles.get(i);
                System.out.println("Beitrag: " + i);
                String script = String.format("displayArticle(%d, '%s', '%s', '%s')", a.getId(), a.getVerfasser(), a.getTitel(), a.getText());
                webEngine.executeScript(script);

                //Kommentare einfügen
                System.out.println("Artikel: " + a.getId() + " Kommentare: " +a.getKommentare().size());
                for (int j = 0; j < a.getKommentare().size(); j++) {
                    Kommentar k = a.getKommentare().get(j);
                    System.out.println("Kommentar anzeigen: " + k.getId());
                    script = String.format("displayComment(%d, %d, '%s', '%s')", k.getId(), a.getId(), k.getVerfasser(), k.getText());
                    webEngine.executeScript(script);
                }
            }

//            for (int j = seitenzahl+((seitenzahl-1)*4); j <= seitenzahl*5; j++) {    //FIXME: crash wenn weniger als 5 artikel in db
//                System.out.println(j);
//                String test1 = String.format("fill(new Artikel('%s','%s','%s'), '%s');", a.get(j-1).getVerfasser(), a.get(j-1).getTitel(), a.get(j-1).getText(), j);
//                webEngine.executeScript(test1);
//
//            }
        }

        public void createArticle(String titel, String text) {
            if (currBlogger != null)
                currBlogger.createArticle(titel, text);
        }

        public void createComment(int oberBeitragID, String text) {
            System.out.println("createComment(oberBeitragID=" + oberBeitragID + ", text="+text+")");
            Beitrag b = DatabaseController.getBeitrag(oberBeitragID); //FIXME: retarded shit
            currUser.createKommentar(text, b);
        }
        /**
         * Die JavaScript Funktion "Page" wird mit dem Wert der Seitenazahl ausgeführt
         */
        public void addPage() {
            String s = String.format("Page('%s')", DatabaseController.getSeitenanzahl());
            webEngine.executeScript(s);
        }
    }

}
