/**
 * 
 */
package com.contentanalyzer.springservice.domain;

/**
 * @author YAS
 * 
 */
public class WordVector {

	private String stemmer;
	private int count;
	private String vectorItem;

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
