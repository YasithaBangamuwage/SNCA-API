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
	static final String DATABASE ="cdap";// "wekadb";//cdap

	// Database credentials
	static final String USER = "cdap_user";//"cdap_user";//"root";//cdap_user
	static final String PASSWORD = "cdap_pwd";//"abc123";//cdap_pwd

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

	public int getLikeCount(String word) throws SQLException {
		statement = db.conn.createStatement();
		res = statement
				.executeQuery("SELECT COUNT(*) FROM filtered_feeds WHERE filtered_feed LIKE '%"
						+ word + "%';");
		if (res.next()) {
			return res.getInt(1);
		} else {
			return 0;
		}
	}

	public int getTrainingDataCount() throws SQLException {
		statement = db.conn.createStatement();
		res = statement
				.executeQuery("SELECT filtered_id, filtered_feed, classified_ads FROM filtered_feeds WHERE classified_ads IS NOT NULL;");
		if (res.next()) {
			return res.getInt(1);
		} else {
			return 0;
		}
	}
	
	public void setWeightMethod01(int id, String vectorData, int count, String date) throws SQLException{
		 // Call a procedure with no parameters
	    CallableStatement cs = db.conn.prepareCall("{call define_weight_method01(?, ?, ?, ?)}");
	    // Set the value for the IN parameter
	    cs.setInt(1, id);
	    cs.setString(2, vectorData);
	    cs.setInt(3, count);
	    cs.setString(4, date);
	    cs.execute();
	}
}




