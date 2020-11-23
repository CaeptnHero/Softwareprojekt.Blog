package Controller;

import Model.Nutzer;

import javax.swing.JOptionPane;

//Lie�t die Daten aus der DB von der Tabelle nutzer
public class AuthentifizierungsController {
	
	private static String sql;

	public Nutzer Login(String nutzername, String passwort) {
		
		DBConnection dbc = new DBConnection();
		Nutzer n = null;
		try {
		dbc.connect();
		n = (Nutzer) dbc.getUser(nutzername, passwort);
		if(n == null) {
			System.out.println("Login fehlgeschlagen");
			JOptionPane.showMessageDialog(null,"Login fehlgeschlagen"); 
			return null;
		}
		System.out.println(n);
		System.out.println("Login erfolgreich");
		JOptionPane.showMessageDialog(null,"Login Erfolgreich"); 
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			dbc.close();
		}
		return n;
	}
	
	public boolean Registrieren(String nutzername, String passwort) {
		DBConnection dbc = new DBConnection();
		Nutzer n;
		try {
		dbc.connect();
		n = (Nutzer) dbc.getUser(nutzername, passwort);
		if(n == null) {
			sql ="INSERT INTO nutzer (nid, nutzername, passwort, istBlogger) VALUES (NULL, '" + nutzername + "', '" + passwort + "', 0)"; //TODO: niemand der sich registriert ist momentan ein blogger
			dbc.executeUpdate(sql);
			System.out.println("Benutzer wurde registriert");
			JOptionPane.showMessageDialog(null,"Benutzer wurde registriert"); 
			return true;
		}
		System.out.println("Der Nutzername ist bereits vergeben");
		JOptionPane.showMessageDialog(null,"Der Nutzername ist bereits vergeben"); 
		return false;
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			dbc.close();
		}
		return false;
	}
}
