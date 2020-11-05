package application;

import java.time.LocalDateTime;
import java.util.*;

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
		kommentare = new ArrayList<Kommentar>();
		
		//this.id = ...
		//get id from db auto inc
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
	
	public ArrayList<Kommentar> getKommentare() {
		return kommentare;
	}
	
	public void delteKommentar(Kommentar k) {
		//removeFromDB(k)		//kommentar aus db löschen
		kommentare.remove(k);	//kommentar löschen
	}
	
	public void delete() {
		for(Kommentar k: kommentare) {
			k.delete(); //recursion
			
			this.delteKommentar(k);	//eigene kommentare löschen
		}
	}
}