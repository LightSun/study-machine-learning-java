package com.heaven7.java.math.processor;


/*public*/ class ShortProcessor extends NumberProcessor implements SimpleNumberProcessor{
	
	@Override
	public Number abs(Number n) {
		if(n instanceof Short){
			return Math.abs(n.shortValue());
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public Number add(Number main, Number n) {
		if(main instanceof Short){
			return main.shortValue() +  n.shortValue();
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public Number subtract(Number main, Number n) {
		if(main instanceof Short){
			return main.shortValue() -  n.shortValue();
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public Number multiply(Number main, Number n) {
		if(main instanceof Short){
			return main.shortValue() *  n.shortValue();
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public Number divide(Number main, Number n) {
		if(main instanceof Short){
			return main.shortValue() /  n.shortValue();
		}
		throw new UnsupportedOperationException();
	}
	
	

}
