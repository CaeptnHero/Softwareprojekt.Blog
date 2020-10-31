package application;

import java.time.LocalDateTime;

public class Kommentar extends Beitrag {
	// ctor for UI
	public Kommentar(Nutzer verfasser, String text) {
		super(verfasser, text);
	}
	
	// ctor for db
	public Kommentar(int id, Nutzer verfasser, String text, LocalDateTime dateTime) {
		super(id, verfasser, text, dateTime);
	}
	
	public void delete() {
		super.delete();
	}
}