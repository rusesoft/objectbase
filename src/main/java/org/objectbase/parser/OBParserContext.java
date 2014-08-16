package org.objectbase.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.objectbase.parser.OBLexer.ArtToken;


public class OBParserContext implements ParserContext {

	private final List<SyntaxCheck> warnings = new ArrayList<SyntaxCheck>();
	private final List<SyntaxCheck> errors = new ArrayList<SyntaxCheck>();
	
	private int dataStackMaxSize = 20;
	private int dataStackLevel = -1;
	
	private class SyntaxCheck {
		private final String message;
		private final int line;
		private final int beginPos;
		private final int endPos;
		
		public SyntaxCheck(String message, int line, int beginPos, int endPos) {
			this.message = message;
			this.line = line;
			this.beginPos = beginPos;
			this.endPos = endPos;
		}

		public String getMessage() {
			return message;
		}

		public int getLine() {
			return line;
		}

		public int getBeginPos() {
			return beginPos;
		}
		
		public int getEndPos() {
			return endPos;
		}
	}
	
	
	public void addParseWarning(ArtToken token) {
		this.addParseWarning("unexpected token: "+ token.tokenValue(), token.lineNumber(), token.lineBeginPosition(), token.lineEndPosition());
	}
	
	public void addParseWarning(String message, int line, int beginPos, int endPos) {
		warnings.add(new SyntaxCheck(message, line, beginPos, endPos));
	}
	
	public void addParseError(String message, int line, int beginPos, int endPos) {
		errors.add(new SyntaxCheck(message, line, beginPos, endPos));
	}
	
	public void addParseError(ArtToken token) {
		this.addParseError("unexpected token: "+ token.tokenValue(), token.lineNumber(), token.lineBeginPosition(), token.lineEndPosition());
	}
	
	public void addParseError(ArtToken token, String message) {
		this.addParseError(message + " for token: "+ token.tokenValue(), token.lineNumber(), token.lineBeginPosition(), token.lineEndPosition());
	}

}
