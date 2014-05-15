package com.contentanalyzer.springservice.controller;

import java.sql.ResultSet;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.contentanalyzer.springservice.dao.MysqlConnection;
import com.contentanalyzer.springservice.dao.WekaInstances;
import com.contentanalyzer.springservice.domain.Classification;
import com.contentanalyzer.springservice.domain.Preprocessing;

@RestController
@RequestMapping("/classification")
public class ClassificationController {

	@RequestMapping(value = "/smo/adnotnull", method = RequestMethod.GET)
	public void training() throws Exception {

		Classification classification = new Classification(WekaInstances
				.connect().getQueryData(
						"select * from status_ads where ads is not null;"),
				WekaInstances.connect().getQueryData(
						"select * from status_ads where ads is null;"));
		classification.trainClassifier();
		classification.testClassifier();

	}

	@RequestMapping(value = "/smo/adnull", method = RequestMethod.GET)
	public void testing() throws Exception {

	}
}
