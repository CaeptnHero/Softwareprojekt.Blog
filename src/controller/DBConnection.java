package controller;

import com.mysql.cj.result.Row;
import model.*;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public final class DBConnection {
	static final String dbUrl = "jdbc:mysql://localhost:3306/blog?autoReconnect=true&serverTimezone=UTC";
	static final String dbUsername = "root";
	static final String dbPassword = "";
	
	private static Connection connection;
	private static Statement statement;
	
	/**
	 * Baut eine verbindung zur MySQL-Datenbank auf.
	 * @author Daniel Isaak
	 */
	public static void connect() {
		try {
			connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
		}
		catch(SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Führt eine SQL-Query aus (SELECT, ...).
	 * @param sql SQL-Befehl das ausgefuehrt werden soll.
	 * @return Resultset, welches alle zeilen der ausgefuehrten Abfrage zurückgibt.
	 * @author Daniel Isaak
	 */
	public static CachedRowSet executeQuery(String sql) {
		try {
			connect();
			statement = connection.createStatement();
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
	 * @param sql SQL-Befehl das ausgefuehrt werden soll.
	 * @return Resultset, welches alle generierten Schlüssel zurückgibt.
	 * @author Daniel Isaak
	 */
	public static int executeUpdate(String sql) {
		try {
			connect();
			statement = connection.createStatement();
			statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			ResultSet res = statement.getGeneratedKeys();
			if(res.next()) {
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
	* Fuert eine Datenbankabfrage aus, welcher nach einem bestimmten nutzer sucht und diesen zurückgibt.
	* @param nutzername name des nutzers
	* @param passwort passwort des nutzers
	* @return Object, welches entweder ein Blogger oder ein Reader ist.
	* @author Daniel Isaak
	*/
	public static Object getUser(String nutzername, String passwort) {
		String SQL = "SELECT COUNT(*) as rowcount, NID, Nutzername, Passwort, istBlogger FROM nutzer WHERE nutzer.Nutzername = \"" + nutzername + "\" AND nutzer.Passwort = \"" + passwort + "\"";
		System.out.println(SQL);
		ResultSet res = executeQuery(SQL);
		try {
			res.next();
			if(res.getInt("rowcount") == 1) {
				int nid = res.getInt("NID");
				String username = res.getString("Nutzername");
				String pw = res.getString("Passwort");
				if (res.getBoolean("istBlogger"))
					return new Blogger(nid,username,pw);
				else 
					return new Reader(nid,username,pw);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

//	Alle artikel abfragen
	public static ArrayList<Artikel> getArtikel() {
		ArrayList<Artikel> artikel = new ArrayList<>();
		ResultSet res = executeQuery("select * from artikel a, beitrag b where a.aid = b.bid ORDER BY a.aid DESC");
		
		try {
			while (res.next()) {
				int id = res.getInt("b.bid");
				Blogger verfasser = null;//res.getInt("b.verfasser");	//TODO: getVerfasser aus RAM / wenn nicht vorhanden => db abfrage starten
				String titel = res.getString("a.titel");
				String text = res.getString("a.text");
				String date = res.getString("b.datum");
				String formattedDate = date.substring(0,10) +"T" + date.substring(11,19);
				LocalDateTime dateTime = LocalDateTime.parse(formattedDate);	//TODO: testen ob das parsen funktioniert
				
				Artikel a = new Artikel(id, verfasser, titel, text, dateTime);
//				Alle Kommentare des Artikels abfragen
				a.addKommentar(getKommentare(a));
				
				
				artikel.add(a);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return artikel;
	}
	
//	Alle Kommentare eines Oberbeitrags abfragen
	private static ArrayList<Kommentar> getKommentare(Beitrag Oberbeitrag) {
		ArrayList<Kommentar> kommentare = new ArrayList<>();
		ResultSet res = executeQuery("select * from beitrag b, kommentar k where b.bid = k.kid and b.oberbeitrag = " + Oberbeitrag.getId());
		
		try {
			while (res.next()) {
				int id = res.getInt("b.bid");
				Nutzer verfasser = null;//res.getInt("b.verfasser");	//TODO: getVerfasser aus RAM / wenn nicht vorhanden => db abfrage starten
				String text = res.getString("k.text");
				LocalDateTime dateTime = LocalDateTime.parse(res.getString("b.datum"));	//TODO: testen ob das parsen funktioniert
				
				Kommentar k = new Kommentar(id, verfasser, text, dateTime, Oberbeitrag);
				k.addKommentar(getKommentare(k));	//rekursion
				kommentare.add(k);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return kommentare;
	}

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
