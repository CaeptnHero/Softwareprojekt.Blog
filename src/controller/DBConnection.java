package controller;

import model.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class DBConnection {
	final String dbUrl = "jdbc:mysql://localhost:3306/blog?autoReconnect=true&serverTimezone=UTC";
	final String dbUsername = "root";
	final String dbPassword = "";
	
	private Connection connection;
	private Statement statement;
	
	public DBConnection() {
		connection = null;
		statement = null;
	}
	
	/**
	 * Baut eine verbindung zur MySQL-Datenbank auf.
	 * @author Daniel Isaak
	 */
	public void connect() {
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
	public ResultSet executeQuery(String sql) {
		try {
			statement = connection.createStatement();
			return statement.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Führt eine SQL-Update aus (INSERT, UPDATE, ...).
	 * @param sql SQL-Befehl das ausgefuehrt werden soll.
	 * @return Resultset, welches alle generierten Schlüssel zurückgibt.
	 * @author Daniel Isaak
	 */
	public ResultSet executeUpdate(String sql) {
		try {
			statement = connection.createStatement();
			statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			return statement.getGeneratedKeys();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Schließt die Verbindung zur MySQL-Datenbank.
	 * @author Daniel Isaak
	 */
	public void close() {
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
	public Object getUser(String nutzername, String passwort) {
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
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

//	Alle artikel abfragen
	public ArrayList<Artikel> getArtikel() {
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
	private ArrayList<Kommentar> getKommentare(Beitrag Oberbeitrag) {
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
	
}
