package org.objectbase.parser;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OBReservedWord {

	private final static Map<String, OBTokenType> reserved = new ConcurrentHashMap<String, OBTokenType>();
	
	static {
		reserved.put("if", OBTokenType.IF);
		reserved.put("else", OBTokenType.ELSE);
		reserved.put("for", OBTokenType.FOR);
		reserved.put("and", OBTokenType.AND);
		reserved.put("or", OBTokenType.OR);
		reserved.put("not", OBTokenType.NOT);
		reserved.put("like", OBTokenType.LIKE);
		reserved.put("switch", OBTokenType.SWITCH);
		reserved.put("case", OBTokenType.CASE);
		reserved.put("do", OBTokenType.DO);
		reserved.put("while", OBTokenType.WHILE);
		reserved.put("continue", OBTokenType.CONTINUE);
		reserved.put("break", OBTokenType.BREAK);
		reserved.put("return", OBTokenType.RETURN);
		reserved.put("null", OBTokenType.NULL);
		reserved.put("try", OBTokenType.TRY);
		reserved.put("catch", OBTokenType.CATCH);
		reserved.put("finally", OBTokenType.FINALLY);
		reserved.put("throw", OBTokenType.THROW);
		reserved.put("throws", OBTokenType.THROWS);
		reserved.put("public", OBTokenType.PUBLIC);
		reserved.put("protected", OBTokenType.PROTECTED);
		reserved.put("private", OBTokenType.PRIVATE);
		reserved.put("final", OBTokenType.FINAL);
		reserved.put("static", OBTokenType.STATIC);
		reserved.put("default", OBTokenType.DEFAULT);
		reserved.put("void", OBTokenType.VOID);
		reserved.put("this", OBTokenType.THIS);
		reserved.put("super", OBTokenType.SUPER);
		reserved.put("new", OBTokenType.NEW);
		
		reserved.put("true", OBTokenType.TRUE);
		reserved.put("false", OBTokenType.FALSE);
		
		reserved.put("boolean", OBTokenType.BOOLEAN);
		reserved.put("byte", OBTokenType.BYTE);
		reserved.put("short", OBTokenType.SHORT);
		reserved.put("char", OBTokenType.CHAR);
		reserved.put("int", OBTokenType.INT);
		reserved.put("long", OBTokenType.LONG);
		reserved.put("float", OBTokenType.FLOAT);
		reserved.put("double", OBTokenType.DOUBLE);
		
		reserved.put("create", OBTokenType.CREATE);
		reserved.put("drop", OBTokenType.DROP);
		reserved.put("project", OBTokenType.PROJECT);
		reserved.put("entity", OBTokenType.ENTITY);
		reserved.put("class", OBTokenType.CLASS);
		
		reserved.put("print", OBTokenType.PRINT);
		reserved.put("println", OBTokenType.PRINTLN);
		
		
	}
	
	public static OBTokenType getTokenType(String word) {
		return reserved.get(word);
	}
}
