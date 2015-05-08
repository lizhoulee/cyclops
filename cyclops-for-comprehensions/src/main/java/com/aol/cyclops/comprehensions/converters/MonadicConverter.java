package com.aol.cyclops.comprehensions.converters;

public interface MonadicConverter<T> {
	public boolean accept(Object o);
	public T convertToMonadicForm(Object f);
}
