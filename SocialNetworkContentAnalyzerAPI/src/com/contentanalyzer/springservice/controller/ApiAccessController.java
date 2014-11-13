package com.contentanalyzer.springservice.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.contentanalyzer.springservice.domain.ApiAccess;
import com.contentanalyzer.springservice.domain.JsonMessage;

/**
 * @author YAS
 * @author Garushi
 * @version 1.5
 * @Desc Main access points of the SNCA API
 * */
@RestController
@RequestMapping("/access")
public class ApiAccessController {

	/**
	 * @Desc service that used by not registered users.
	 * @param searchString
	 *            user typed search query
	 * @throws Exception
	 */
	@RequestMapping(value = "/{searchString}", method = RequestMethod.GET, headers = "Accept=application/json")
	public JsonMessage defaultFilterAdsUsingWeka(
			@PathVariable String searchString) throws Exception {
		// JsonMessage jm = new JsonMessage();
		// return jm;
		ApiAccess api = new ApiAccess();
		// default userid is -1
		return api.doPreprocessing("-1",
				searchString.replaceAll("[-+^,']*", ""));
	}

	/**
	 * @Desc service that used to filter category according to the userid. This
	 *       service used to mainly filter SN details.
	 * @param id
	 *            id of the user.
	 * */
	@RequestMapping(value = "/filterCategories/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public JsonMessage filterAdsUsingWeka(@PathVariable String id)
			throws Exception {

		ApiAccess api = new ApiAccess();
		return api.doPreprocessing(id.replaceAll("[-+^,']*", ""));

	}

	/**
	 * @Desc service that used to filter category according to userid and
	 *       searchString Used when user typed search string in browser.
	 * @param id
	 *            id of the user
	 * @param searchString
	 *            user typed search query
	 * */
	@RequestMapping(value = "/filterCategories/{id}/{searchString}", method = RequestMethod.GET, headers = "Accept=application/json")
	public JsonMessage filterAdsUsingWeka(@PathVariable String id,
			@PathVariable String searchString) throws Exception {

		ApiAccess api = new ApiAccess();
		return api.doPreprocessing(id.replaceAll("[-+^,']*", ""),
				searchString.replaceAll("[-+^,']*", ""));
	}
}
