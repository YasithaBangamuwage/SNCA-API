package com.contentanalyzer.springservice.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.contentanalyzer.springservice.domain.ApiAccess;
import com.contentanalyzer.springservice.domain.JsonMessage;

@RestController
@RequestMapping("/access")
public class ApiAccessController {

	/**
	 * @Desc service that used by not registered users.
	 * @param searchString
	 * @throws Exception
	 */
	@RequestMapping(value = "/{searchString}", method = RequestMethod.GET, headers = "Accept=application/json")
	public JsonMessage defaultFilterAdsUsingWeka(@PathVariable String searchString)
			throws Exception {
		JsonMessage jm = new JsonMessage();
		return jm;

	}
	/**
	 * @Desc service that used to filter category according to the userid
	 * @param id
	 * */
	@RequestMapping(value = "/filterCategories/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public JsonMessage filterAdsUsingWeka(@PathVariable String id) throws Exception {

		ApiAccess api = new ApiAccess();
		return api.doPreprocessing( id.replaceAll("[-+^,']*", ""));
		
	}
	
	/**
	 * @Desc service that used to filter category according to userid and searchString
	 * @param id
	 * @param searchString
	 * */
	@RequestMapping(value = "/filterCategories/{id}/{searchString}", method = RequestMethod.GET, headers = "Accept=application/json")
	public JsonMessage filterAdsUsingWeka(@PathVariable String id,
			@PathVariable String searchString) throws Exception {

		ApiAccess api = new ApiAccess();
		return api.doPreprocessing( id.replaceAll("[-+^,']*", ""), searchString.replaceAll("[-+^,']*", ""));
	}
}
