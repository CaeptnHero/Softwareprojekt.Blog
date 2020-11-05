package application;

import java.sql.*;

public class DBConnection {
	final String dbUrl = "jdbc:mysql://localhost:3306/blog?autoReconnect=true&serverTimezone=UTC";
	final String dbUsername = "root";
	final String dbPassword = "";
	
	private Connection connection;
	private Statement statement;
	
	public DBConnection() {
		connection = null;
		statement = null;
	}
	
	public void connect() {
		try {
			connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
		}
		catch(SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ResultSet executeQuery(String sql) {
		try {
			statement = connection.createStatement();
			return statement.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public int executeUpdate(String sql) {
		try {
			statement = connection.createStatement();
			return statement.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	
	public void close() {
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Object getUser(String nutzername, String passwort) {
		String SQL = "SELECT COUNT(*) as rowcount, NID, Nutzername, Passwort, istBlogger FROM nutzer WHERE nutzer.Nutzername = \"" + nutzername + "\" AND nutzer.Passwort = \"" + passwort + "\"";
		System.out.println(SQL);
		ResultSet res = executeQuery(SQL);
		try {
			res.next();
			if(res.getInt("rowcount") == 1) {
				if (res.getBoolean("istBlogger")) {
					return new Blogger(res.getInt("NID"),res.getString("Nutzername"),res.getString("Passwort"));
				}
				else {
					return new Reader(res.getInt("NID"),res.getString("Nutzername"),res.getString("Passwort"));
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
