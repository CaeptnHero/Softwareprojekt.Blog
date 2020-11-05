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
	
	/**
	 * Führt eine SQL-Query aus (SELECT, ...).
	 * @param sql SQL-Statement das ausgeführt werden soll.
	 * @return Resultset, welches alle zeilen der ausgeführten Abfrage zurückgibt.
	 */
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
	
	/**
	 * Führt eine SQL-Update aus (INSERT, UPDATE, ...).
	 * @param sql SQL-Statement das ausgeführt werden soll.
	 * @return Resultset, welches alle generierten Schlüssel zurückgibt.
	 */
	public ResultSet executeUpdate(String sql) {
		try {
			statement = connection.createStatement();
			statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			return statement.getGeneratedKeys();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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

	public Connection getConnection() {
		return connection;
	}
	
	public Statement getStatement() {
		return statement;
	}
	
	
	
}
