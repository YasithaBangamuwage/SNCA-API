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
	static final String USER = "cdap_user";
	static final String PASSWORD = "cdap_pwd";

	private WekaInstances() throws Exception {
		query = new InstanceQuery();
		query.setUsername(USER);
		query.setPassword(PASSWORD);

		query.setUsername(USER);
		query.setPassword(PASSWORD);

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
