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
		//removeFromDB(this)						//sich selbst aus db löschen
	}
}