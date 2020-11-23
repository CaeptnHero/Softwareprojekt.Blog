package model;

import controller.DBConnection;

import java.time.LocalDateTime;

public class Kommentar extends Beitrag {
	// ctor for UI
	public Kommentar(Nutzer verfasser, String text, Beitrag oberbeitrag) {
		super(verfasser, text, oberbeitrag);

		String sql = String.format("INSERT INTO Kommentar VALUES (%s, %s)", this.getId(), text);
		DBConnection.executeUpdate(sql);
	}
	
	// ctor for db
	public Kommentar(int id, Nutzer verfasser, String text, LocalDateTime dateTime, Beitrag oberbeitrag) {
		super(id, verfasser, text, dateTime, oberbeitrag);
	}
	
	public void delete() {
		super.delete();	//eigene kommentare löschen
		this.getOberbeitrag().delteKommentar(this);	//sich selbst löschen
		deleteFromDatabase(); // sich selnst aus db löschen
	}
	
	private void deleteFromDatabase() {
		String sql = "DELETE FROM kommentar WHERE KID = " + this.getId();
		DBConnection.executeUpdate(sql);
		sql = "DELETE FROM beitrag WHERE BID = " + this.getId();
		DBConnection.executeUpdate(sql);
	}
}