package application;

import java.time.LocalDateTime;
import java.util.*;

public abstract class Beitrag {
	private int id;
	private String text;
	private LocalDateTime dateTime;
	private Nutzer verfasser;
	private ArrayList<Kommentar> kommentare;
	private Beitrag oberBeitrag;
	
	// ctor for UI
	public Beitrag(Nutzer verfasser, String text) {
		this.verfasser = verfasser;
		this.text = text;
		this.dateTime = LocalDateTime.now();
		kommentare = new ArrayList<Kommentar>();
		
		//this.id = ...
		//get id from db auto inc
	}
	
	// ctor for db
	public Beitrag(int id, Nutzer verfasser, String text, LocalDateTime dateTime) {
		this.id = id;
		this.verfasser = verfasser;
		this.text = text;
		this.dateTime = dateTime;
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
	
	public void addKommentar(Kommentar k) {
		kommentare.add(k);
	}
	
	public void delteKommentar(Kommentar k) {
		kommentare.remove(k);
	}
	
	public void delete() {
		//delete childs
		for(Beitrag b: kommentare) {
			
		}
		oberBeitrag.delteKommentar((Kommentar)this); //delete ref
	}
}