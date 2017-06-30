package com.heaven7.java.math.processor;


/*public*/ class FloatProcessor extends NumberProcessor implements SimpleNumberProcessor{
	
	@Override
	public Number abs(Number n) {
		if(n instanceof Float){
			return Math.abs(n.floatValue());
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public Number add(Number main, Number n) {
		if(main instanceof Float){
			return main.floatValue() +  n.floatValue();
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public Number subtract(Number main, Number n) {
		if(main instanceof Float){
			return main.floatValue() -  n.floatValue();
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public Number multiply(Number main, Number n) {
		if( main instanceof Float){
			return main.floatValue() *  n.floatValue();
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public Number divide(Number main, Number n) {
		if(main instanceof Float){
			return main.floatValue() /  n.floatValue();
		}
		throw new UnsupportedOperationException();
	}
	
	

}
