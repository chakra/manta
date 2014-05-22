package com.espendwise.manta.util.parser;


public interface AppParser<T> {
	public T parse(String parserValue, Object... params) throws AppParserException;
}
