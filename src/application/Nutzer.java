package application;

public abstract class Nutzer {
	private int id;
	private String nutzername;
	private String passwort;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getNutzername() {
		return nutzername;
	}
	
	public void setNutzername(String nutzername) {
		this.nutzername = nutzername;
	}
	
	public String getPasswort() {
		return passwort;
	}
	
	public void setPasswort(String passwort) {
		this.passwort = passwort;
	}
}
