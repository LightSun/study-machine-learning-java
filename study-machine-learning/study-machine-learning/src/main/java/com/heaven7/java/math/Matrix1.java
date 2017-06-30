package com.heaven7.java.math;

import java.util.List;

import com.heaven7.java.base.util.ArrayUtils;
import com.heaven7.java.base.util.SparseArray;
import com.heaven7.java.base.util.Throwables;
import com.heaven7.java.math.processor.NumberProcessor;

public final class Matrix1 {
	
	private final List<? extends Number> mList;
	private NumberProcessor mNumberProcessor;
	
	public Matrix1(List<? extends Number> list) {
		super();
		this.mList = list;
	}
	
	public void setNumberType(byte type){
		mNumberProcessor = NumberProcessor.getNumberProcessor(type);
		if(mNumberProcessor == null){
			throw new IllegalArgumentException("unsupport type");
		}
	}
	
}
