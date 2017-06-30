package com.heaven7.java.math.processor;


/*public*/ class LongProcessor extends NumberProcessor implements SimpleNumberProcessor{

	@Override
	public Number abs(Number n) {
		if(n instanceof Long){
			return Math.abs(n.longValue());
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public Number add(Number main, Number n) {
		if( main instanceof Long){
			return main.longValue() +  n.longValue();
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public Number subtract(Number main, Number n) {
		if(main instanceof Long){
			return main.longValue() -  n.longValue();
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public Number multiply(Number main, Number n) {
		if(main instanceof Long){
			return main.longValue() *  n.longValue();
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public Number divide(Number main, Number n) {
		if(main instanceof Long){
			return main.longValue() /  n.longValue();
		}
		throw new UnsupportedOperationException();
	}
	
	
	

}
