package com.contentanalyzer.springservice.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import sun.security.jca.GetInstance.Instance;
import weka.core.Instances;

import com.contentanalyzer.springservice.dao.MysqlConnection;
import com.contentanalyzer.springservice.dao.WekaInstances;
import com.contentanalyzer.springservice.domain.Preprocessing;

/**
 * All pre processing controls will be handled.
 * 
 * @author YAS
 ***/
@RestController
@RequestMapping("/preprocessing/user")
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
							"SELECT name, description, story, message, comments FROM wekadb.social_feeds;");
			Preprocessing preprocessing = new Preprocessing(datbaseInput,
					searchString);
			// preprocessing.preprocessingWithTweetPOSTagger();
			preprocessing.test();
			// do preprocessing
			// update database tables
			// return filter ad list
		} else if (!userSNDataExists) {
			System.out.println("user SN data does not exsits !! ");
		} else if (userSNDataExists && searchString.equals("")) {
			System.out.println("enter search string to filter data !!");
		}
	}

}
