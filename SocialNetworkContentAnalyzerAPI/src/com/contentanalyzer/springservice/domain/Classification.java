package com.contentanalyzer.springservice.domain;

import java.util.HashMap;
import java.util.Map;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.SMO;
import weka.core.Instance;
import weka.core.Instances;

public class Classification {

	private Instances trainingData;
	private Instances testingData;
	private Map <Integer, String> classifiedAds = new HashMap<Integer, String>();
	public Map getClassifiedAds() {
		return classifiedAds;
	}

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
<<<<<<< HEAD
        System.out.println(eTest.toSummaryString());
        System.out.println(smo.toString());
=======
       // System.out.println(eTest.toSummaryString());
       // System.out.println(smo.toString());
>>>>>>> 7e9b25f97d0de1841cefc00d4a616d4e9445ce60
	}

	public void testClassifier() throws Exception {
		testingData.setClassIndex(testingData.numAttributes() - 1);
		Instances labeled = new Instances(testingData);

		for (int i = 0; i < testingData.numInstances(); i++) {
			double clsLabel = smo.classifyInstance(testingData.instance(i));
			//System.out.println("clsLabel : " + clsLabel);
			labeled.instance(i).setClassValue(clsLabel);
			//System.out.println(labeled.instance(i).classValue());	
			Instance ins = testingData.instance(i);
			//System.out.println("Id : "+ins.value(0));
			System.out.println("Class predicted for: " +ins.value(1)+ " is " + trainingData.classAttribute().value((int) clsLabel));
			classifiedAds.put((int)ins.value(0), trainingData.classAttribute().value((int) clsLabel));
			//xxxx = trainingData.classAttribute().value((int) clsLabel);
		}
		//System.out.println(classifiedAds.toString());
		Evaluation eTest = new Evaluation(testingData); 
        eTest.evaluateModel(smo,testingData); 
        //System.out.println(eTest.toSummaryString());
	}
}
