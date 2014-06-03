package com.contentanalyzer.springservice.domain;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.contentanalyzer.springservice.dao.MysqlConnection;

import snowballstemmer.PorterStemmer;
import weka.core.Instance;
import weka.core.Instances;
import cmu.arktweetnlp.POSTagger;
import cmu.arktweetnlp.Token;

/**
 * @author YAS
 * @version 1.0
 * @Desc for do all Preprocessing operations
 * */
public class Preprocessing {

	/**
	 * Instances to be indexed.
	 */
	private Instances inputInstances;
	/*
	 * string data that user entered
	 */

	private String stringValue;

	private HashMap<String, Integer> filteredWTokenSet = new HashMap<String, Integer>();
	private HashMap<String, Integer> filteredwords = new HashMap<String, Integer>();
	private POSTagger postagger = new POSTagger();
	private List<Token> tokenList = new ArrayList<Token>();

	public Preprocessing() {

	}

	public Preprocessing(String searchString) {
		this.stringValue = searchString;
		this.POSTagerTokenizeStringValue();
		this.createFilteredTokens();
		this.applayPorterStemmer();
	}

	public Preprocessing(Instances inputInstances) throws SQLException {
		this.inputInstances = inputInstances;
		this.POSTagerTokenizeInstances();
		this.createFilteredTokens();
		this.applayPorterStemmer();
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}
	
	public HashMap<String, Integer> getFilteredwords() {
		return filteredwords;
	}

	public void setFilteredwords(HashMap<String, Integer> filteredwords) {
		this.filteredwords = filteredwords;
	}

	private void POSTagerTokenizeInstances() {
		if (!inputInstances.equals(null) || inputInstances.numInstances() != 0) {

			// loop through inputInstances
			for (int i = 0; i < inputInstances.numInstances(); i++) {
				// loop through instance attributes
				for (int x = 0; x < inputInstances.instance(i).numAttributes() - 1; x++) {
					Instance inputInstance = inputInstances.instance(i);
					tokenList.addAll(postagger.runPOSTagger(inputInstance
							.stringValue(x).toLowerCase()));
				}
			}
		}
	}

	private void POSTagerTokenizeStringValue() {
		tokenList.addAll(postagger.runPOSTagger(stringValue));
	}

	private void createFilteredTokens() {
		// loop through selected attribute token data
		for (Token token : tokenList) {

			switch (token.getPOS()) {
			case "A":
			case "R":
			case "#":
			case "^":
			case "N":
				String word = token.getWord().replaceAll("#", "");
				// System.out.println(token.getPOS() +" : "+token.getWord());
				if (filteredWTokenSet.containsKey(word)) {
					// update map entry
					filteredWTokenSet
							.put(word, filteredWTokenSet.get(word) + 1);
				} else {
					// add new map entry
					filteredWTokenSet.put(word, 1);
				}
			}
		}
		System.out.println("filteredWTokenSet : "
				+ filteredWTokenSet.toString());
	}

	private void applayPorterStemmer() {
		PorterStemmer stemmer = new PorterStemmer();
		for (Map.Entry<String, Integer> entry : filteredWTokenSet.entrySet()) {
			stemmer.setCurrent(entry.getKey());
			if (stemmer.stem()) {
				// If stemming is successful obtain the stem of the given word
				String word = stemmer.getCurrent();
				if (filteredwords.containsKey(word)) {
					filteredwords.put(word,
							filteredwords.get(word) + entry.getValue());
				} else {
					filteredwords.put(word, entry.getValue());
				}
			}
		}
		System.out.println("filteredwords : " + filteredwords.toString());

	}
}
