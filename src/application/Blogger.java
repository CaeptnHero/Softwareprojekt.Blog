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
	
	public Blogger() {
		// TODO Auto-generated constructor stub
		this.id = 1;
	}

	public Artikel createArticle(String titel, String text) {
		String sql = "INSERT INTO beitrag VALUES (NULL, CURRENT_DATE()," + this.id + ", NULL)";
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
		Artikel a = new Artikel(this, titel, text);
		a.setId(autoincrement);
		db.executeUpdate("INSERT INTO artikel VALUES (" + autoincrement +  ", '" + a.getTitel() + "', '" + a.getText() +"');");
		db.close();
		return a;
	}
}
