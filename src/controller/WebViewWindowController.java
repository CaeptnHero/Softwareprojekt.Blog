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

/**
 * TODO: FINISH JAVADOC COMMENT
 */
public class WebViewWindowController implements Initializable {

    private WebEngine webEngine;
    private Bridge jsBridge;
    private Reader currReader = null;
    private Blogger currBlogger = null;
    private User currUser = null;
    private int currPage = 1;
    private int scrollPosition = 0;
    private ArrayList<Article> allArticles;

    @FXML
    private WebView webView = new WebView();

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);
        webEngine.getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) { //on window reload
                jsBridge = new Bridge();
                JSObject jso = (JSObject) jsBridge.executeJavascript("window");
                jso.setMember("bridge", jsBridge);

                allArticles = DatabaseController.getAllArticles();
                int usertype = currUser == null ? -1 : (currBlogger != null ? 1 : 0); //-1 = Visitor, 0 = Reader, 1 = Blogger
                System.out.println("usertype = " + usertype);
                jsBridge.executeJavascript(String.format("ready(' %s', %d, %d, %d);", (currUser != null ? currUser : "Visitor"), usertype, currPage, scrollPosition));
            }
        });

        try {
            webEngine.load(new File("www\\index.html").toURI().toURL().toString());
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * TODO: FINISH JAVADOC COMMENT
     * @param n
     */
    public void setUser(User n) {
        currBlogger = n instanceof Blogger ? (Blogger) n : null;
        currReader = n instanceof Reader ? (Reader) n : null;
        currUser = n;
        System.out.println("Webview User: " + (n != null ? n : "Visitor"));
    }

    /**
     * TODO: FINISH JAVADOC COMMENT
     * @param id
     * @param isArticle
     * @return
     */
    private Post findPost(int id, boolean isArticle) {
        Post res = null;
        if (isArticle) {
            for (Article a : allArticles) {
                if (a.getId() == id)
                    res = a;
            }
        } else {
            for (Article a : allArticles) {
                res = findComment(a.getComments(), id);
            }
        }
        return res;
    }

    /**
     * TODO: FINISH JAVADOC COMMENT
     * @param comments
     * @param id
     * @return
     */
    private Comment findComment(ArrayList<Comment> comments, int id) {
        Comment res = null;
        for (Comment c : comments) {
            if (c.getComments().size() != 0)
                res = findComment(c.getComments(), id);
            if(c.getId() == id)
                res = c;
        }
        return res;
    }

    /**
     * Klasse die als Brueke zwischen Javascript und Java dient.
     * Alle methoden in dieser Klasse können über bridge.methode(); in Javascript/HTML aufgerufen werden.
     * @author Daniel Isaak
     */
    public class Bridge {

        /**
         * TODO: FINISH JAVADOC COMMENT
         * @param script
         * @return
         */
        private Object executeJavascript(String script) {
            return webEngine.executeScript(script);
        }

        /**
         * TODO: FINISH JAVADOC COMMENT
         * @param msg
         * @param url
         * @param line
         */
        public void errorLog(String msg, String url, int line) {
            System.out.println("JS error in " + url + " : " + line + "\n" + msg);
        }

        /**
         * TODO: FINISH JAVADOC COMMENT
         * @param msg
         */
        public void consoleLog(String msg) {
            System.out.println("Javascript log: " + msg);
        }

        /**
         * TODO: FINISH JAVADOC COMMENT
         * @param currPage
         * @param scrollPosition
         */
        public void reloadSite(int currPage, int scrollPosition) {
            WebViewWindowController.this.currPage = currPage;
            WebViewWindowController.this.scrollPosition = scrollPosition;
            webEngine.reload();
        }

        /**
         * Beiträge werden aus der DB gelesen und anhand der Seitenanzahl die korrekten Beiträge auf der WebView angezeigt, durch die JavaScript funktion "displayArticle".
         * @param numPages anzahl der Seiten
         */
        public void fillWeb(int numPages) {
            System.out.println("Seite: " + numPages);

            int startIndex = (numPages - 1) * 5;
            for (int i = startIndex; i < allArticles.size(); i++) {
                if (i >= startIndex + 5)
                    break;

                Article a = allArticles.get(i);
                System.out.print("Index=" + i);    //FIXME: debug only
                String script = String.format("displayArticle(%d, '%s', '%s', '%s', '%s')", a.getId(), a.getAuthor().getUsername(), a.getTitle(), a.getText(), a.getDate());
                jsBridge.executeJavascript(script);

                //Kommentare einfügen
                fillComments(a);
            }
        }

        /**
         * TODO: FINISH JAVADOC COMMENT
         * @param b
         */
        private void fillComments(Post b) {
            System.out.println(" AID=" + b.getId() + " Kommentare=" + b.getComments().size());    //FIXME: debug only
            for (int j = 0; j < b.getComments().size(); j++) {
                Comment k = b.getComments().get(j);
                System.out.println("Kommentar anzeigen: " + k.getId()); //FIXME: debug only
                String script = String.format("displayComment(%d, %d, '%s', '%s', '%s')", k.getId(), b.getId(), k.getAuthor().getUsername(), k.getText(), k.getDate());
                jsBridge.executeJavascript(script);
                fillComments(k);
            }
        }

        /**
         * TODO: FINISH JAVADOC COMMENT
         * @param title
         * @param text
         * @throws UserViolationException
         */
        public void createArticle(String title, String text) throws UserViolationException {
            if (currBlogger == null)
                throw new UserViolationException();

            title = DatabaseController.escapeString(title);
            text = DatabaseController.escapeString(text);

            currBlogger.createArticle(title, text);
        }

        /**
         * TODO: FINISH JAVADOC COMMENT
         * @param parentID
         * @param text
         * @throws UserViolationException
         */
        public void createComment(int parentID, String text, boolean parentIsArticle) throws UserViolationException {
            if (currUser == null)
                throw new UserViolationException();

            text = DatabaseController.escapeString(text);

            System.out.println("createComment(oberBeitragID=" + parentID + ", text=" + text + ")");
            Post parent = findPost(parentID, parentIsArticle);
            currUser.createComment(text, parent);
        }

        /**
         * TODO: FINISH JAVADOC COMMENT
         * @param id
         * @param isArticle
         * @throws UserViolationException
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
        public UserViolationException() {
            super("Violated User Structure"); //FIXME: irgendeine passedere message.
        }
    }
}
