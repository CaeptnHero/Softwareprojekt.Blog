package application;

import java.sql.ResultSet;
import java.sql.SQLException;

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
	
	public Artikel createArticle() {
		Artikel a = new Artikel(null, nutzername, nutzername);
		String sql = "INSERT INTO beitrag VALUES (NULL, CURRENT_DATE(), '1', NULL)";
		DBConnection db = new DBConnection();
		db.connect();
		ResultSet res =	db.executeUpdate(sql);
		int autoincrement = 0;
		try {
			if (res.next())
				autoincrement = res.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.executeUpdate("INSERT INTO artikel VALUES (" + autoincrement +  ", '" + a.getTitel() + "', '" + a.getText() +"');");
		db.close();
		return null;
	}
}
