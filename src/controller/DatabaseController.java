package controller;

import model.*;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Dient als Schnittstelle zur MySQL-Datenbank
 */
public final class DatabaseController {
    static final String dbUrl = "jdbc:mysql://localhost:3306/blog?autoReconnect=true";
    static final String dbUsername = "root";
    static final String dbPassword = "";

    private static Connection connection;
    private static Statement statement;

    /**
     * Baut eine verbindung zur MySQL-Datenbank auf.
     * @author Daniel Isaak
     */
    public static void open() {
        try {
            connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            statement = connection.createStatement();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Schließt die Verbindung zur MySQL-Datenbank.
     * @author Daniel Isaak
     */
    public static void close() {
        try {
            statement.close();
            connection.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Speichert ein ResultSet zwischen.
     * @param in zu speicherndes ResultSet.
     * @return Zwischenspreichertes CachedRowSet
     * @author Daniel Isaak
     */
    private static CachedRowSet cacheRowSet(ResultSet in) throws SQLException {
        RowSetFactory factory = RowSetProvider.newFactory();
        CachedRowSet out = factory.createCachedRowSet();
        out.populate(in);
        return out;
    }

    /**
     * Entwertet eine Zeichenkette um SQL-injektionen zu vermeiden.
     * @param str zu entwertender String
     * @return Entwerteter String
     * @author Daniel Isaak
     */
    public static String escapeString(String str) {
        String data = "";
        if (!str.equals("")) {
            str = str.replace("\\", "\\\\");
            str = str.replace("'", "\\'");
            str = str.replace("\0", "\\0");
            str = str.replace("\n", "\\n");
            str = str.replace("\r", "\\r");
            str = str.replace("\"", "\\\"");
            str = str.replace("\\x1a", "\\Z");
            data = str;
        }
        return data;
    }

    /**
     * Führt eine SQL-Query aus (SELECT, ...).
     * @param sql SQL-Befehl welcher ausgefuehrt werden soll.
     * @return Resultset, welches alle Zeilen der ausgefuehrten Abfrage enthealt.
     * @author Daniel Isaak
     */
    public static ResultSet executeQuery(String sql) {
        System.out.println("SQL QUERY EXECUTED: " + sql);
        try {
            open();
            return cacheRowSet(statement.executeQuery(sql));
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            close();
        }
        return null;
    }

    /**
     * Führt eine SQL-Update aus (INSERT, UPDATE, ...).
     * @param sql SQL-Befehl, welcher ausgefuehrt werden soll.
     * @return neu generierter Schluessel.
     * @author Daniel Isaak
     */
    public static int executeUpdate(String sql) {
        System.out.println("SQL UPDATE EXECUTED: " + sql);
        try {
            open();
            statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet res = statement.getGeneratedKeys();
            if (res.next()) {
                return res.getInt(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            close();
        }
        return -1;
    }

    /**
     * Fuert eine Datenbankabfrage aus, welcher nach einem bestimmten nutzer sucht und diesen zurückgibt.
     * @param username name des nutzers
     * @return Object, welches entweder ein Blogger oder ein Reader ist.
     * @author Daniel Isaak
     */
    public static Object getUser(String username) {
        String SQL = "SELECT COUNT(*) as rowcount, NID, Nutzername, Passwort, istBlogger FROM nutzer WHERE nutzer.Nutzername = \"" + username + "\"";
        ResultSet res = executeQuery(SQL);
        try {
            res.next();
            if (res.getInt("rowcount") == 1) {
                int nid = res.getInt("NID");
                String un = res.getString("Nutzername");
                String pw = res.getString("Passwort");
                if (res.getBoolean("istBlogger"))
                    return new Blogger(nid, un, pw);
                else
                    return new Reader(nid, un, pw);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Alle artikel abfragen
     * @return
     * @author
     */
    public static ArrayList<Article> getAllArticles() {
        ArrayList<Article> article = new ArrayList<>();
        ResultSet res = executeQuery("select * from artikel a, beitrag b, Nutzer n where a.aid = b.bid AND n.nid = b.verfasser ORDER BY a.aid DESC");

        try {
            while (res.next()) {
                int id = res.getInt("bid");
                Blogger author = new Blogger(res.getInt("nid"), res.getString("nutzername"), res.getString("Passwort"));
                String title = res.getString("titel");
                String text = res.getString("text");
                String date = res.getString("datum");
                String formattedDate = date.substring(0, 10) + "T" + date.substring(11, 19);
                LocalDateTime dateTime = LocalDateTime.parse(formattedDate);    //TODO: testen ob das parsen funktioniert

                Article a = new Article(id, author, title, text, dateTime);
                // Alle Kommentare des Artikels abfragen
                a.addComment(getComments(a));
                article.add(a);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return article;
    }

    /**
     * Alle Kommentare eines Oberbeitrags abfragen
     * @param parent
     * @return
     * @author
     */
    private static ArrayList<Comment> getComments(Post parent) {
        ArrayList<Comment> comments = new ArrayList<>();
        String query = "select * from beitrag b, kommentar k, Nutzer n where b.bid = k.kid and n.nid = b.verfasser and b.oberbeitrag = " + parent.getId();
        ResultSet res = executeQuery(query);    //FIXME: es kommt nichts zurück SQL abfrage liefer bei phpmyadmin jedoch 1 resultat

        try {
            while (res.next()) {
                boolean isblogger = res.getBoolean("istBlogger");
                int nid = res.getInt("nid");
                String username = res.getString("nutzername");
                String password = res.getString("Passwort");
                User author = null;
                if(isblogger)
                    author = new Blogger(nid, username, password);
                else
                    author = new Reader(nid, username, password);

                int id = res.getInt("bid");
                String text = res.getString("text");
                String date = res.getString("datum");
                String formattedDate = date.substring(0, 10) + "T" + date.substring(11, 19);
                LocalDateTime dateTime = LocalDateTime.parse(formattedDate);    //TODO: testen ob das parsen funktioniert

                Comment k = new Comment(id, author, text, dateTime, parent);
                k.addComment(getComments(k));    //rekursion
                comments.add(k);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return comments;
    }

    public static Post getPost(int bid) {
        ResultSet res = executeQuery("select Oberbeitrag from beitrag where bid = " + bid);
        int obid = -1;
        try {
            res.next();
            obid = res.getInt("Oberbeitrag");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        if (obid == -1) {
            System.out.println("Beitrag nicht gefunden");
            return null;
        } else if (obid == 0) {
            res = executeQuery("select * from artikel a, beitrag b where a.aid = b.bid and b.bid = " + bid);
            try {
                while (res.next()) {
                    int id = res.getInt("bid");
                    Blogger verfasser = null;//res.getInt("b.verfasser");	//TODO: getVerfasser aus RAM / wenn nicht vorhanden => db abfrage starten
                    String titel = res.getString("titel");
                    String text = res.getString("text");
                    String date = res.getString("datum");
                    String formattedDate = date.substring(0, 10) + "T" + date.substring(11, 19);
                    LocalDateTime dateTime = LocalDateTime.parse(formattedDate);    //TODO: testen ob das parsen funktioniert

                    Article a = new Article(id, verfasser, titel, text, dateTime);

                    //	Alle Kommentare des Artikels abfragen
                    a.addComment(getComments(a));
                    return a;
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }

        } else {
            res = executeQuery("select * from Kommentar k, beitrag b where k.kid = b.bid and b.bid = " + bid);
            try {
                while (res.next()) {
                    int id = res.getInt("bid");
                    Blogger verfasser = null;//res.getInt("b.verfasser");	//TODO: getVerfasser aus RAM / wenn nicht vorhanden => db abfrage starten
                    String text = res.getString("text");
                    String date = res.getString("datum");
                    String formattedDate = date.substring(0, 10) + "T" + date.substring(11, 19);
                    LocalDateTime dateTime = LocalDateTime.parse(formattedDate);    //TODO: testen ob das parsen funktioniert

                    Comment k = new Comment(id, verfasser, text, dateTime, getPost(res.getInt("Oberbeitrag"))); //TODO: Beitrag ist null
                    k.addComment(getComments(k));
                    return k;
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * Fragt die anzahl der Artikel ab und berechnet damit die Seitenanzahl.
     * @return aufgerundete Seitenanzahl
     * @author Daniel Isaak
     */
    public static int getNumberOfPages() {
        int anzahl = 0;
        ResultSet res = executeQuery("SELECT count(*) from artikel");
        try {
            res.next();
            anzahl = res.getInt(1);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return (int) Math.ceil(anzahl / 5.0);
    }
}
