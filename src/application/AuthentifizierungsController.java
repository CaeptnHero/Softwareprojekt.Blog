package application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;


//Lieﬂt die Daten aus der DB von der Tabelle nutzer
public class AuthentifizierungsController {
	
	private static String sql;
	private static List<Nutzer> NutzerListe = new ArrayList<>();
	private static ResultSet rs;
	
	
	public List<Nutzer> DatenAusDbLesen() throws SQLException{
		
			DBConnection dbc = new DBConnection();
			sql = "SELECT * FROM nutzer"; 
		try {
			dbc.connect();
			rs = dbc.executeQuery(sql);
			while (rs.next()) {
				Nutzer n = new Reader();
				n.setId(rs.getInt("id"));
				n.setNutzername(rs.getString("nutzername"));          
				n.setPasswort(rs.getString("passwort"));
				NutzerListe.add(n);
				}
	
		//Dient der Kontrolle (Kann sp‰ter entfernt werden
		for (Nutzer n : NutzerListe)
			System.out.println(n.getId() + " " + n.getNutzername() + " " + n.getPasswort());
		
		}catch(SQLException e) {
			e.printStackTrace();
			
		}finally {
			dbc.getConnection().close();
			dbc.getStatement().close();
		}
	
		return NutzerListe;	
	}
	
	public boolean Login(List<Nutzer> ListeNutzer, String nutzername, String passwort) {
		
		for (Nutzer n : ListeNutzer ) {
			if(n.getNutzername().equals(nutzername)  && n.getPasswort().equals(passwort)) {
				System.out.println("Login erfolgreich");
				return true;
			}
		}
		System.out.println("Login fehlgeschlagen");
		JOptionPane.showMessageDialog(null,"Login fehlgeschlagen"); 
		return false;	
	}
	
	public boolean Regestrieren(List<Nutzer> ListeNutzer, String nutzername, String passwort) throws SQLException {
		
		String 	sql; 
		Boolean erfolgreich = true;
		
		for ( Nutzer n : ListeNutzer) {
			if(n.getNutzername().equals(nutzername)) {
				erfolgreich = false;
				System.out.println("Der Nutzername ist bereits vergeben");
				JOptionPane.showMessageDialog(null,"Der Nutzername ist bereits vergeben"); 
				return false;
			}
		}
		if (erfolgreich) {
			DBConnection dbc = new DBConnection();
			try {
				dbc.connect();
				sql ="INSERT INTO nutzer (id, nutzername, passwort) VALUES (NULL, '" + nutzername + "', '" + passwort + "')";
				dbc.executeUpdate(sql);
				System.out.println("Benutzer wurde registriert");
				JOptionPane.showMessageDialog(null,"Benutzer wurde registriert"); 
				return true;
			}finally {
				dbc.getConnection().close();
				dbc.getStatement().close();
				}
			}
		return false;
	}
	
	//Zum Testen
	/*
	public static void main (String[] args) {
		AuthentifizierungsController ac = new AuthentifizierungsController();
	
		try {
			ac.Regestrieren(ac.DatenAusDbLesen(), "neuerNutzer", "456");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	*/
}
