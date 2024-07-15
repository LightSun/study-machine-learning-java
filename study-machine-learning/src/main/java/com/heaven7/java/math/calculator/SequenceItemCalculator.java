package com.heaven7.java.math.calculator;

import java.util.ArrayList;

import com.heaven7.java.math.ItemCalculator;

public class SequenceItemCalculator implements ItemCalculator{
	
	private final ArrayList<ItemCalculator> mCalculators = new ArrayList<>();
	
	public void addItemCalculator(ItemCalculator calculator){
		this.mCalculators.add(calculator);
	}
	public void removeItemCalculator(ItemCalculator calculator){
		this.mCalculators.remove(calculator);
	}
	public void clearItemCalculator(ItemCalculator calculator){
		this.mCalculators.clear();
	}

	@Override
	public Number calculate(int index, Number num) {
		if(mCalculators.isEmpty()){
			return null;
		}
		Number result = mCalculators.remove(0).calculate(index, num);
		for(ItemCalculator c : mCalculators){
			result = c.calculate(index, result);
		}
		return result;
	}

}
