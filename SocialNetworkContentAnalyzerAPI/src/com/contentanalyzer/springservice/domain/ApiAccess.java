package com.contentanalyzer.springservice.domain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weka.core.Instance;
import weka.core.Instances;

import com.contentanalyzer.springservice.dao.MysqlConnection;
import com.contentanalyzer.springservice.dao.WekaInstances;
import com.contentanalyzer.springservice.domain.Preprocessing;

public class ApiAccess {

	//to store preprocessing objects
	private HashMap<String, Preprocessing> preProcsObjectSet = new HashMap<String, Preprocessing>();
	public ApiAccess() {

	}

	private boolean userExists(String id) throws SQLException {
		return MysqlConnection
				.getDbConnection()
				.isExistData(
						"SELECT social_feeds.user_id FROM wekadb.social_feeds WHERE social_feeds.user_id ="
								+ id + ";");

	}

	public void doPreprocessing(String id, String searchString) throws SQLException {
		
		if(!id.equals("") && !searchString.equals("")){
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			//get current date time with Date()
			String date = dateFormat.format(new Date());
			
			// run the preprocessing using SN data
			Preprocessing preprocessing = new Preprocessing(searchString);
			//put preprocessing object
			preProcsObjectSet.put(date, preprocessing);
		}else{
			System.out.println("invalid request");
		}
	}

	public void doPreprocessing(String id) throws Exception {

		// preprocessing with SN data and searchString
		if (userExists(id)) {
			// get latest SN data group by date
			Instances datbaseInput = WekaInstances
					.connect()
					.getQueryData(
							"SELECT name, description, story, message, comments, feed_date  FROM wekadb.social_feeds WHERE social_feeds.user_id ="
									+ id + " order by feed_date;");
			// to store Instance set sorted by date
			Instances dummayInstancesObj= WekaInstances.connect().getQueryData("SELECT name, description, story, message, comments, feed_date  FROM wekadb.social_feeds WHERE social_feeds.user_id ="
									+ id + " order by feed_date;");
			dummayInstancesObj.delete();
			Instances filterdInstances = new Instances(dummayInstancesObj, 0) ;
			int count = datbaseInput.numInstances();
			int temp = 0;
			Instance current = null ;
			Instance next;
			
			for (int i = 0; i < count; i++) {
				 current = datbaseInput.instance(i);
				temp++;
				if(temp==count){
					 next = datbaseInput.instance(i);
				}else{
					 next = datbaseInput.instance(temp);
				}
				// check the date is equal or not
				if (current.stringValue(5) == next.stringValue(5)) {
					filterdInstances.add(current);
				} else {
					// run the preprocessing using SN data
					Preprocessing preprocessing = new Preprocessing(filterdInstances);
					//put preprocessing object
					preProcsObjectSet.put(current.stringValue(5), preprocessing);
					// get outputs from the preprocessing
					filterdInstances.delete();
					// add new date status to continue the loop
					filterdInstances.add(current);
				}
			}
			Preprocessing preprocessing = new Preprocessing(filterdInstances);
			//put preprocessing object
			preProcsObjectSet.put(current.stringValue(5), preprocessing);//current.stringValue(5)
			//add new filtered data into database
			updateFilteredFeeds(id);
			//delete social_feeds data
			MysqlConnection.getDbConnection().delete("DELETE FROM social_feeds WHERE user_id="+id+"");
			
		}
		// preprocessing already done for requested user
		else if (!userExists(id)) {
			System.out.println("already done !!");
		}
		// Unsuccessful request
		else {
			System.out.println("Unsuccessful request");
		}
	}
	
	private void updateFilteredFeeds(String id) throws SQLException{
		
		for (Map.Entry<String, Preprocessing> entry : preProcsObjectSet.entrySet()) {
			
			String stringData = "";
			for (Map.Entry<String, Integer> insideEntry : entry.getValue().getFilteredwords().entrySet()) {
				stringData = stringData + insideEntry.getKey() + ":" + insideEntry.getValue() + ",";
			}
			
			String vectorData = stringData.replaceAll("[']*", "");
			vectorData = vectorData.substring(0, stringData.length()-1);
			System.out.println(entry.getKey() +" => "+vectorData);
			MysqlConnection.getDbConnection().setWeightMethod01(Integer.parseInt(id), vectorData, entry.getValue().getFilteredwords().size(), entry.getKey());
		}
	}
}
