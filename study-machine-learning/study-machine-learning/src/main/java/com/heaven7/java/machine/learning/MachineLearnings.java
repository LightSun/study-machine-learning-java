package com.heaven7.java.machine.learning;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.heaven7.java.base.anno.IntDef;

public final class MachineLearnings {
	
	public static final byte SCOPE_PREPROCCESS = 0x1;
	public static final byte SCOPE_ANALYSE     = 0x2;
	public static final byte SCOPE_TRAINING    = 0x4;
	public static final byte SCOPE_TEST        = 0x8;
	public static final byte SCOPE_USAGE       = 0x10;
	
	//public static final byte SCOPE_REAL_TIME_PROCCESS    = 0x20;

	
	@IntDef(value = {
			SCOPE_PREPROCCESS,
			SCOPE_ANALYSE,
			SCOPE_TRAINING,
			SCOPE_TEST,
			SCOPE_USAGE,
	}, flag = true)
	@interface ScopeFlags {
	}
	
	/**
	 * the scope of machine learning
	 * @author heaven7
	 */
	@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
	@Retention(RetentionPolicy.CLASS)
	public @interface Scope {
		@ScopeFlags int value() default SCOPE_TRAINING;
	}
}
