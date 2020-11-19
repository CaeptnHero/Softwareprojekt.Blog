package application;

import java.sql.ResultSet;
import java.time.LocalDateTime;

public class Artikel extends Beitrag {
	private String titel;
	
	// ctor for UI
	public Artikel(Blogger verfasser, String titel, String text) {
		super(verfasser, text, null);
		this.titel = titel;

		String sql = String.format("INSERT INTO Artikel VALUES (%s, %s, %s)", this.getId(), titel, text);
		DBConnection db = new DBConnection();
		db.connect();
		db.executeUpdate(sql);
	}
	
	// ctor for db
	public Artikel(int id, Blogger verfasser, String titel, String text, LocalDateTime dateTime) {
		super(id, verfasser, text, dateTime, null);
		this.titel = titel;
	}
	
	public Artikel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getTitel() {
		return titel;
	}
	
	public void setTitel(String titel) {
		this.titel = titel;
	}
	
	public void delete() {
		super.delete();
		
		//PLACEHOLDER ArtikelListe.remove(this)		//sich selbst löschen
		deleteFromDatabase();
	
	}
	private void deleteFromDatabase() {
		
		String sql = "DELETE FROM artikel WHERE AID = " + this.getId();
		DBConnection db = new DBConnection();
		db.connect();
		db.executeUpdate(sql);
		sql = "DELETE FROM beitrag WHERE BID = " + this.getId();
		db.executeUpdate(sql);
	}
}