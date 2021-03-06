package controller;

import model.*;

import javax.sql.rowset.CachedRowSet;
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
     * Baut die Verbindung zur MySQL-Datenbank auf
     */
    public static void open() {
        try {
            connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Baut die Verbindung zur MySQL-Datenbank ab
     */
    public static void close() {
        try {
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Entwertet eine Zeichenkette um SQL-injektionen zu vermeiden
     *
     * @param s zu entwertender String
     * @return Entwerteter String
     */
    public static String escapeString(String s) {
        String data = "";
        if (!s.equals("")) {
            s = s.replace("\\", "\\\\");
            s = s.replace("'", "\\'");
            s = s.replace("\0", "\\0");
            s = s.replace("\n", "\\n");
            s = s.replace("\r", "\\r");
            s = s.replace("\"", "\\\"");
            s = s.replace("\\x1a", "\\Z");
            data = s;
        }
        return data;
    }

    /**
     * Führt eine SQL-Query aus (SELECT, ...)
     *
     * @param sql SQL-Befehl welcher ausgefuehrt werden soll
     * @return ResultSet, welches alle Zeilen der ausgefuehrten Abfrage enthaelt
     */
    public static ResultSet executeQuery(String sql) {
        try {
            open();
            ResultSet result = statement.executeQuery(sql);
            CachedRowSet cachedResult = RowSetProvider.newFactory().createCachedRowSet();
            cachedResult.populate(result);
            return cachedResult;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return null;
    }

    /**
     * Führt eine SQL-Update aus (INSERT, UPDATE, ...)
     *
     * @param sql SQL-Befehl, welcher ausgefuehrt werden soll
     * @return neu generierter Schluessel
     */
    public static int executeUpdate(String sql) {
        try {
            open();
            statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet res = statement.getGeneratedKeys();
            if (res.next())
                return res.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return -1;
    }

    /**
     * Fuert eine Datenbankabfrage aus, welcher nach einem bestimmten nutzer sucht und diesen zurückgibt
     *
     * @param username name des nutzers
     * @return Object, welches entweder ein Blogger oder ein Reader ist
     */
    public static Object getUser(String username) {
        String SQL = "SELECT COUNT(*) as rowcount, UID, username, password, isBlogger FROM user WHERE user.username = \"" + username + "\"";
        ResultSet res = executeQuery(SQL);
        try {
            res.next();
            if (res.getInt("rowcount") == 1) {
                int nid = res.getInt("UID");
                String un = res.getString("username");
                String pw = res.getString("password");
                if (res.getBoolean("isBlogger"))
                    return new Blogger(nid, un, pw);
                else
                    return new Reader(nid, un, pw);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Alle Artikel und deren Kommentare aus der Datenbank abfragen
     *
     * @return Alle Artikel in einer ArrayList
     */
    public static ArrayList<Article> getArticles() {
        ArrayList<Article> articles = new ArrayList<>();
        ResultSet res = executeQuery("select * from article a, post p, user u where a.aid = p.pid AND u.uid = p.author ORDER BY a.aid DESC");

        try {
            while (res.next()) {
                int id = res.getInt("pid");
                Blogger author = new Blogger(res.getInt("uid"), res.getString("username"), res.getString("password"));
                String title = res.getString("title");
                String text = res.getString("text");
                String date = res.getString("date");
                String formattedDate = date.substring(0, 10) + "T" + date.substring(11, 19);
                LocalDateTime dateTime = LocalDateTime.parse(formattedDate);

                Article a = new Article(id, author, title, text, dateTime);
                // Alle Kommentare des Artikels abfragen
                a.addComment(getComments(a));
                articles.add(a);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return articles;
    }

    /**
     * Alle Kommentare eines Oberbeitrags abfragen
     *
     * @param parent Oberbeitrag, von welchem wir die Kommentare haben wollen
     * @return Kommentarliste des Oberbeitrags
     */
    private static ArrayList<Comment> getComments(Post parent) {
        ArrayList<Comment> comments = new ArrayList<>();
        String query = "select * from post p, comment c, user u where p.pid = c.cid and u.uid = p.author and p.parent = " + parent.getId();
        ResultSet res = executeQuery(query);

        try {
            while (res.next()) {
                boolean isBlogger = res.getBoolean("isBlogger");
                int uid = res.getInt("uid");
                String username = res.getString("username");
                String password = res.getString("password");
                User author = null;
                if (isBlogger)
                    author = new Blogger(uid, username, password);
                else
                    author = new Reader(uid, username, password);

                int id = res.getInt("pid");
                String text = res.getString("text");
                String date = res.getString("date");
                String formattedDate = date.substring(0, 10) + "T" + date.substring(11, 19);
                LocalDateTime dateTime = LocalDateTime.parse(formattedDate);

                Comment k = new Comment(id, author, text, dateTime, parent);
                k.addComment(getComments(k));    //rekursion
                comments.add(k);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return comments;
    }

    /**
     * Fragt die anzahl der Artikel ab und berechnet damit die Seitenanzahl.
     *
     * @return aufgerundete Seitenanzahl
     */
    public static int getNumberOfPages() {
        int count = 0;
        ResultSet res = executeQuery("SELECT count(*) from article");
        try {
            res.next();
            count = res.getInt(1);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return (int) Math.ceil(count / 5.0);
    }

}
