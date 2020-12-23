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
    private Bridge jsBridge;
    private Reader currReader = null;
    private Blogger currBlogger = null;
    private int currPage = 1;
    private int scrollPosition = 0;
    private ArrayList<Article> allArticles;

    @FXML
    private WebView webView = new WebView();

    /**
     * Initialisiert die Webview und Injiziert ein JSObject zur Kommunikation zwischen Java und Javascript
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);
        webEngine.getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) { //on window reload
                jsBridge = new Bridge();
                JSObject jso = (JSObject) jsBridge.executeJavascript("window");
                jso.setMember("bridge", jsBridge);

                allArticles = DatabaseController.getArticles();
                int usertype = currBlogger != null ? 1 : (currReader != null ? 0 : -1); //-1 = Visitor, 0 = Reader, 1 = Blogger
                String username = currBlogger != null ? currBlogger.getUsername() : (currReader != null ? currReader.getUsername() : "Visitor");
                jsBridge.executeJavascript(String.format("ready(' %s', %d, %d, %d);", username, usertype, currPage, scrollPosition));
            }
        });

        try {
            webEngine.load(new File("www\\index.html").toURI().toURL().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Setzt den neuen Nutzer der für die Webview genutzt werden soll
     *
     * @param u neuer Nutzer welcher gesetzt werden soll
     */
    public void setUser(User u) {
        currBlogger = u instanceof Blogger ? (Blogger) u : null;
        currReader = u instanceof Reader ? (Reader) u : null;
        System.out.println("Webview User: " + (u != null ? u : "Visitor"));
    }

    /**
     * Findet einen Beitrag in der ArrayList allArticles
     *
     * @param id        Identifikator des zu findenden Beitrags
     * @param isArticle Sagt aus, ob der zu findende Beitrag ein Artikel ist oder ein Kommentar
     * @return null, falls der Beitrag nicht gefunden wurde. Ein Post-Objekt, falls der zu findende Beitrag gefunden wurde.
     */
    private Post findPost(int id, boolean isArticle) {
        if (isArticle) {
            for (Article a : allArticles) {
                if (a.getId() == id)
                    return a;
            }
        } else {
            for (Article a : allArticles) {
                Post res = findComment(a.getComments(), id);
                if (res != null)
                    return res;
            }
        }
        return null;
    }

    /**
     * Findet ein Kommentar in einer Kommentar ArrayList
     *
     * @param comments Kommentarliste in welcher der gesuchte kommentar gesucht werden soll
     * @param id       Identifikator des zu findenden Kommentar
     * @return null, falls der Kommentar nicht gefunden wurde. Ein Comment-Objekt, falls der zu findende Kommentar gefunden wurde.
     */
    private Comment findComment(ArrayList<Comment> comments, int id) {
        Comment res = null;
        for (Comment c : comments) {
            if (res != null)
                return res;
            if (c.getComments().size() != 0)
                res = findComment(c.getComments(), id);
            if (c.getId() == id)
                res = c;
        }
        return res;
    }

    /**
     * Klasse die als Brueke zwischen Javascript und Java dient.
     * Alle methoden in dieser Klasse können über bridge.methode(); in Javascript/HTML aufgerufen werden.
     */
    public class Bridge {

        /**
         * Injiziert javascript code in die Webengine
         *
         * @param script zu Injizierendes script
         * @return Ruekgabeobjekt von Javascript
         */
        private Object executeJavascript(String script) {
            return webEngine.executeScript(script);
        }

        /**
         * Gibt Javascript Fehler aus
         *
         * @param msg  Fehlernachricht
         * @param url  Dateipfad in dem der Fehler aufgetaucht ist
         * @param line zeile in dem der Fehler aufgetaucht ist
         */
        public void errorLog(String msg, String url, int line) {
            System.out.println("JS error in " + url + " : " + line + "\n" + msg);
        }

        /**
         * Logging methode für Javascript
         *
         * @param msg Log Nachricht
         */
        public void consoleLog(String msg) {
            System.out.println("Javascript log: " + msg);
        }

        /**
         * Laed die WebView neu und setzt die derzeitige Seite und Scrollbar position.
         *
         * @param currPage       derzeitige Seite
         * @param scrollPosition Scrollbar position
         */
        public void reloadSite(int currPage, int scrollPosition) {
            WebViewWindowController.this.currPage = currPage;
            WebViewWindowController.this.scrollPosition = scrollPosition;
            webEngine.reload();
        }

        /**
         * Beiträge werden aus der DB gelesen und anhand der Seitenanzahl die korrekten Beiträge auf der WebView angezeigt, durch die JavaScript funktion "displayArticle".
         *
         * @param numPages anzahl der Seiten
         */
        public void fillWeb(int numPages) {
            System.out.println("Seite: " + numPages);

            int startIndex = (numPages - 1) * 5;
            for (int i = startIndex; i < allArticles.size(); i++) {
                if (i >= startIndex + 5)
                    break;

                Article a = allArticles.get(i);
                String script = String.format("displayArticle(%d, '%s', '%s', '%s', '%s', %b)", a.getId(), a.getAuthor().getUsername(), a.getTitle(), a.getText(), a.getDate(), a.getAuthor().getIsBlogger());
                jsBridge.executeJavascript(script);

                //Kommentare einfügen
                fillComments(a);
            }
        }

        /**
         * TODO: FINISH JAVADOC COMMENT
         *
         * @param b
         */
        private void fillComments(Post b) {
            System.out.println(" AID=" + b.getId() + " Kommentare=" + b.getComments().size());    //FIXME: debug only
            for (int j = 0; j < b.getComments().size(); j++) {
                Comment k = b.getComments().get(j);
                String script = String.format("displayComment(%d, %d, '%s', '%s', '%s', %b)", k.getId(), b.getId(), k.getAuthor().getUsername(), k.getText(), k.getDate(), k.getAuthor().getIsBlogger());
                jsBridge.executeJavascript(script);
                fillComments(k);
            }
        }

        /**
         * Methode zum erstellen von Artikeln von Javascript aus
         *
         * @param title Titel des Artikels
         * @param text  Text des Artikels
         * @throws UserViolationException wird geworfen, wenn diese methode von einem user ausgefürt wird, welcher kein Blogger ist
         */
        public void createArticle(String title, String text) throws UserViolationException {
            if (currBlogger == null)
                throw new UserViolationException();

            title = DatabaseController.escapeString(title);
            text = DatabaseController.escapeString(text);

            currBlogger.createArticle(title, text);
        }

        /**
         * Methode zum erstellen von Kommentaren von Javascript aus
         *
         * @param parentID        Identifikator des Oberbeitrags
         * @param text            Text des Kommentars
         * @param parentIsArticle sagt aus, ob der zu kommentierende Beitrag ein Artikel oder ein Kommentar ist
         * @throws UserViolationException wird geworfen, wenn diese methode von einem unangemeldeten Nutzer ausgefuert wird
         */
        public void createComment(int parentID, String text, boolean parentIsArticle) throws UserViolationException {
            if (currBlogger == null && currReader == null)
                throw new UserViolationException();

            text = DatabaseController.escapeString(text);

            System.out.println("createComment(oberBeitragID=" + parentID + ", text=" + text + ")");
            Post parent = findPost(parentID, parentIsArticle);
            if (currBlogger != null)
                currBlogger.createComment(text, parent);
            else
                currReader.createComment(text, parent);
        }

        /**
         * Methode zum loeschen von Beiträgen von Javascript aus
         *
         * @param id        Identifikator des zu loeschenden Beitrags
         * @param isArticle sagt aus, ob ein das zu loeschende ein Artikel oder ein Kommentar ist
         * @throws UserViolationException wird geworfen, wenn diese methode von einem user ausgefürt wird, welcher kein Blogger ist
         */
        public void deletePost(String id, boolean isArticle) throws UserViolationException {
            if (currBlogger == null)
                throw new UserViolationException();

            int dbid = Integer.parseInt(id.substring(id.indexOf('-') + 1));
            System.out.println("delete Beitrag: " + dbid);
            if (isArticle)
                currBlogger.deleteArticle((Article) findPost(dbid, isArticle));
            else
                currBlogger.deleteComment((Comment) findPost(dbid, isArticle));
        }

        /**
         * Die JavaScript Funktion "addPageNumbers" wird mit dem Wert der Seitenazahl ausgeführt
         */
        public void addPageNumbers() {
            String s = String.format("addPageNumbers('%s')", DatabaseController.getNumberOfPages());
            jsBridge.executeJavascript(s);
        }
    }

    public final class UserViolationException extends Exception {
		private static final long serialVersionUID = -2392433922334610372L;

		public UserViolationException() {
            super("Violated User Structure. User was trying to use a function he is not eligible of using.");
        }
    }

}
