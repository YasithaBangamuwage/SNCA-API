package com.contentanalyzer.springservice.domain;

import java.util.ArrayList;

public class AdvertisementCategory {
	
	private ArrayList<String> CategoryList;

	public AdvertisementCategory() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AdvertisementCategory(ArrayList<String> categoryList) {
		super();
		CategoryList = categoryList;
	}

	public ArrayList<String> getCategoryList() {
		return CategoryList;
	}

	public void setCategoryList(ArrayList<String> categoryList) {
		CategoryList = categoryList;
	}
	
	@Override
	public String toString() {
		return "JsonMessage [categoryList=" + CategoryList + "]";
	}
	
}
