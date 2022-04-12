package com.heaven7.java.math;

import java.util.ArrayList;
import java.util.List;

import com.heaven7.java.base.util.ArrayUtils;
import com.heaven7.java.base.util.SparseArray;
import com.heaven7.java.base.util.Throwables;
import com.heaven7.java.math.processor.NumberProcessor;

public final class MatrixHelper {
	
	private final List<? extends Number> mList;
	private NumberProcessor mNumberProcessor;
	
	public MatrixHelper(List<? extends Number> list) {
		super();
		this.mList = list;
	}
	
	public void setNumberType(byte type){
		mNumberProcessor = NumberProcessor.getNumberProcessor(type);
		if(mNumberProcessor == null){
			throw new IllegalArgumentException("unsupport type");
		}
	}

	public List<? extends Number> multiply(List<? extends Number> other){
		Throwables.checkArgument(other.size() == mList.size(), "");
		List<Number> list = new ArrayList<>(other);
		for(int i = 0; i < list.size(); i++){
			list.add(mNumberProcessor.multiply(mList.get(i), other.get(i)));
		}
		return list;
	}
}
