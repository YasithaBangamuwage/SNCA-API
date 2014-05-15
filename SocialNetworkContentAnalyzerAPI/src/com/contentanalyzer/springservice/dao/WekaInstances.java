package com.contentanalyzer.springservice.dao;

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
	static final String PASSWORD = "123";

	private WekaInstances() throws Exception {
		query = new InstanceQuery();
		query.setUsername("root");
		query.setPassword("123");
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
