package com.heaven7.java.machine.learning.util;

public interface ArrayIterator<T, R> {
	R iterate(int index, T t);
}