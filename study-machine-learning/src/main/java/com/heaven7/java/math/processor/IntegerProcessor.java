package com.heaven7.java.math.processor;

/*public*/ class IntegerProcessor extends NumberProcessor implements SimpleNumberProcessor{
	
	@Override
	public Number abs(Number n) {
		if(n instanceof Integer){
			return Math.abs(n.intValue());
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public Number add(Number main, Number n) {
		if(main instanceof Integer){
			return main.intValue() +  n.intValue();
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public Number subtract(Number main, Number n) {
		if(main instanceof Integer){
			return main.intValue() - n.intValue();
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public Number multiply(Number main, Number n) {
		if(main instanceof Integer){
			return main.intValue() * n.intValue();
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public Number divide(Number main, Number n) {
		if(main instanceof Integer){
			 return main.intValue() / n.intValue();
		}
		throw new UnsupportedOperationException();
	}

}
