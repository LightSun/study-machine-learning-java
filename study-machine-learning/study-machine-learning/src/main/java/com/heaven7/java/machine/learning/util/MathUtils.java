package com.heaven7.java.machine.learning.util;

import java.util.List;


public class MathUtils {
	
	public static float[] iterate(int[] dest, ArrayIterator<Integer, Float> ai){
		if(dest == null ){
			throw new NullPointerException();
		}
		if(dest.length == 0){
			throw new IllegalArgumentException();
		}
		float[] result = new float[dest.length];
		for(int size = dest.length, i = size -1 ; i>=0 ; i--){
			result[i] = ai.iterate(i, dest[i]);
		}
		return result;
	}
	
	public static float[] divide(int[] dest, float divisor){
		if(dest == null ){
			throw new NullPointerException();
		}
		if(dest.length == 0){
			throw new IllegalArgumentException();
		}
		float[] result = new float[dest.length];
		for(int size = dest.length, i = size -1 ; i>=0 ; i--){
			result[i] = dest[i] / divisor;
		}
		return result;
	}
	
	public static int sum(List<Integer> list){
		int sum = 0;
		for(int size = list.size(), i = size -1 ; i>=0 ; i--){
			sum += list.get(i);
		}
		return sum;
	}
	public static int sum(int[] arr){
		int sum = 0;
		for(int size = arr.length, i = size -1 ; i>=0 ; i--){
			sum += arr[i];
		}
		return sum;
	}
	public static float sum(float[] arr){
		float sum = 0;
		for(int size = arr.length, i = size -1 ; i>=0 ; i--){
			sum += arr[i];
		}
		return sum;
	}
	public static double sum(double[] arr){
		double sum = 0;
		for(int size = arr.length, i = size -1 ; i>=0 ; i--){
			sum += arr[i];
		}
		return sum;
	}
	
	//=============== matrix ====================
	/**
	 * make the every value of extra add to the dest';
	 * @param dest the dest array/matrix
	 * @param extra the extra array/matrix
	 */
	public static void sum(int[] dest, int[] extra){
		if(dest == null || extra == null){
			throw new NullPointerException();
		}
		if(dest.length != extra.length){
			throw new IllegalArgumentException();
		}
		for(int size = extra.length, i = size -1 ; i>=0 ; i--){
			dest[i] += extra[i];
		}
	}
	/**
	 * make the every value of extra add to the dest';
	 * @param dest the dest array/matrix
	 * @param extra the extra list/matrix
	 */
	public static void sum(int[] dest, List<Integer> extra){
		if(dest == null || extra == null){
			throw new NullPointerException();
		}
		if(dest.length != extra.size()){
			throw new IllegalArgumentException();
		}
		for(int size = extra.size(), i = size -1 ; i>=0 ; i--){
			dest[i] += extra.get(i);
		}
	}
	
	/**
	 * make the every value of extra multiply to the dest';
	 * @param dest the dest array/matrix
	 * @param extra the extra list/matrix
	 */
	public static float[] multiply(float[] dest, List<Integer> extra){
		if(dest == null || extra == null){
			throw new NullPointerException();
		}
		if(dest.length != extra.size()){
			throw new IllegalArgumentException();
		}
		float[] result = new float[dest.length];
		for(int size = extra.size(), i = size -1 ; i>=0 ; i--){
			result[i] = dest[i] * extra.get(i);
		}
		return result;
	}
	


}
