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

	private String searchString;
	/*
	 * contains feature words
	 */
	private ArrayList<String> featureWords;

	private POSTagger postagger = new POSTagger();
	private List<Token> tokenList = new ArrayList<Token>();

	public Preprocessing(String searchString) {
		this.searchString = searchString;
		this.tokenizeSearchString();
	}

	public Preprocessing(Instances inputInstances, String searchString) {
		this.inputInstances = inputInstances;
		this.searchString = searchString;

		// change this after integrate with db
		this.featureWords = new ArrayList<String>();
		featureWords.add("cat");
		featureWords.add(":)");
		featureWords.add("use");
		featureWords.add("likes");
		featureWords.add("happy");

		this.tokenizeInstances();
		this.tokenizeSearchString();

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

	private void tokenizeSearchString() {
		tokenList.addAll(postagger.runPOSTagger(searchString));
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

}
