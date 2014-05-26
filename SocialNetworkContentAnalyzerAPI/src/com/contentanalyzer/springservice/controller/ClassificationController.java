package com.contentanalyzer.springservice.controller;

import java.util.Iterator;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.contentanalyzer.springservice.dao.MysqlConnection;
import com.contentanalyzer.springservice.dao.WekaInstances;
import com.contentanalyzer.springservice.domain.Classification;
import com.contentanalyzer.springservice.domain.test;

@RestController
@RequestMapping("/classification")
public class ClassificationController {

	@RequestMapping(value = "/smo/adnotnull", method = RequestMethod.GET)
	public void training() throws Exception {

		Classification classification = new Classification(WekaInstances
				.connect().getQueryData(
						"select filtered_id, filtered_feed, classified_ads from filtered_feeds where classified_ads is not null;"),
				WekaInstances.connect().getQueryData(
						"select filtered_id, filtered_feed, classified_ads from filtered_feeds where classified_ads is null;"));
		classification.trainClassifier();
		classification.testClassifier();
		Map map = classification.getClassifiedAds();
		Iterator iterator = (Iterator) map.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry mapEntry = (Map.Entry) ((java.util.Iterator) iterator).next();
			System.out.println("The key is: " + mapEntry.getKey()
				+ ",value is :" + mapEntry.getValue());
			String insertAd = "update filtered_feeds set classified_ads = '"+mapEntry.getValue()+"' where filtered_id = "+mapEntry.getKey()+";";
			//System.out.println(insertAd);
			MysqlConnection.getDbConnection().insert(insertAd);
		}
	}
}
