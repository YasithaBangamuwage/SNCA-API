package com.contentanalyzer.springservice.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
	
	public static int x=0;
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

	private ArrayList<String> filteredWTokenSet = new ArrayList<String>();
	private ArrayList<String> filteredwords = new ArrayList<String>();
	private POSTagger postagger = new POSTagger();
	private List<Token> tokenList = new ArrayList<Token>();

	public Preprocessing() {

	}

	public Preprocessing(String searchString, ArrayList<String> featureWords) {
		this.stringValue = searchString;
		this.featureWords = featureWords;
		this.POSTagerTokenizeStringValue();
		this.createFilteredTokens();
		this.applayPorterStemmer();
	}

	public Preprocessing(Instances inputInstances,
			ArrayList<String> featureWords) {
		this.inputInstances = inputInstances;
		this.featureWords = featureWords;
		this.POSTagerTokenizeInstances();
		this.createFilteredTokens();
		this.applayPorterStemmer();

		// join with featurewords
		// make weight for filtered word set
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	/**
	 * @return the filtered words
	 */
	public ArrayList<String> getFilteredwords() {
		return filteredwords;
	}

	/**
	 * @param filteredwords
	 *            the filtered words to set
	 */
	public void setFilteredwords(ArrayList<String> filteredwords) {
		this.filteredwords = filteredwords;
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
				if (!filteredWTokenSet.contains(word)) {
					filteredWTokenSet.add(word);
				}
			}
		}
		System.out.println("filteredWTokenSet : "
				+ filteredWTokenSet.toString());
	}

	private void applayPorterStemmer() {
		PorterStemmer stemmer = new PorterStemmer();
		for (int i = 0; i < filteredWTokenSet.size(); i++) {
			// System.out.println("befor stemme :"+filteredWTokenSet.get(i));
			stemmer.setCurrent(filteredWTokenSet.get(i));
			if (stemmer.stem()) {
				// If stemming is successful obtain the stem of the given word
				String w = stemmer.getCurrent();
				if (!filteredwords.contains(w)) {
					filteredwords.add(w);

				}
			}
		}
		System.out.println("filteredwords : " + filteredwords.toString());
		System.out
				.println("----------------------------------------------------------------------");
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public ArrayList<String> getFilteredWTokenSet() {
		// loop through selected attribute token data
		for (Token token : tokenList) {
			switch (token.getPOS()) {
			case "A":
			case "R":
			case "#":
			case "^":
			case "N":
				String word = token.getWord();
				if (featureWords.contains(word)
						&& !filteredWTokenSet.contains(word)) {
					filteredWTokenSet.add(word);
				}
			}
		}
		return filteredWTokenSet;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList<String> mapFilteredTokens(
			Map<String, ArrayList<String>> adsMap) {

		System.out.println("database f words :" + featureWords.toString());
		System.out.println("f words set filtered using database f words :"
				+ filteredWTokenSet.toString());

		ArrayList<String> selectedKeySet = new ArrayList<String>();

		Iterator iterator = adsMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry mapEntry = (Map.Entry) iterator.next();
			ArrayList<String> list = (ArrayList<String>) mapEntry.getValue();

			System.out.println("ads map data : " + list.toString());

			for (String filteredW : filteredWTokenSet) {
				if (list.contains(filteredW)
						&& !selectedKeySet.contains(mapEntry.getKey())) {
					System.out.println("map word: " + filteredW);
					selectedKeySet.add(mapEntry.getKey().toString());
				}
			}
		}
		return selectedKeySet;
	}
}
