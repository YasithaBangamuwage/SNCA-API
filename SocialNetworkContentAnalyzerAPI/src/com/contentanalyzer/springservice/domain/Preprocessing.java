package com.contentanalyzer.springservice.domain;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.contentanalyzer.springservice.dao.MysqlConnection;

import snowballstemmer.PorterStemmer;
import sun.security.acl.WorldGroupImpl;
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

	/**
	 * @return the wordVectorList
	 */
	public HashMap<String, WordVector> getWordVectorList() {
		return wordVectorList;
	}

	/**
	 * @param wordVectorList
	 *            the wordVectorList to set
	 */
	public void setWordVectorList(HashMap<String, WordVector> wordVectorList) {
		this.wordVectorList = wordVectorList;
	}

	private String stringValue;
	private ArrayList<String> filteredwords = new ArrayList<String>();

	public void setFilteredwords(ArrayList<String> filteredwords) {
		this.filteredwords = filteredwords;
	}

	private POSTagger postagger = new POSTagger();
	private List<Token> tokenList = new ArrayList<Token>();

	private HashMap<String, WordVector> wordVectorList = new HashMap<String, WordVector>();

	private HashMap<String, PreWord> preWordList = new HashMap<String, PreWord>();

	/**
	 * @return the filteredwords
	 */
	public ArrayList<String> getFilteredwords() {
		return filteredwords;
	}

	/**
	 * @param filteredwords
	 *            the filteredwords to set
	 */
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
			case "$":
			case "^":
			case "N":
			case "V":
				String word = token.getWord().replaceAll("#", "");
				if (preWordList.containsKey(word)) {
					// update list entry
					preWordList.get(word).setCount(
							preWordList.get(word).getCount() + 1);
				} else {
					// add new map entry
					PreWord newWv = new PreWord(word, token.getPOS(), 1);
					preWordList.put(word, newWv);
				}
			}
			System.out.println("createFilteredTokens - " + token.getWord());
		}
	}

	private void applayPorterStemmer() {
		PorterStemmer stemmer = new PorterStemmer();
		for (Entry<String, PreWord> entry : preWordList.entrySet()) {
			stemmer.setCurrent(entry.getKey());
			if (stemmer.stem()) {
				// If stemming is successful obtain the stem of the given word
				String stemmerWord = stemmer.getCurrent();

				if (wordVectorList.containsKey(stemmerWord)) {
					// update wordvector object with new count value
					wordVectorList.get(stemmerWord).setCount(
							wordVectorList.get(stemmerWord).getCount()
									+ entry.getValue().getCount());
				} else {
					// add new wordvector object
					WordVector wv = new WordVector(stemmerWord, entry
							.getValue().getCount(), entry.getValue().getWord()
							+ "=" + entry.getValue().getPosTagger());
					wordVectorList.put(stemmerWord, wv);
					
					filteredwords.add(stemmerWord);
				}
			}
			System.out.println("applayPorterStemmer - "+ entry.getKey());
		}
	}
}
