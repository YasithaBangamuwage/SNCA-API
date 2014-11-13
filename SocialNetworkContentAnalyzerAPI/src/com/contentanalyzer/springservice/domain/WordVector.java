/**
 * 
 */
package com.contentanalyzer.springservice.domain;

/**
 * @author YAS
 * @version 1.3
 * @Desc Used to store final pre processed vector model for a given word.
 * */
public class WordVector {

	/**
	 * stemmer word.
	 */
	private String stemmer;
	/**
	 * word repeated count.
	 */
	private int count;
	/**
	 * POSTTagger referance.
	 */
	private String vectorItem;

	/**
	 * 
	 * @param stemmer
	 *            of the WordVector
	 * @param count
	 *            of the WordVector
	 * @param vectorItem
	 *            of the WordVector
	 */
	public WordVector(String stemmer, int count, String vectorItem) {
		super();
		this.stemmer = stemmer;
		this.count = count;
		this.vectorItem = vectorItem;
	}

	/**
	 * @return the stemmer
	 */
	public String getStemmer() {
		return stemmer;
	}

	/**
	 * @param stemmer
	 *            the stemmer to set
	 */
	public void setStemmer(String stemmer) {
		this.stemmer = stemmer;
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

	/**
	 * @return the vectorItem
	 */
	public String getVectorItem() {
		return vectorItem;
	}

	/**
	 * @param vectorItem
	 *            the vectorItem to set
	 */
	public void setVectorItem(String vectorItem) {
		this.vectorItem = vectorItem;
	}

}
