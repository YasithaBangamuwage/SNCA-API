package com.contentanalyzer.springservice.controller;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import weka.core.Instances;

import com.contentanalyzer.springservice.dao.MysqlConnection;
import com.contentanalyzer.springservice.dao.WekaInstances;
import com.contentanalyzer.springservice.domain.JsonMessage;
import com.contentanalyzer.springservice.domain.Preprocessing;

/**
 * All pre processing controls will be handled.
 * 
 * @author YAS
 ***/
@RestController
@RequestMapping("/preprocessing")
public class PreprocessingController {

	/**
	 * @Desc service that used by not registered users.
	 * @param searchString
	 * @throws Exception
	 */
	@RequestMapping(value = "/{searchString}", method = RequestMethod.GET)
	public void defaultFilterAdsUsingWeka(@PathVariable String searchString)
			throws Exception {
		System.out.println("user not register with our app");
	}

	/**
	 * @Desc service that used by registered users without having SN details
	 * @param searchString
	 * @throws Exception
	 */
	@RequestMapping(value = "filterAds/{searchString}", method = RequestMethod.GET)
	public void filterAdsUsingWeka(@PathVariable String searchString)
			throws Exception {
		System.out
				.println("user only registered with app or second attempt of thr full registered user");

	}

	/**
	 * @Desc service that used by users that fulfill all application
	 *       requirements
	 * @param id
	 * @param searchString
	 * @throws Exception
	 */
	@RequestMapping(value = "/filterAds/{id}/{searchString}", method = RequestMethod.GET)
	public void filterAdsUsingWeka(@PathVariable String id,
			@PathVariable String searchString) throws Exception {
		// check user SN data exists or not
		boolean userSNDataExists = MysqlConnection
				.getDbConnection()
				.isExistData(
						"SELECT social_feeds.user_id FROM wekadb.social_feeds WHERE social_feeds.user_id ="
								+ id + ";");
		if (userSNDataExists && !searchString.equals("")) {
			// get user SN status data
			Instances datbaseInput = WekaInstances
					.connect()
					.getQueryData(
							"SELECT name FROM wekadb.test4;");
			ResultSet res = MysqlConnection.getDbConnection().getQueryData("SELECT * FROM Feature_Words;");
			ArrayList<String> featureWordSet = new ArrayList<String>();
			while(res.next()){
				featureWordSet.add(res.getString(1));
			}
			Preprocessing preprocessing = new Preprocessing(datbaseInput,
					searchString, featureWordSet);
		
			preprocessing.tokenizeInstances();
			preprocessing.tokenizeStringValue();
			//get all filtered tokens
			preprocessing.createFilteredTokensSet();
			//get ads feature words set
			Map<String, ArrayList<String>> adsMap = new HashMap<String, ArrayList<String>>();
			ArrayList<String> adsFWordsSet = new ArrayList<String>();
			 ResultSet res2 = MysqlConnection.getDbConnection().getQueryData("SELECT advertisements.ADVERTISEMENT_ID, advertisements.FILTERED_WORDS  FROM wekadb.advertisements");
			 while(res2.next()){
				 
				 adsFWordsSet.addAll(Arrays.asList(res2.getString(2).split("\\s*,\\s*")));
				 adsMap.put(res2.getString(1), adsFWordsSet);
				}
			 System.out.println("filtered ads : "+preprocessing.mapFilteredTokens(adsMap).toString());
			
			
			
			// update database tables
			// return filter ad list
			
			
			
		} else if (!userSNDataExists) {
			System.out.println("user SN data does not exsits !! ");
		} else if (userSNDataExists && searchString.equals("")) {
			System.out.println("enter search string to filter data !!");
		}
	}

	@RequestMapping(value = "filterAdWords/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public JsonMessage filterFeatureWords(@PathVariable String id)
			throws Exception {
		Preprocessing preprocessing = new Preprocessing();
		ArrayList<String> tokenSet = new ArrayList<String>();
		ResultSet res = MysqlConnection
				.getDbConnection()
				.getQueryData(
						"SELECT advertisements.FILTERED_WORDS, advertisements.NAME, advertisements.DESCRIPTION "
								+ "FROM wekadb.advertisements WHERE advertisements.ADVERTISEMENT_ID ="
								+ id + ";");
		while (res.next()) {
			tokenSet.addAll(Arrays.asList(res.getString(1).split("\\s*,\\s*")));
			preprocessing.setStringValue(res.getString(2));
			preprocessing.tokenizeStringValue();
			preprocessing.setStringValue(res.getString(3));
			preprocessing.tokenizeStringValue();
		}
		tokenSet.addAll(preprocessing.getFilteredTokens());
		
		
		
		// Converting ArrayList to HashSet to remove duplicates
		HashSet<String> listToSet = new HashSet<String>(tokenSet);
		// Creating ArrayList without duplicate values
		ArrayList<String> listWithoutDuplicates = new ArrayList<String>(
				listToSet);
		
		
		String updatedFilterWordList = listWithoutDuplicates.toString()
				.substring(1, listWithoutDuplicates.toString().length() - 1);
		int updateStatus = MysqlConnection.getDbConnection().update(
				"UPDATE advertisements SET advertisements.FILTERED_WORDS='"
						+ updatedFilterWordList
						+ "'  WHERE advertisements.ADVERTISEMENT_ID =" + id
						+ ";");
		// check update query works or not
		JsonMessage jm = new JsonMessage();
		if (updateStatus == 1) {
			jm.setId(updateStatus);
			jm.setMsg("Success");
		} else {

			jm.setId(updateStatus);
			jm.setMsg("Fail");
		}
		return jm;
	}
}
