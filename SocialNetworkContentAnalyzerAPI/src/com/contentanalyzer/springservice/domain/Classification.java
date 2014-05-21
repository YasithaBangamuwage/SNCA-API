package com.contentanalyzer.springservice.domain;

import weka.classifiers.Evaluation;
import weka.classifiers.functions.SMO;
import weka.core.Instance;
import weka.core.Instances;

public class Classification {

	private Instances trainingData;
	private Instances testingData;
	SMO smo = new SMO();

	public Classification(Instances trainingData, Instances testingData) {
		this.trainingData = trainingData;
		this.testingData = testingData;
	}

	public Instances getTrainingData() {
		return trainingData;
	}

	public void setTrainingData(Instances trainingData) {
		this.trainingData = trainingData;
	}

	public Instances getTestingData() {
		return testingData;
	}

	public void setTestingData(Instances testingData) {
		this.testingData = testingData;
	}

	public void trainClassifier() throws Exception {
		trainingData.setClassIndex(trainingData.numAttributes() - 1);
		smo.setOptions(new String[] { "-R" });
		smo.buildClassifier(trainingData);
		smo.turnChecksOn();
		Evaluation eTest = new Evaluation(trainingData); 
        eTest.evaluateModel(smo,trainingData); 
        System.out.println(eTest.toSummaryString());
        System.out.println(smo.toString());
	}

	public void testClassifier() throws Exception {
		testingData.setClassIndex(testingData.numAttributes() - 1);
		Instances labeled = new Instances(testingData);

		for (int i = 0; i < testingData.numInstances(); i++) {
			double clsLabel = smo.classifyInstance(testingData.instance(i));
			System.out.println(testingData.instance(i));
			System.out.println("clsLabel : " + clsLabel);
			labeled.instance(i).setClassValue(clsLabel);
			System.out.println(labeled.instance(i).classValue());
			System.out.println("Class predicted: " + trainingData.classAttribute().value((int) clsLabel));	
		}
		Evaluation eTest = new Evaluation(testingData); 
        eTest.evaluateModel(smo,testingData); 
        System.out.println(eTest.toSummaryString());
	}
}
