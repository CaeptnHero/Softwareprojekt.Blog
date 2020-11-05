package application;

public class Reader extends Nutzer {
	public Reader(int id, String nutzername, String passwort) {
		this.id = id;
		this.nutzername = nutzername;
		this.passwort = passwort;
	}
	
	public Reader(String nutzername, String passwort) {
		this.id = -1;
		this.nutzername = nutzername;
		this.passwort = passwort;
	}
}
