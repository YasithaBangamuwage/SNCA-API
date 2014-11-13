package com.contentanalyzer.springservice.dao;

import weka.core.Instance;
import weka.core.Instances;
import weka.experiment.InstanceQuery;

/**
 * @author YAS
 * @author Garushi
 * @version 1.1
 * @Desc A singleton Mysql instance access class for weka
 * */
public class WekaInstances {

	/**
	 * Global WekaInstances object.
	 */
	public static WekaInstances mysqlWeka;
	/**
	 * Global InstanceQuery object.
	 */
	private static InstanceQuery query;
	/**
	 * weka database access username
	 */
	static final String USER = "cdap_user";
	/**
	 * weka database access password.
	 */
	static final String PASSWORD = "cdap_pwd";

	/**
	 * Default constructor.
	 * 
	 * @throws Exception
	 */
	private WekaInstances() throws Exception {
		query = new InstanceQuery();
		query.setUsername(USER);
		query.setPassword(PASSWORD);

		query.setUsername(USER);
		query.setPassword(PASSWORD);

	}

	/**
	 * Used to get Singleton WekaInstance
	 * 
	 * @return WekaInstance object
	 * @throws Exception
	 */
	public static synchronized WekaInstances connect() throws Exception {
		if (mysqlWeka == null) {
			mysqlWeka = new WekaInstances();
		}
		return mysqlWeka;
	}

	/**
	 * Used to get database data as weka instances
	 * 
	 * @param queryString
	 *            sql query
	 * @return set of weka Instances
	 * @throws Exception
	 */
	public Instances getQueryData(String queryString) throws Exception {
		query.setQuery(queryString);
		return query.retrieveInstances();
	}
}
