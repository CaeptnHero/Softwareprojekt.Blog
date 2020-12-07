package controller;

import model.*;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import javax.swing.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Dient als Schnittstelle zur MySQL-Datenbank
 */
public final class DatabaseController {
    static final String dbUrl = "jdbc:mysql://localhost:3306/blog?autoReconnect=true&serverTimezone=UTC";
    static final String dbUsername = "root";
    static final String dbPassword = "";

    private static Connection connection;
    private static Statement statement;

    /**
     * Baut eine verbindung zur MySQL-Datenbank auf.
     *
     * @author Daniel Isaak
     */
    public static void open() {
        try {
            connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            
        }
    }

    /**
     * Schließt die Verbindung zur MySQL-Datenbank.
     *
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
     * Führt eine SQL-Query aus (SELECT, ...).
     *
     * @param sql SQL-Befehl das ausgefuehrt werden soll.
     * @return Resultset, welches alle zeilen der ausgefuehrten Abfrage zurückgibt.
     * @author Daniel Isaak
     */
    public static CachedRowSet executeQuery(String sql) {
        try {
            open();
            statement = connection.prepareStatement(sql);
            RowSetFactory factory = RowSetProvider.newFactory();
            CachedRowSet rowset = factory.createCachedRowSet();
            rowset.populate(statement.executeQuery(sql));
            return rowset;
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
     *
     * @param sql SQL-Befehl das ausgefuehrt werden soll.
     * @return Resultset, welches alle generierten Schlüssel zurückgibt.
     * @author Daniel Isaak
     */
    public static int executeUpdate(String sql) {
        try {
            open();
            statement = connection.prepareStatement(sql);
            //statement = connection.createStatement();
            statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet res = statement.getGeneratedKeys();
            if (res.next()) {
                return res.getInt(1);
            }
        } catch (SQLIntegrityConstraintViolationException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Benutzername ist vergeben");
            return -1;
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
     *
     * @param nutzername name des nutzers
     * @param passwort   passwort des nutzers
     * @return Object, welches entweder ein Blogger oder ein Reader ist.
     * @author Daniel Isaak
     */
    public static Object getUser(String nutzername, String passwort) {
        String SQL = "SELECT COUNT(*) as rowcount, NID, Nutzername, Passwort, istBlogger FROM nutzer WHERE nutzer.Nutzername = \"" + nutzername + "\" AND nutzer.Passwort = \"" + passwort + "\"";
        System.out.println(SQL);
        ResultSet res = executeQuery(SQL);
        try {
            res.next();
            if (res.getInt("rowcount") == 1) {
                int nid = res.getInt("NID");
                String username = res.getString("Nutzername");
                String pw = res.getString("Passwort");
                if (res.getBoolean("istBlogger"))
                    return new Blogger(nid, username, pw);
                else
                    return new Reader(nid, username, pw);
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
    public static ArrayList<Artikel> getArtikel() {
        ArrayList<Artikel> artikel = new ArrayList<>();
        ResultSet res = executeQuery("select * from artikel a, beitrag b where a.aid = b.bid ORDER BY a.aid DESC");

        try {
            while (res.next()) {
                int id = res.getInt("bid");
                Blogger verfasser = null;//res.getInt("b.verfasser");	//TODO: getVerfasser aus RAM / wenn nicht vorhanden => db abfrage starten
                String titel = res.getString("titel");
                String text = res.getString("text");
                String date = res.getString("datum");
                String formattedDate = date.substring(0, 10) + "T" + date.substring(11, 19);
                LocalDateTime dateTime = LocalDateTime.parse(formattedDate);    //TODO: testen ob das parsen funktioniert

                Artikel a = new Artikel(id, verfasser, titel, text, dateTime);
//				Alle Kommentare des Artikels abfragen

                a.addKommentar(getKommentare(a));


                artikel.add(a);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return artikel;
    }

    /**
     * Alle Kommentare eines Oberbeitrags abfragen
     * @param Oberbeitrag
     * @return
     * @author
     */
    private static ArrayList<Kommentar> getKommentare(Beitrag Oberbeitrag) {
        ArrayList<Kommentar> kommentare = new ArrayList<>();
        ResultSet res = executeQuery("select * from beitrag b, kommentar k where b.bid = k.kid and b.oberbeitrag = " + Oberbeitrag.getId());

        try {
            while (res.next()) {
                int id = res.getInt("bid");
                Nutzer verfasser = null;//res.getInt("b.verfasser");	//TODO: getVerfasser aus RAM / wenn nicht vorhanden => db abfrage starten
                String text = res.getString("text");
                LocalDateTime dateTime = LocalDateTime.parse(res.getString("datum"));    //TODO: testen ob das parsen funktioniert

                Kommentar k = new Kommentar(id, verfasser, text, dateTime, Oberbeitrag);
                k.addKommentar(getKommentare(k));    //rekursion
                kommentare.add(k);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        return kommentare;
    }

    public static Beitrag getBeitrag(int bid) {
        ResultSet res = executeQuery("select Oberbeitrag from beitrag where bid = " + bid);
        int obid = -1;
        try {
            res.next();
            obid = res.getInt("Oberbeitrag");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        if(obid == -1) {
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

                    Artikel a = new Artikel(id, verfasser, titel, text, dateTime);

                    //	Alle Kommentare des Artikels abfragen
                    a.addKommentar(getKommentare(a));
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

                    Kommentar k = new Kommentar(id, verfasser, text, dateTime, null); //TODO: Beitrag ist null
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
     *
     * @return aufgerundete Seitenanzahl
     * @author Daniel Isaak
     */
    public static int getSeitenanzahl() {
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
