package application;

import java.time.LocalDateTime;

public class Artikel extends Beitrag {
	private String titel;
	
	// ctor for UI
	public Artikel(Nutzer verfasser, String titel, String text) {
		super(verfasser, text, null);
		this.titel = titel;
	}
	
	// ctor for db
	public Artikel(int id, Nutzer verfasser, String titel, String text, LocalDateTime dateTime) {
		super(id, verfasser, text, dateTime, null);
		this.titel = titel;
	}
	
	public String getTitel() {
		return titel;
	}
	
	public void setTitel(String titel) {
		this.titel = titel;
	}
	
	public void delete() {
		super.delete();
	}
}