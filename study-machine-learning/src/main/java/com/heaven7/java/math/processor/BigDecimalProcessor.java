package com.heaven7.java.math.processor;

import java.math.BigDecimal;
import java.math.BigInteger;

/*public*/ class BigDecimalProcessor extends NumberProcessor{
	
	private BigDecimal convertToBigDecimal(Number n) {
		if( n instanceof BigDecimal){
			return (BigDecimal) n;
		}
		if(n instanceof BigInteger){
			return new BigDecimal((BigInteger)n);
		}
		if(n instanceof Float || n instanceof Double){
			return BigDecimal.valueOf(n.doubleValue());
		}
		return BigDecimal.valueOf(n.longValue());
	}
	
	@Override
	public Number abs(Number n) {
		if(n instanceof BigDecimal){
			return ((BigDecimal) n).abs();
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public Number add(Number main, Number n) {
		if(main instanceof BigDecimal){
			return ((BigDecimal)main).add(convertToBigDecimal(n));
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public Number subtract(Number main, Number n) {
		if(main instanceof BigDecimal){
			return ((BigDecimal)main).subtract(convertToBigDecimal(n));
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public Number multiply(Number main, Number n) {
		if(main instanceof BigDecimal){
			return ((BigDecimal)main).multiply(convertToBigDecimal(n));
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public Number divide(Number main, Number n) {
		if(main instanceof BigDecimal){
			return ((BigDecimal)main).divide(convertToBigDecimal(n));
		}
		throw new UnsupportedOperationException();
	}

}
