package application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AuthentifizierungsController {
	
//Lieﬂt die Daten aus der DB von der Tabelle nutzer
	
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
	
	
	/*public static void main (String[] args) {
	AuthentifizierungsController ac = new AuthentifizierungsController();
	try {
		ac.DatenAusDbLesen();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	*/
}
