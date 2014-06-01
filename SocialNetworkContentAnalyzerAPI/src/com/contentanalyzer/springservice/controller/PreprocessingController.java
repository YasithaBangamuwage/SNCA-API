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
import com.contentanalyzer.springservice.domain.*;

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

	@RequestMapping(value = "/filterAds/{id}", method = RequestMethod.GET)
	public void filterAdsSNUsingWeka(@PathVariable String id) throws Exception {

		ApiAccess api = new ApiAccess();
		api.doPreprocessing( id.replaceAll("[-+^,']*", ""));
		
	}
	
	@RequestMapping(value = "/filterAds/{id}/{searchString}", method = RequestMethod.GET)
	public void filterAdsUsingWeka(@PathVariable String id,
			@PathVariable String searchString) throws Exception {

		ApiAccess api = new ApiAccess();
		api.doPreprocessing(searchString.replaceAll("[-+^,']*", ""), id.replaceAll("[-+^,']*", ""));
		
	}
}
