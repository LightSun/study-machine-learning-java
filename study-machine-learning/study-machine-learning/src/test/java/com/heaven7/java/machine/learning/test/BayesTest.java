package com.heaven7.java.machine.learning.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.heaven7.java.base.util.DefaultPrinter;
import com.heaven7.java.machine.learning.Bayes;

import junit.framework.TestCase;

//朴素贝叶斯概率算法测试
public class BayesTest extends TestCase{
	
	static final DefaultPrinter DP  = DefaultPrinter.getDefault();
	String[][] keywords; 
	int[] categories;
	
	Bayes bayes = new Bayes();
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		keywords = new String[][]{
			{"my", "dog", "has", "flea", "problems", "help", "please"},
             {"maybe", "not", "take", "him", "to", "dog", "park", "stupid"},
             {"my", "dalmation", "is", "so", "cute", "I", "love", "him"},
             {"stop", "posting", "stupid", "worthless", "garbage"},
             {"mr", "licks", "ate", "my", "steak", "how", "to", "stop", "him"} , 
             {"quit", "buying", "worthless", "dog", "food", "stupid"}
		};
		categories = new int[]{0, 1, 0, 1, 0, 1}; // 1 表示是侮辱特征.0相反
		bayes.setKeywords(keywords, categories);
		DP.println("len = " + keywords.length); //列数
		// DP.println("log(2) = " + Math.log(2)); 
	}
	
	public void testFull(){
		List<List<Integer>> trainMat = new ArrayList<>();
		for(String[] words : keywords){
			trainMat.add(bayes.mapVector(words));
		}
		bayes.training(trainMat);
		//System.out.println(Math.E); //2.718281828459045
		
		String[] entry = new String[]{"love", "my", "dalmation"};
		DP.println(Arrays.toString(entry) + " ,classify = " + bayes.classify(entry));
		entry = new String[]{"stupid", "garbage"};
		DP.println(Arrays.toString(entry) + " ,classify = " + bayes.classify(entry));
	}
	
	public void testMapVector(){
		DP.println(bayes.mapVector(keywords[0]).toString());
	}

}
