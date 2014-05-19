package com.contentanalyzer.springservice.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.tokenizers.NGramTokenizer;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.StringToWordVector;
import cmu.arktweetnlp.POSTagger;
import cmu.arktweetnlp.Token;

import com.contentanalyzer.springservice.dao.MysqlConnection;
import com.contentanalyzer.springservice.dao.WekaInstances;

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
	/**
	 * Instances after indexing.
	 */
	private Instances outputInstances;

	/*
	 * string data that user entered
	 */

	private String stringValue;

	/*
	 * contains feature words
	 */
	private ArrayList<String> featureWords;

	private ArrayList<String> filteredTokens;

	private POSTagger postagger = new POSTagger();
	private List<Token> tokenList = new ArrayList<Token>();

	public Preprocessing() {
		filteredTokens = new ArrayList<String>();
	}

	public Preprocessing(String searchString) {
		this.stringValue = searchString;
		this.tokenizeStringValue();
	}

	public Preprocessing(Instances inputInstances, String searchString) {
		this.inputInstances = inputInstances;
		this.stringValue = searchString;

		// change this after integrate with db
		this.featureWords = new ArrayList<String>();
		featureWords.add("cat");
		featureWords.add(":)");
		featureWords.add("use");
		featureWords.add("likes");
		featureWords.add("happy");

		this.tokenizeInstances();
		this.tokenizeStringValue();

	}

	public void setFilteredTokens(ArrayList<String> filteredTokens) {
		this.filteredTokens = filteredTokens;
	}

	public ArrayList<String> getFilteredTokens1() {
		return filteredTokens;
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	private void tokenizeInstances() {
		// loop through inputInstances
		for (int i = 0; i < inputInstances.numInstances(); i++) {
			// loop through instance attributes
			for (int x = 0; x < inputInstances.instance(i).numAttributes(); x++) {
				Instance inputInstance = inputInstances.instance(i);
				tokenList.addAll(postagger.runPOSTagger(inputInstance
						.stringValue(x)));
			}
		}
	}

	public  void tokenizeStringValue() {
		tokenList.addAll(postagger.runPOSTagger(stringValue));
	}

	public void test() {
		// loop through selected attribute token data
		for (Token token : tokenList) {

			switch (token.getPOS()) {
			case "A":
			case "V":
			case "R":
			case "#":
				String word = token.getWord().replaceAll("#", "");
				if (featureWords.contains(word)) {
					System.out.println("POSTagger found : " + word);
				}
			}
		}
	}

	public ArrayList<String> getFilteredTokens() {
		
		// loop through selected attribute token data
		for (Token token : tokenList) {
			switch (token.getPOS()) {
			case "A":
			case "R":
			case "#":
			case "^":
			case "N":
				String word = token.getWord().replaceAll("#", "");
				if (!filteredTokens.contains(word)) {
					filteredTokens.add(word);
				}
			}
		}
		return filteredTokens;
	}

}
