package application;

import java.time.LocalDateTime;

public class Artikel extends Beitrag {
	private String titel;
	
	// ctor for UI
	public Artikel(Blogger verfasser, String titel, String text) {
		super(verfasser, text, null);
		this.titel = titel;
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
		//removeFromDB(this)						//sich selbst aus db löschen
	}
}