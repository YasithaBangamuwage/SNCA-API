package com.contentanalyzer.springservice.dao;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;

import java.sql.*;

/**
 * @author YAS
 * @author Garushi
 * @version 1.2
 * @Desc A singleton database access class for MySQL
 * */

public class MysqlConnection {

	/**
	 * JDBC driver name.
	 */
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	/**
	 * database URL.
	 */
	static final String DB_URL = "jdbc:mysql://localhost:3306/";
	/**
	 * database name.
	 */
	static final String DATABASE = "cdap";

	/**
	 * database user.
	 */
	static final String USER = "cdap_user";
	/**
	 * database password.
	 */
	static final String PASSWORD = "cdap_pwd";

	/**
	 * global Connection object.
	 */
	public Connection conn;
	/**
	 * global sql statment object.
	 */
	private Statement statement;
	/**
	 * global sql ResultSet object.
	 */
	private ResultSet res;
	/**
	 * Singleton class variable.
	 */
	public static MysqlConnection db;

	/**
	 * default constructor.
	 */
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
	 * Used to get singleton class object.
	 * 
	 * @return MysqlConnection Database connection object
	 */
	public static synchronized MysqlConnection getDbConnection() {
		if (db == null) {
			db = new MysqlConnection();
		}
		return db;
	}

	/**
	 * Used to get any query data when the sql query passed as parameter.
	 * 
	 * @param query
	 *            sql query that api passed.
	 * @return ResultSet database result as ResultSet.
	 * @throws SQLException
	 */
	public ResultSet getQueryData(String query) throws SQLException {
		statement = db.conn.createStatement();
		res = statement.executeQuery(query);
		return res;
	}

	/**
	 * Used to check the data is exsits in the database.
	 * 
	 * @param query
	 *            sql query that api passed.
	 * @return status
	 * @throws SQLException
	 */
	public boolean isExistData(String query) throws SQLException {
		statement = db.conn.createStatement();
		res = statement.executeQuery(query);
		if (res.next()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Used to run any insert into table query in the database.
	 * 
	 * @param insertQuery
	 *            insert into table query
	 * @return status
	 * @throws SQLException
	 */
	public int insert(String insertQuery) throws SQLException {
		statement = db.conn.createStatement();
		int result = statement.executeUpdate(insertQuery);
		return result;
	}

	/**
	 * Used to run any delete data query in the database.
	 * 
	 * @param deleteQuery
	 *            delete query
	 * @return status
	 * @throws SQLException
	 */
	public int delete(String deleteQuery) throws SQLException {
		statement = db.conn.createStatement();
		int result = statement.executeUpdate(deleteQuery);
		return result;
	}

	/**
	 * Used to run any update query in the database.
	 * 
	 * @param updateQuery
	 *            update query
	 * @return status
	 * @throws SQLException
	 */
	public int update(String updateQuery) throws SQLException {
		statement = db.conn.createStatement();
		int result = statement.executeUpdate(updateQuery);
		return result;
	}

	/**
	 * Used to create final vector in database side.
	 * 
	 * @param id
	 *            user id
	 * @param vectorData
	 *            pre process to vector.
	 * @param count
	 *            row count
	 * @param date
	 *            date of the pre processed
	 * @throws SQLException
	 */
	public void setWeightMethod01(int id, String vectorData, int count,
			String date) throws SQLException {
		// Call a procedure with no parameters
		CallableStatement cs = db.conn
				.prepareCall("{call define_weight_method01(?, ?, ?, ?)}");
		// Set the value for the IN parameter
		cs.setInt(1, id);
		cs.setString(2, vectorData);
		cs.setInt(3, count);
		cs.setString(4, date);
		cs.execute();
	}
}
