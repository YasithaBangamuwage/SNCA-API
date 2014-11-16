package com.contentanalyzer.springservice.domain;

import java.io.File;
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
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.WordNetDatabase;

/**
 * @author YAS
 * @version 1.8
 * @Desc Do all Preprocessing operations.
 * */
public class Preprocessing {

	/**
	 * Instances to be indexed.
	 */
	private Instances inputInstances;

	/**
	 * user search string.
	 */
	private String stringValue;

	/**
	 * final filtered words list.
	 */
	private ArrayList<String> filteredwords = new ArrayList<String>();

	/**
	 * POSTagger object.
	 */
	private POSTagger postagger = new POSTagger();
	/**
	 * to store token list.
	 */
	private List<Token> tokenList = new ArrayList<Token>();

	/**
	 * to store word Vector List.
	 */
	private HashMap<String, WordVector> wordVectorList = new HashMap<String, WordVector>();

	/**
	 * to store pre Word List.
	 */
	private HashMap<String, PreWord> preWordList = new HashMap<String, PreWord>();

	/**
	 * WordNetDatabase object.
	 */
	private WordNetDatabase database;

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

	/**
	 * @return the filteredwords
	 */
	public ArrayList<String> getFilteredwords() {
		return filteredwords;
	}

	/**
	 * 
	 * @return the stringValue
	 */
	public String getStringValue() {
		return stringValue;
	}

	/**
	 * 
	 * @param stringValue
	 *            the stringValue to set.
	 */
	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	/**
	 * Default constructor.
	 */
	public Preprocessing() {

	}

	/**
	 * @param searchString
	 *            user search string of the Preprocessing.
	 */
	public Preprocessing(String searchString) {

		System.setProperty("wordnet.database.dir", "/usr/share/tomcat/dict");
		database = WordNetDatabase.getFileInstance();
		this.stringValue = searchString;
		this.POSTagerTokenizeStringValue();
		this.createFilteredTokens();
		this.applayPorterStemmer();
	}

	/**
	 * 
	 * @param inputInstances
	 *            weka instance set.
	 * @throws SQLException
	 */

	public Preprocessing(Instances inputInstances) throws SQLException {

		System.setProperty("wordnet.database.dir", "/usr/share/tomcat/dict");
		database = WordNetDatabase.getFileInstance();
		this.inputInstances = inputInstances;
		this.POSTagerTokenizeInstances();
		this.createFilteredTokens();
		this.applayPorterStemmer();
	}

	/**
	 * Used to Tokenize weka Instances using POSTager
	 */
	private void POSTagerTokenizeInstances() {

		// tokenList.clear();

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

	/**
	 * Used to add stringValue tokens into tokenList.
	 */
	private void POSTagerTokenizeStringValue() {
		tokenList.addAll(postagger.runPOSTagger(stringValue));
	}

	/**
	 * Tokenize using POSTager.
	 */
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
					// check synonyms count
					int sCount = synonymsCounter(tokenList, word);
					// System.out.println("synonymsCounter : " + sCount);
					// add new map entry
					PreWord newWv = new PreWord(word, token.getPOS(),
							1 + sCount);
					preWordList.put(word, newWv);
				}
			}
			// System.out.println("createFilteredTokens - " + token.getWord());
		}
	}

	/**
	 * Used to applay Porter Stemmer
	 */
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
							wordVectorList.get(stemmerWord).getCount()+ entry.getValue().getCount());
				} else {
					// add new wordvector object
					//System.out.println(stemmerWord);
					WordVector wv = new WordVector(stemmerWord, entry
							.getValue().getCount(), stemmerWord
							+ "=" + entry.getValue().getPosTagger());
					wordVectorList.put(stemmerWord, wv);

					filteredwords.add(stemmerWord);
				}
			}
			// System.out.println("applayPorterStemmer - " + entry.getKey());
		}
	}

	/**
	 * Used to count synonyms using wordnet database.
	 * 
	 * @param tokenList
	 *            set of tokens
	 * @param pWord
	 *            word to check synonyms.
	 * @return number of synonyms
	 */
	private int synonymsCounter(List<Token> tokenList, String pWord) {

		int count = 0;
		Synset[] synsets = database.getSynsets(pWord);

		if (synsets.length > 0) {
			for (int i = 0; i < synsets.length; i++) {
				// System.out.println(synsets[i]);
				String[] wordForms = synsets[i].getWordForms();
				for (int j = 0; j < wordForms.length; j++) {
					for (Token token : tokenList) {
						String word = token.getWord().replaceAll("#", "");
						if (wordForms[j].equals(word)
								&& !wordForms[j].equals(pWord)) {
							count++;
							// System.out.println("match : " + wordForms[j]);
						}
					}
				}
			}
		} else {
			return 0;
		}
		return count;
	}
}
