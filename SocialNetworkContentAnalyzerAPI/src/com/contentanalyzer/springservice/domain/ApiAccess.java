package com.contentanalyzer.springservice.domain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import weka.core.Instance;
import weka.core.Instances;

import com.contentanalyzer.springservice.dao.MysqlConnection;
import com.contentanalyzer.springservice.dao.WekaInstances;
import com.contentanalyzer.springservice.domain.Preprocessing;

public class ApiAccess {

	// to store preprocessing objects
	private HashMap<String, Preprocessing> preProcsObjectSet = new HashMap<String, Preprocessing>();
	JsonMessage jm = new JsonMessage();

	public ApiAccess() {

	}

	private boolean userExists(String id) throws SQLException {
		return MysqlConnection.getDbConnection().isExistData(
				"SELECT social_feeds.user_id FROM social_feeds WHERE social_feeds.user_id ="
						+ id + ";");

	}

	@SuppressWarnings("unchecked")
	public JsonMessage doPreprocessing(String id, String searchString)
			throws Exception {

		System.out.println("start pre processing and classification");

		if (!id.equals("") && !searchString.equals("")) {

			try {
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				// get current date time with Date()
				String date = dateFormat.format(new Date());

				// run the preprocessing using SN data
				Preprocessing preprocessing = new Preprocessing(searchString);
				// put preprocessing object
				preProcsObjectSet.put(date, preprocessing);
				// add new filtered data into database
				updateFilteredFeeds(id);

				// do classification
				SMOClassification(id);
				jm.setUserId(id);
				jm.setStatus("true");
				jm.setMsg("classification done");

				System.out.println("end pre processing and classification");

				
				  List<String> list = new ArrayList<String>(preprocessing .getFilteredwords());
				  jm.setFilteredwordSet((ArrayList<String>) list);
				 

				return jm;

			} catch (Exception ex) {
				System.out.println(ex.getMessage());
				jm.setUserId(id);
				jm.setStatus("false");
				jm.setMsg("internal error : " + ex.getMessage());
				return jm;
			}

		} else {
			System.out.println("invalid request");
			jm.setUserId(id);
			jm.setStatus("false");
			jm.setMsg("bad request");
			return jm;
		}
	}

	@SuppressWarnings("unchecked")
	public JsonMessage doPreprocessing(String id) throws Exception {

		jm.setUserId(id);
		System.out.println("start pre processing and classification");

		// preprocessing with SN data and searchString
		if (userExists(id)) {

			try {
				// get latest SN data group by date
				Instances datbaseInput = WekaInstances
						.connect()
						.getQueryData(
								"SELECT name, description, story, message, comments, feed_date  FROM social_feeds WHERE social_feeds.user_id ="
										+ id + " order by feed_date;");
				// to store Instance set sorted by date
				Instances dummayInstancesObj = WekaInstances
						.connect()
						.getQueryData(
								"SELECT name, description, story, message, comments, feed_date  FROM social_feeds WHERE social_feeds.user_id ="
										+ id + " order by feed_date;");
				dummayInstancesObj.delete();
				Instances filterdInstances = new Instances(dummayInstancesObj,
						0);
				int count = datbaseInput.numInstances();
				int temp = 0;
				Instance current = null;
				Instance next;

				for (int i = 0; i < count; i++) {
					current = datbaseInput.instance(i);
					temp++;
					if (temp == count) {
						next = datbaseInput.instance(i);
					} else {
						next = datbaseInput.instance(temp);
					}
					// check the date is equal or not
					if (current.stringValue(5) == next.stringValue(5)) {
						filterdInstances.add(current);
					} else {
						// run the preprocessing using SN data
						Preprocessing preprocessing = new Preprocessing(
								filterdInstances);
						// put preprocessing object
						preProcsObjectSet.put(current.stringValue(5),
								preprocessing);
						// get outputs from the preprocessing
						filterdInstances.delete();
						// add new date status to continue the loop
						filterdInstances.add(current);
					}
				}
				Preprocessing preprocessing = new Preprocessing(
						filterdInstances);
				// put preprocessing object
				preProcsObjectSet.put(current.stringValue(5), preprocessing);// current.stringValue(5)
				// add new filtered data into database
				updateFilteredFeeds(id);
				// delete social_feeds data
				MysqlConnection.getDbConnection().delete(
						"DELETE FROM social_feeds WHERE user_id=" + id + "");

				// do classification
				SMOClassification(id);
				jm.setUserId(id);
				jm.setStatus("true");
				jm.setMsg("classification done for SN");

				System.out.println("end pre processing and classification");
				return jm;

			} catch (Exception ex) {

				System.out.println(ex.getStackTrace());
				jm.setUserId(id);
				jm.setStatus("false");
				jm.setMsg("internal error : " + ex.getMessage());
				return jm;

			}

		}
		// preprocessing already done for requested user
		else if (!userExists(id)) {
			System.out.println("already done or invlid user id !!");
			jm.setStatus("false");
			jm.setMsg("classification already done or invlid user id");
			return jm;
		}
		// Unsuccessful request
		else {
			System.out.println("Unsuccessful request");
			jm.setStatus("false");
			jm.setMsg("bad request");
			return jm;
		}
	}

	private void updateFilteredFeeds(String id) throws SQLException {

		for (Map.Entry<String, Preprocessing> entry : preProcsObjectSet
				.entrySet()) {

			String stringData = "";

			for (Entry<String, WordVector> insideEntry : entry.getValue()
					.getWordVectorList().entrySet()) {
				stringData = stringData
						+ insideEntry.getValue().getVectorItem() + ":"
						+ insideEntry.getValue().getCount() + ",";
			}

			if (stringData.length() != 0) {
				String vectorData = stringData.replaceAll("[']*", "");
				String newVectorString = vectorData.substring(0,
						vectorData.length() - 1);
				System.out.println(entry.getKey() + " => " + newVectorString);

				MysqlConnection.getDbConnection().setWeightMethod01(
						Integer.parseInt(id), newVectorString,
						entry.getValue().getWordVectorList().size(),
						entry.getKey());

			}
		}
	}

	private void SMOClassification(String id) throws Exception {
		ArrayList<String> PredictedList = new ArrayList<String>();

		Classification classification = new Classification(
				WekaInstances
						.connect()
						.getQueryData(
								"select filtered_id, filtered_feed, classified_ads from filtered_feeds where classified_ads is not null;"),
				WekaInstances
						.connect()
						.getQueryData(
								"select filtered_id, filtered_feed, classified_ads from filtered_feeds where classified_ads is null and user_id="
										+ id + ";"));
		classification.trainClassifier();
		classification.testClassifier();
		Map map = classification.getClassifiedAds();
		Iterator iterator = (Iterator) map.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry mapEntry = (Map.Entry) ((java.util.Iterator) iterator)
					.next();
			// System.out.println("The key is: " + mapEntry.getKey()+
			// ",value is :" + mapEntry.getValue());
			PredictedList.add(mapEntry.getValue().toString());
			String insertAd = "update filtered_feeds set classified_ads = '"
					+ mapEntry.getValue() + "' where filtered_id = "
					+ mapEntry.getKey() + ";";
			// System.out.println(insertAd);
			MysqlConnection.getDbConnection().insert(insertAd);
		}
		jm.setCategoryList(PredictedList);
	}
}
