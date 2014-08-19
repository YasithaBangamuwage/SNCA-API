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
	String CategoryListString;
	private ArrayList<String> filteredwordSet;
	
	public ArrayList<String> getFilteredwordSet() {
		return filteredwordSet;
	}

	public void setFilteredwordSet(ArrayList<String> filteredwordSet) {
		this.filteredwordSet = filteredwordSet;
	}

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
	
	public String CategoryListToJson(){
		
		CategoryListString = "{";
		for (int i = 0; i < CategoryList.size(); i++) {
			CategoryListString = CategoryListString + i +":"+"\""+CategoryList.get(i)+"\",";
		}
		CategoryListString = CategoryListString.substring(0, CategoryListString.length() - 1);
		CategoryListString = CategoryListString + "}";
		
		return CategoryListString;
	}
	
	@Override
	public String toString() {
		return "JsonMessage [userId:" + userId
				+ ", status:" + status + ", CategoryList:"
				+ CategoryList +", filteredwordSet:"+filteredwordSet+ "]";
	}
}
