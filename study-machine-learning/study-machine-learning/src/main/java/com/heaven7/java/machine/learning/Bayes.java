package com.heaven7.java.machine.learning;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.heaven7.java.base.util.ArrayUtils;
import com.heaven7.java.base.util.Throwables;
import com.heaven7.java.machine.learning.MachineLearnings.Scope;
import com.heaven7.java.machine.learning.util.ArrayIterator;

import static com.heaven7.java.machine.learning.util.MathUtils.*;

/**
 * 移植朴素贝叶斯概率算法到这里。from python machine learning.(python_study)
 * @author heaven7
 *
 */
public class Bayes {

	private final List<String> mUniqueWords;
	private String[][] mWords;
	private int[] mCategories; //only 0,1 type
	private int mCategorySum;
	private TrainResult mResult;
	
	
	public Bayes() {
		super();
		mUniqueWords = new ArrayList<>();
	}
	
	/**
	 * classify the entry
	 * @param testEntry the entry to classify
	 * @return 1 represent the 1/true feature.  0 otherwise.
	 */
	@Scope(MachineLearnings.SCOPE_USAGE)
	public int classify(String[] testEntry){
		if(mResult == null){
			throw new IllegalStateException();
		}
		List<Integer> vector = mapVector(testEntry);//count = mUniqueWords.szie()
		//Math.log(mResult.total1Ratio)); 类别对数概率
		float p1 = (float) (sum(multiply(mResult.p1Vector, vector)) + Math.log(mResult.total1Ratio));
		float p0 = (float) (sum(multiply(mResult.p0Vector, vector)) + Math.log(1 -mResult.total1Ratio));
		return p1 > p0 ? 1 : 0;
	}

	@Scope(MachineLearnings.SCOPE_TRAINING)
	public void training(List<List<Integer>> traimMat){
		Throwables.checkEmpty(traimMat);
		TrainResult result = new TrainResult();
		
		final int columnCount = traimMat.size();  //列数 numTrainDocs
		final int rowCount = traimMat.get(0).size();
		
		//计算所有侮辱词的概率
		result.total1Ratio = mCategorySum * 1f /columnCount; //pAbusive
		
		//p1 代表侮辱， p0非
		int[] p0Num = new int[rowCount];
		int[] p1Num = new int[rowCount];
		float p0Denom = 2.0f;
		float p1Denom = 2.0f;
		Arrays.fill(p0Num, 1);
		Arrays.fill(p1Num, 1);
		
		for(int i= 0 ; i < columnCount ; i++){
			List<Integer> row = traimMat.get(i);
			if(mCategories[i] == 1){
				sum(p1Num, row);
				p1Denom += sum(row);
			}else{
				sum(p0Num, row);
				p0Denom += sum(row);
			}
		}
		
		final float p0 = p0Denom;
		final float p1 = p1Denom;
		/**
		 *  求对数，避免下溢出或者 浮点数舍入后为0.得不到正确的. 所以这库取对数. log(a*b) = log(a) + log(b) 
		    log（x） 和f(x)  在相同区域内递增或者递减。 并在相同点上取得极值. 取值不同，结果相同   .   
		 */
		//每个元素除以总词数.
		result.p0Vector = iterate(p0Num, new ArrayIterator<Integer, Float>() {
			@Override
			public Float iterate(int index, Integer t) {
				//System.out.println("t = " + t + " ,p0 = " + p0);
				return (float) Math.log(t/p0);
			}
		});
		result.p1Vector = iterate(p1Num, new ArrayIterator<Integer, Float>() {
			@Override
			public Float iterate(int index, Integer t) {
				//System.out.println("t = " + t + " ,p1 = " + p1);
				return (float) Math.log(t/p1);
			}
		});
		mResult = result;
	}

	/**
	 * set the main data. any step result is based on this call.
	 * @param words the base words
	 * @param categories the category , element value is 1 or 0.
	 */
	@Scope(MachineLearnings.SCOPE_PREPROCCESS)
	public void setKeywords(String[][] words, int[] categories){
		Throwables.checkEmpty(words);
		Throwables.checkEmpty(categories);
		if(words.length != categories.length){
			throw new IllegalArgumentException();
		}
		this.mWords = words;
		this.mCategories = categories;
		
		Set<String> keywords = new HashSet<>();
		for(String[] arr : words){
			for(String word : arr){
				keywords.add(word);
			}
		}
		mUniqueWords.clear();
		mUniqueWords.addAll(keywords);
		// calculate category sum
		mCategorySum = 0;
		for(int c : categories){
			mCategorySum += c;
		}
	}
	
	/**
	 * map the target words. if exist 1, 0 otherwise
	 * @param words the words to mark
	 * @return the marked list.
	 */
	//出现则为1,不出现为0. 返回标记集合 list。个数是所有唯一单词的数量
	public List<Integer> mapVector(String[] words){
		int[] temp = new int[mUniqueWords.size()];
		
		for(String word : words){
			int index = mUniqueWords.indexOf(word);
			temp[index] = index >=0 ? 1 : 0; //打标记，如果有就为1，否则0
		}
		return ArrayUtils.toList(temp);
	}
	
	public static class TrainResult{
		
		public float total1Ratio; //the ratio of 1 means true.
		public float[] p1Vector;
		public float[] p0Vector;
		
		@Override
		public String toString() {
			return "TrainResult [total1Ratio=" + total1Ratio + ", \n p1Vector=" + Arrays.toString(p1Vector) + ", \n p0Vector="
					+ Arrays.toString(p0Vector) + "]";
		}
		
	}
}
