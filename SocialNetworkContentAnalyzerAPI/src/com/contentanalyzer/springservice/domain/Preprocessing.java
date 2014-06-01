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

	/*
	 * contains feature words
	 */
	private ArrayList<String> featureWords;
	public static HashMap<String, Integer> newFeatureWords = new HashMap<String, Integer>();
	private HashMap<String, Integer> weightedWords = new HashMap<String, Integer>();

	private HashMap<String, Integer> filteredWTokenSet = new HashMap<String, Integer>();
	private HashMap<String, Integer> filteredwords = new HashMap<String, Integer>();
	private POSTagger postagger = new POSTagger();
	private List<Token> tokenList = new ArrayList<Token>();
	
	private int D;
	
	
	public Preprocessing() {

	}

	public Preprocessing(String searchString, ArrayList<String> featureWords, int D) {
		this.stringValue = searchString;
		this.featureWords = featureWords;
		this.POSTagerTokenizeStringValue();
		this.createFilteredTokens();
		this.applayPorterStemmer();
		this.D = D;
	}

	public Preprocessing(Instances inputInstances,
			ArrayList<String> featureWords, int D) throws SQLException {
		this.inputInstances = inputInstances;
		this.featureWords = featureWords;
		this.POSTagerTokenizeInstances();
		this.createFilteredTokens();
		this.applayPorterStemmer();
		this.D = D;
		this.setWeight();

		// join with featurewords
		// make weight for filtered word set
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}


	public HashMap<String, Integer> getWeightedWords() {
		return weightedWords;
	}

	public void setWeightedWords(HashMap<String, Integer> weightedWords) {
		this.weightedWords = weightedWords;
	}
	
	private void POSTagerTokenizeInstances() {
		if (!inputInstances.equals(null) || inputInstances.numInstances() != 0) {

			// loop through inputInstances
			for (int i = 0; i < inputInstances.numInstances(); i++) {
				// loop through instance attributes
				for (int x = 0; x < inputInstances.instance(i).numAttributes(); x++) {
					Instance inputInstance = inputInstances.instance(i);
					// inputInstance.attribute(4).value(arg0)
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
				if(filteredWTokenSet.containsKey(word)){
					//update map entry
					filteredWTokenSet.put(word, filteredWTokenSet.get(word) + 1);
				}else{
					//add new map entry
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
			if(stemmer.stem()){
				// If stemming is successful obtain the stem of the given word
				String word = stemmer.getCurrent();
				if(filteredwords.containsKey(word)){
					filteredwords.put(word, filteredwords.get(word) + entry.getValue());
				}else{
					filteredwords.put(word, entry.getValue());
				}
			}
		}
		System.out.println("filteredwords : " + filteredwords.toString());
		
	}

	
	private void setWeight() throws SQLException {
		
	
		for (Map.Entry<String, Integer> entry : filteredwords.entrySet()) {
			
			// check w in featureWords
			if(featureWords.contains(entry.getKey())){
				// do weight
				int TF = entry.getValue();
				int DF = MysqlConnection.getDbConnection().getLikeCount(entry.getKey());
				int A = (int) (TF * Math.log((10/DF)));/**************************************/
				// put into weightedWords
				weightedWords.put(entry.getKey(), A);
				
			}else if(!featureWords.contains(entry.getKey())){
				// check w in newFeatureWords
				if(newFeatureWords.containsKey(entry.getKey())){//.contains(entry.getKey())){
					
					newFeatureWords.put(entry.getKey(), newFeatureWords.get(entry.getKey()) + 1);
					
					int TF = entry.getValue();
					int DF = newFeatureWords.get(entry.getKey());//MysqlConnection.getDbConnection().getLikeCount(entry.getKey());
					//System.out.println("D:10  DF: "+DF+" TF: "+TF);
					int A = (int) (TF * Math.log((10/DF)));/***********************/
					System.out.println("entry.getKey() : "+entry.getKey()+ " D:10  DF: "+DF+" TF: "+TF+" A: "+A);
					// put into weightedWords
					weightedWords.put(entry.getKey(), A);
				}else{
					// add w into newFeatureWords
					newFeatureWords.put(entry.getKey(),1);
					// do weight(0)
					// add into weightedWords
					weightedWords.put(entry.getKey(), 0);
				}
			}
		}
		
		
		
		
		

		System.out.println("weightedWords set : "+weightedWords.toString());
		System.out.println("----------------------------------------------------------------------");

	}
	
	
	

}
