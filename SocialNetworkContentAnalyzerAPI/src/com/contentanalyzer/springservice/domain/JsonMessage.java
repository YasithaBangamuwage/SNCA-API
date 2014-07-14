/**
 * 
 */
package com.contentanalyzer.springservice.domain;

import java.util.ArrayList;

/**
 * @author YAS
 * 
 */
public class JsonMessage {

	String msg;
	String status;


	String userId;
	private ArrayList<String> CategoryList;

	public JsonMessage() {
		super();
		this.status="false";
		this.CategoryList = new ArrayList<String>();
	}

	public String getMsg() {
		return msg;
	}


	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public ArrayList<String> getCategoryList() {
		return CategoryList;
	}

	public void setCategoryList(ArrayList<String> categoryList) {
		CategoryList = categoryList;
	}
	
	@Override
	public String toString() {
		return "JsonMessage [userId:" + userId
				+ ", status:" + status + ", CategoryList:"
				+ CategoryList + "]";
	}
}
