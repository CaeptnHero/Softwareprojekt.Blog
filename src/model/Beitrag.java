package model;

import controller.DBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public abstract class Beitrag {
	private int id;
	private String text;
	private LocalDateTime dateTime;
	private Nutzer verfasser;
	private Beitrag oberbeitrag;
	private ArrayList<Kommentar> kommentare;
	
	// ctor for UI
	public Beitrag(Nutzer verfasser, String text, Beitrag oberbeitrag) {
		this.verfasser = verfasser;
		this.text = text;
		this.dateTime = LocalDateTime.now();
		kommentare = new ArrayList<>();

		String sql = String.format("INSERT INTO Beitrag (Datum, Verfasser, Oberbeitrag) VALUES (%s, %s, %s)", this.dateTime, this.verfasser, this.oberbeitrag);
		DBConnection db = new DBConnection();
		db.connect();
		ResultSet res = db.executeUpdate(sql);
		try {
			if (res.next())
				this.id = res.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// ctor for db
	public Beitrag(int id, Nutzer verfasser, String text, LocalDateTime dateTime, Beitrag oberbeitrag) {
		this.id = id;
		this.verfasser = verfasser;
		this.text = text;
		this.dateTime = dateTime;
	}
	
	public Beitrag() {
		
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public LocalDateTime getDate() {
		return dateTime;
	}
	
	public Nutzer getVerfasser() {
		return verfasser;
	}
	
	public Beitrag getOberbeitrag() {
		return oberbeitrag;
	}
	
	public void addKommentar(Kommentar k) {
		kommentare.add(k);
	}
	
	public void addKommentar(ArrayList<Kommentar> kommentare) {
		this.kommentare.addAll(kommentare);
	}
	
	public ArrayList<Kommentar> getKommentare() {
		return kommentare;
	}
	
	public void delteKommentar(Kommentar k) {
		//#region Kommentar aus DB löschen
		String sql = "DELETE FROM kommentar WHERE KID = " + k.getId();
		DBConnection db = new DBConnection();
		db.connect();
		db.executeUpdate(sql);
		sql = "DELETE FROM beitrag WHERE BID = " + k.getId();
		db.executeUpdate(sql);
		//#endregion
		
		
		kommentare.remove(k);	//kommentar löschen
	}
	
	public void delete() {
		for(Kommentar k: kommentare) {
			k.delete(); //recursion
			
			this.delteKommentar(k);	//eigene kommentare löschen
		}
	}
}