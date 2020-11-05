package application;

public class Blogger extends Nutzer {
	public Blogger(int id, String nutzername, String passwort) {
		this.id = id;
		this.nutzername = nutzername;
		this.passwort = passwort;
	}
	
	public Blogger(String nutzername, String passwort) {
		this.id = -1;
		this.nutzername = nutzername;
		this.passwort = passwort;
	}
}
