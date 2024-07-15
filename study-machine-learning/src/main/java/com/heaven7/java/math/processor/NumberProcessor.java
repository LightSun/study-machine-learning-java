package com.heaven7.java.math.processor;

import com.heaven7.java.base.util.SparseArray;

public abstract class NumberProcessor {
	
	public static final byte TYPE_INT          = 1;
	public static final byte TYPE_FLOAT        = 2;
	public static final byte TYPE_DOUBLE       = 3;
	public static final byte TYPE_LONG         = 4;
	public static final byte TYPE_SHORT        = 5;
	public static final byte TYPE_BIGINTEGER   = 6;
	public static final byte TYPE_BIGDECIMAL   = 7;
	
    private static final SparseArray<NumberProcessor> sProcessorMap;
	
	static{
		sProcessorMap = new SparseArray<NumberProcessor>();
		sProcessorMap.put(TYPE_BIGDECIMAL, new BigDecimalProcessor());
		sProcessorMap.put(TYPE_BIGINTEGER, new BigIntegerProcessor());
		sProcessorMap.put(TYPE_INT, new IntegerProcessor());
		sProcessorMap.put(TYPE_FLOAT, new FloatProcessor());
		sProcessorMap.put(TYPE_DOUBLE, new DoubleProcessor());
		sProcessorMap.put(TYPE_LONG, new LongProcessor());
		sProcessorMap.put(TYPE_SHORT, new ShortProcessor());
	}
	
	public static NumberProcessor getNumberProcessor(byte type){
		return sProcessorMap.get(type);
	}
	
	public abstract  Number abs(Number n);

	public abstract  Number add(Number main, Number n);

	public abstract  Number subtract(Number main, Number n);

	public abstract  Number multiply(Number main, Number n);

	public abstract  Number divide(Number main, Number n);
}