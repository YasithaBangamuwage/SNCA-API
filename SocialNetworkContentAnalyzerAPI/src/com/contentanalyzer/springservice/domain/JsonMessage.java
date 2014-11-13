/**
 * 
 */
package com.contentanalyzer.springservice.domain;

import java.util.ArrayList;

/**
 * @author YAS
 * @author Garushi
 * @version 1.3
 * @Desc A JsonMessage that SNCA API returned.
 * */
public class JsonMessage {

	/**
	 * Main message.
	 */
	String msg;
	/**
	 * Current status of the service.
	 */
	String status;
	/**
	 * user id that josn message belongs.
	 */
	String userId;
	/**
	 * Category List
	 */
	private ArrayList<String> CategoryList;
	/**
	 * Category List String
	 */
	String CategoryListString;
	/**
	 * filtered word Set
	 */
	private ArrayList<String> filteredwordSet;

	/**
	 * @return the msg
	 */
	public final String getMsg() {
		return msg;
	}

	/**
	 * @param msg
	 *            the msg to set
	 */
	public final void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * @return the status
	 */
	public final String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public final void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the userId
	 */
	public final String getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public final void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the categoryList
	 */
	public final ArrayList<String> getCategoryList() {
		return CategoryList;
	}

	/**
	 * @param categoryList
	 *            the categoryList to set
	 */
	public final void setCategoryList(ArrayList<String> categoryList) {
		CategoryList = categoryList;
	}

	/**
	 * @return the categoryListString
	 */
	public final String getCategoryListString() {
		return CategoryListString;
	}

	/**
	 * @param categoryListString
	 *            the categoryListString to set
	 */
	public final void setCategoryListString(String categoryListString) {
		CategoryListString = categoryListString;
	}

	/**
	 * @return the filteredwordSet
	 */
	public final ArrayList<String> getFilteredwordSet() {
		return filteredwordSet;
	}

	/**
	 * @param filteredwordSet
	 *            the filteredwordSet to set
	 */
	public final void setFilteredwordSet(ArrayList<String> filteredwordSet) {
		this.filteredwordSet = filteredwordSet;
	}

	/**
	 * Default constructor.
	 */
	public JsonMessage() {
		super();
		this.status = "false";
		this.CategoryList = new ArrayList<String>();
	}

	/**
	 * Used to make CategoryListString as string representation
	 * 
	 * @return CategoryListString as string
	 */
	public String CategoryListToJson() {

		CategoryListString = "{";
		for (int i = 0; i < CategoryList.size(); i++) {
			CategoryListString = CategoryListString + i + ":" + "\""
					+ CategoryList.get(i) + "\",";
		}
		CategoryListString = CategoryListString.substring(0,
				CategoryListString.length() - 1);
		CategoryListString = CategoryListString + "}";

		return CategoryListString;
	}

	@Override
	public String toString() {
		return "JsonMessage [userId:" + userId + ", status:" + status
				+ ", CategoryList:" + CategoryList + ", filteredwordSet:"
				+ filteredwordSet + "]";
	}
}
