package application;

import java.time.LocalDateTime;

public class Kommentar extends Beitrag {
	// ctor for UI
	public Kommentar(Nutzer verfasser, String text, Beitrag oberbeitrag) {
		super(verfasser, text, oberbeitrag);
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
		DBConnection db = new DBConnection();
		db.connect();
		db.executeUpdate(sql);
		sql = "DELETE FROM beitrag WHERE BID = " + this.getId();
		db.executeUpdate(sql);
	}
}