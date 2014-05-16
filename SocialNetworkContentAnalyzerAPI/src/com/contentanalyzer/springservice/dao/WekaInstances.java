package com.contentanalyzer.springservice.dao;

import weka.core.Instance;
import weka.core.Instances;
import weka.experiment.InstanceQuery;

/**
 * @author YAS
 * @author Garushi
 * @version 1.0
 * @Desc A singleton Mysql instance access class for weka
 * */
public class WekaInstances {

	public static WekaInstances mysqlWeka;
	private static InstanceQuery query;
	// Database credentials
	static final String USER = "root";
	static final String PASSWORD = "abc123";

	private WekaInstances() throws Exception {
		query = new InstanceQuery();
<<<<<<< HEAD
		query.setUsername(USER);
		query.setPassword(PASSWORD);
=======
		query.setUsername("root");
		query.setPassword("abc123");
>>>>>>> eb30c2f9f1c6c8483fa57829c8531db48af02acf
	}

	public static synchronized WekaInstances connect() throws Exception {
		if (mysqlWeka == null) {
			mysqlWeka = new WekaInstances();
		}
		return mysqlWeka;
	}

	public Instances getQueryData(String queryString) throws Exception {
		query.setQuery(queryString);
		return query.retrieveInstances();
	}
}
