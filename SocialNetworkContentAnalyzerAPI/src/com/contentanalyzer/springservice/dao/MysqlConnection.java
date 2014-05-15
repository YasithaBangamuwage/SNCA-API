package com.contentanalyzer.springservice.dao;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import java.sql.*;

/**
 * @author YAS
 * @author Garushi
 * @version 1.0
 * @Desc A singleton database access class for MySQL
 * */

public class MysqlConnection {

	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/";
	static final String DATABASE = "wekadb";

	// Database credentials
	static final String USER = "root";
	static final String PASSWORD = "123";

	public Connection conn;
	private Statement statement;
	private ResultSet res;
	public static MysqlConnection db;

	private MysqlConnection() {
		try {
			Class.forName(JDBC_DRIVER).newInstance();
			this.conn = (Connection) DriverManager.getConnection(DB_URL
					+ DATABASE, USER, PASSWORD);
		} catch (Exception sqle) {
			sqle.printStackTrace();
		}
	}

	/**
	 * @return MysqlConnection Database connection object
	 */
	public static synchronized MysqlConnection getDbConnection() {
		if (db == null) {
			db = new MysqlConnection();
		}
		return db;
	}

	public ResultSet getQueryData(String query) throws SQLException {
		statement = db.conn.createStatement();
		res = statement.executeQuery(query);
		return res;
	}

	public boolean isExistData(String query) throws SQLException {
		statement = db.conn.createStatement();
		res = statement.executeQuery(query);
		if (res.next()) {
			return true;
		} else {
			return false;
		}
	}

	public int insert(String insertQuery) throws SQLException {
		statement = db.conn.createStatement();
		int result = statement.executeUpdate(insertQuery);
		return result;
	}

	public int delete(String deleteQuery) throws SQLException {
		statement = db.conn.createStatement();
		int result = statement.executeUpdate(deleteQuery);
		return result;
	}

	public int update(String updateQuery) throws SQLException {
		statement = db.conn.createStatement();
		int result = statement.executeUpdate(updateQuery);
		return result;
	}
}
