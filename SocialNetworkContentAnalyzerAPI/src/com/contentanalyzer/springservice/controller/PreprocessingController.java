package com.contentanalyzer.springservice.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.contentanalyzer.springservice.dao.MysqlConnection;
import com.contentanalyzer.springservice.dao.WekaInstances;
import com.contentanalyzer.springservice.domain.Preprocessing;

/**
 * All pre processing controls will be handled.
 ***/
@RestController
@RequestMapping("/preprocessing/user")
public class PreprocessingController {

	@RequestMapping(method = RequestMethod.GET)
	public void test() throws Exception {
		System.out.println("get lost !!");
	}

	@RequestMapping(value = "status/{id}", method = RequestMethod.GET)
	public void dopreprocessing(@PathVariable String id) throws Exception {
		boolean userExsits = MysqlConnection
				.getDbConnection()
				.isExistData(
						"SELECT social_feeds.user_id FROM wekadb.social_feeds WHERE social_feeds.user_id ="
								+ id + ";");
		// check user exists or not
		if (userExsits) {
			Preprocessing preprocessing = new Preprocessing(
					WekaInstances
							.connect()
							.getQueryData(
									"SELECT name, description, story, message, comments FROM wekadb.social_feeds;"));
			preprocessing.StringToWordVectorForStatus();
		} else {
			System.out.println("user does not exsits !! ");
		}
	}

}
