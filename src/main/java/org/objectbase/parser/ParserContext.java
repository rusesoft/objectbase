package org.objectbase.parser;

import org.objectbase.parser.OBLexer.ArtToken;

public interface ParserContext {

	public void addParseWarning(String message, int line, int beginPos, int endPos);
	public void addParseWarning(ArtToken token);
	public void addParseError(String message, int line, int beginPos, int endPos);
	public void addParseError(ArtToken token);
	public void addParseError(ArtToken token, String message);
}
