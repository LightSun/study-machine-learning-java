package com.heaven7.java.math.processor;

import java.math.BigDecimal;
import java.math.BigInteger;

/*public*/ class BigIntegerProcessor extends NumberProcessor{
	
	private BigInteger convertToBigInteger(Number n) {
		if( n instanceof BigInteger){
			return (BigInteger) n;
		}
		if( n instanceof BigDecimal){
			return ((BigDecimal) n).toBigInteger();
		}
		return BigInteger.valueOf(n.longValue());
	}
	
	@Override
	public Number abs(Number n) {
		if(n instanceof BigInteger){
			return ((BigInteger) n).abs();
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public Number add(Number main, Number n) {
		if(main instanceof BigInteger){
			return ((BigInteger)main).add(convertToBigInteger(n));
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public Number subtract(Number main, Number n) {
		if(main instanceof BigInteger){
			return ((BigInteger)main).subtract(convertToBigInteger(n));
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public Number multiply(Number main, Number n) {
		if(main instanceof BigInteger){
			return ((BigInteger)main).multiply(convertToBigInteger(n));
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public Number divide(Number main, Number n) {
		if(main instanceof BigInteger){
			return ((BigInteger)main).divide(convertToBigInteger(n));
		}
		throw new UnsupportedOperationException();
	}

}
