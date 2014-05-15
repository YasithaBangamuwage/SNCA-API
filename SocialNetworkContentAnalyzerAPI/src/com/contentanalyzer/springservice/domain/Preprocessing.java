package com.contentanalyzer.springservice.domain;

import weka.core.Instances;
import weka.core.tokenizers.NGramTokenizer;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.StringToWordVector;

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

	public Preprocessing(Instances inputInstances) {
		this.inputInstances = inputInstances;
	}

	public Instances getInputInstances() {
		return inputInstances;
	}

	public void setInputInstances(Instances inputInstances) {
		this.inputInstances = inputInstances;
	}

	public Instances getOutputInstances() {
		return outputInstances;
	}

	public void setOutputInstances(Instances outputInstances) {
		this.outputInstances = outputInstances;
	}

	public void StringToWordVectorForStatus() throws Exception {
		StringToWordVector filter = new StringToWordVector();
		filter.setInputFormat(getInputInstances());
		filter.setInvertSelection(true);
		Instances dataFiltered = Filter.useFilter(getInputInstances(), filter);
		System.out.println("\n\nFiltered data:\n\n" + dataFiltered);
		System.out.println(dataFiltered.toSummaryString());
	}
}
