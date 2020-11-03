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
			e.printStackTrace();
		}
	}
	
	public ResultSet executeQuery(String sql) {
		try {
			statement = connection.createStatement();
			return statement.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public int executeUpdate(String sql) {
		try {
			statement = connection.createStatement();
			return statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public Connection getConnection() {
		return connection;
	}
	
	public Statement getStatement() {
		return statement;
	}
	

	
}
