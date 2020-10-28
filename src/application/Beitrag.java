package application;

import java.time.LocalDateTime;

public abstract class Beitrag {
	private int id;
	private String text;
	private LocalDateTime date;
	private Nutzer verfasser;
	
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
		return date;
	}
	
	public void setDate(LocalDateTime date) {
		this.date = date;
	}
	
	public Nutzer getVerfasser() {
		return verfasser;
	}
	
	public void setVerfasser(Nutzer verfasser) {
		this.verfasser = verfasser;
	}
}
