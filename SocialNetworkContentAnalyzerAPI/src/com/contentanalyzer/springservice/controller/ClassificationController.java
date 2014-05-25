package com.contentanalyzer.springservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
						"select filtered_feed, classified_ads from filtered_feeds where classified_ads is not null;"),
				WekaInstances.connect().getQueryData(
						"select filtered_feed, classified_ads from filtered_feeds where classified_ads is null;"));
		classification.trainClassifier();
		classification.testClassifier();

	}

	@RequestMapping(value = "/smo/adnull", method = RequestMethod.GET)
	public void testing() throws Exception {
		test classifier;
			classifier = new test();
			classifier.loadModel();
			classifier.makeInstance();
			classifier.classify();
	}
}
