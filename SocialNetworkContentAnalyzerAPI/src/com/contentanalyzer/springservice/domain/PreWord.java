/**
 * 
 */
package com.contentanalyzer.springservice.domain;

/**
 * @author YAS
 * @version 1.3
 * @Desc Used to store posTaggered word with relevant tagger.
 * */
public class PreWord {

	/**
	 * tokenized word.
	 */
	private String word;
	/**
	 * posTagger of the given word.
	 */
	private String posTagger;

	/**
	 * word count.
	 */
	private int count;

	/**
	 * Default constructor.
	 */
	public PreWord() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @param word
	 *            word of the PreWord.
	 * @param posTagger
	 *            posTagger of the PreWord.
	 * @param count
	 *            count of the PreWord.
	 */
	public PreWord(String word, String posTagger, int count) {
		super();
		this.word = word;
		this.posTagger = posTagger;
		this.count = count;
	}

	/**
	 * @return the word
	 */
	public String getWord() {
		return word;
	}

	/**
	 * @param word
	 *            the word to set
	 */
	public void setWord(String word) {
		this.word = word;
	}

	/**
	 * @return the posTagger
	 */
	public String getPosTagger() {
		return posTagger;
	}

	/**
	 * @param posTagger
	 *            the posTagger to set
	 */
	public void setPosTagger(String posTagger) {
		this.posTagger = posTagger;
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count
	 *            the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}

}
