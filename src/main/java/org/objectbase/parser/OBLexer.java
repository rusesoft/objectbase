package org.objectbase.parser;

import java.util.ArrayList;
import java.util.List;

public class OBLexer {

	private final CharSequence in;
	private final int length;
	
	private int currentPosition;
	private int lineNumber;
	private int lineCurrentPosition;

	private int startPostionOfToken;
	private int startLinePostionOfToken;
	
	private ArtToken currentToken;
	private List<ArtToken> savedTokens = new ArrayList<ArtToken>();
	
	public OBLexer(CharSequence in) {
		this.in = in;
		this.length = in.length();
		this.currentPosition = 0;
		this.lineNumber = 0;
		this.lineCurrentPosition = 0;
	}
	
	public class ArtToken {

		private final OBTokenType tokenType;
		private final int beginPos;
		private final int endPos;
		private final int lineNum;
		private final int lineBeginPos;
		private final int lineEndPos;

		private final String tokenValue;
		
		public ArtToken(OBTokenType tokeType, int beginPosition, int lineBeginPosition) {
			this.tokenType = tokeType;
			this.beginPos = beginPosition;
			this.endPos = currentPosition;
			this.tokenValue = in.subSequence(beginPosition, currentPosition).toString();
			this.lineNum = lineNumber;
			this.lineBeginPos = lineBeginPosition;
			this.lineEndPos = lineCurrentPosition;
		}
		
		public ArtToken(OBTokenType tokeType, int beginPosition, int lineBeginPosition, int endPosition, int lineEdPosition) {
			this.tokenType = tokeType;
			this.beginPos = beginPosition;
			this.endPos = endPosition;
			this.tokenValue = in.subSequence(beginPosition, endPosition).toString();
			this.lineNum = lineNumber;
			this.lineBeginPos = lineBeginPosition;
			this.lineEndPos = lineEdPosition;
		}
		
		public OBTokenType tokenType() {
			return tokenType;
		}
		
		public String tokenValue() {
			return tokenValue;
		}
		
		public int lineNumber() {
			return lineNum;
		}
		
		public int lineBeginPosition() {
			return lineBeginPos;
		}
		
		public int lineEndPosition() {
			return lineEndPos;
		}
		
		public int beginPosition() {
			return beginPos;
		}
		
		public int endPosition() {
			return endPos;
		}
		
		public String toString() {
			return tokenValue;
		}
	}
	
	public ArtToken getCurrentToken() {
		return this.currentToken;
	}
	
//	public ArtTokenType getTokenType() {
//		return this.currentToken.tokenType();
//	}
//	
//	public String getTokenValue() {
//		return this.currentToken.tokenValue();
//	}
	
	public ArtToken getToken(int lookahead) {
		if(lookahead == 0) {
			return currentToken;
		} else {
			ensureLookahead(lookahead);
			return savedTokens.get(lookahead - 1);
		}
	}

	private void ensureLookahead(int lookahead) {
		for (int i = savedTokens.size(); i < lookahead; i++) {
			savedTokens.add(nextToken());
		}
	}
	
	public ArtToken nextToken() {
		if (!savedTokens.isEmpty()) {
			currentToken = savedTokens.remove(0);
		} else {
			currentToken = next();
		}
		
		return currentToken;
	}
	
	public ArtToken nextIdentifier() {
		currentToken = nextIdent();
		return currentToken;
	}
	
	private ArtToken nextIdent() {
		// Keep track of where we started
        skipWhitespace();
        
        // If we're done, mark the end of the statement
        if (!hasNext()) {
            return new ArtToken(OBTokenType.EOF, currentPosition, lineCurrentPosition);
        }
        
        // We need to keep track of where this token's significant text began
        startPostionOfToken = this.currentPosition;
        startLinePostionOfToken = this.lineCurrentPosition;
        
        // Read the first character
        while (hasNext() && peekChar() != ' ') {
            nextChar();
       }
        return new ArtToken(OBTokenType.IDENTIFIER, startPostionOfToken, startLinePostionOfToken);
	}
	
	private ArtToken next() {
		// Keep track of where we started
        skipWhitespace();
        
        // If we're done, mark the end of the statement
        if (!hasNext()) {
            return new ArtToken(OBTokenType.EOF, currentPosition, lineCurrentPosition);
        }
        
        // We need to keep track of where this token's significant text began
        startPostionOfToken = this.currentPosition;
        startLinePostionOfToken = this.lineCurrentPosition;
        
        // Read the first character
        char c = nextChar();
        
        if (Character.isDigit(c)) {
            return readNumeric(c);
        }
        else if (Character.isLetter(c) || c == '_' || c == '$') {
            return readIdentifier();
        }
        else {
        	switch (c) {
        	case '.':
        		if(hasNext() && peekChar() >= '0' && peekChar() <= '9') {
        			nextChar();
        			return scanFractionAndSuffix();
        		} else if (hasNext() && peekChar() == '.') {
        			nextChar();
        			if(hasNext() && peekChar() == '.') {
            			nextChar();
            			return new ArtToken(OBTokenType.ELLIPSIS, startPostionOfToken, startLinePostionOfToken);
            		} else {
            			return new ArtToken(OBTokenType.DOTDOT, startPostionOfToken, startLinePostionOfToken);
            		}
        		} else {
        			return new ArtToken(OBTokenType.DOT, startPostionOfToken, startLinePostionOfToken);
        		}
            case ':': 
            	if(hasNext() && peekChar() == ':') {
        			nextChar();
        			return new ArtToken(OBTokenType.COLCOL, startPostionOfToken, startLinePostionOfToken);
        		} else {
        			return new ArtToken(OBTokenType.COLON, startPostionOfToken, startLinePostionOfToken);
        		}
            case '\'':
            case '"': return readString(c);
            case '~':
            	return new ArtToken(OBTokenType.TILDE, startPostionOfToken, startLinePostionOfToken);
            case '[':
            	return new ArtToken(OBTokenType.LBRACKET, startPostionOfToken, startLinePostionOfToken);
            case ']':
            	return new ArtToken(OBTokenType.RBRACKET, startPostionOfToken, startLinePostionOfToken);
            case ';':
            	return new ArtToken(OBTokenType.SEMI, startPostionOfToken, startLinePostionOfToken);
            case '|': 
            	if(hasNext() && peekChar() == '|') {
        			nextChar();
        			return new ArtToken(OBTokenType.BARBAR, startPostionOfToken, startLinePostionOfToken);
        		} else if(hasNext() && peekChar() == '=') {
        			nextChar();
        			return new ArtToken(OBTokenType.BAREQ, startPostionOfToken, startLinePostionOfToken);
        		} else {
        			return new ArtToken(OBTokenType.BAR, startPostionOfToken, startLinePostionOfToken);
        		}
            case '(':
            	return new ArtToken(OBTokenType.LPAREN, startPostionOfToken, startLinePostionOfToken);
            case ')':
            	return new ArtToken(OBTokenType.RPAREN, startPostionOfToken, startLinePostionOfToken);
            case '{':
            	return new ArtToken(OBTokenType.LBRACE, startPostionOfToken, startLinePostionOfToken);
            case '}':
            	return new ArtToken(OBTokenType.RBRACE, startPostionOfToken, startLinePostionOfToken);
            case '^':
            	if(hasNext() && peekChar() == '=') {
        			nextChar();
        			return new ArtToken(OBTokenType.CARETEQ, startPostionOfToken, startLinePostionOfToken);
        		} else {
        			return new ArtToken(OBTokenType.CARET, startPostionOfToken, startLinePostionOfToken);
        		}
            case '&':
            	if(hasNext() && peekChar() == '&') {
        			nextChar();
        			return new ArtToken(OBTokenType.AMPAMP, startPostionOfToken, startLinePostionOfToken);
        		} else if(hasNext() && peekChar() == '=') {
        			nextChar();
        			return new ArtToken(OBTokenType.AMPEQ, startPostionOfToken, startLinePostionOfToken);
        		} else {
        			return new ArtToken(OBTokenType.AMP, startPostionOfToken, startLinePostionOfToken);
        		}
            case '@':
            	return new ArtToken(OBTokenType.MONKEYS_AT, startPostionOfToken, startLinePostionOfToken);
            case '?':
            	return new ArtToken(OBTokenType.QUES, startPostionOfToken, startLinePostionOfToken);
            case '*':
            	if(hasNext() && peekChar() == '=') {
        			nextChar();
        			return new ArtToken(OBTokenType.STAREQ, startPostionOfToken, startLinePostionOfToken);
        		} else {
        			return new ArtToken(OBTokenType.STAR, startPostionOfToken, startLinePostionOfToken);
        		}
            case '+':
            	if(hasNext() && peekChar() == '+') {
        			nextChar();
        			return new ArtToken(OBTokenType.PLUSPLUS, startPostionOfToken, startLinePostionOfToken);
        		} else if(hasNext() && peekChar() == '=') {
        			nextChar();
        			return new ArtToken(OBTokenType.PLUSEQ, startPostionOfToken, startLinePostionOfToken);
        		} else {
        			return new ArtToken(OBTokenType.PLUS, startPostionOfToken, startLinePostionOfToken);
        		}
            case '-':
            	if(hasNext() && peekChar() == '-') {
        			nextChar();
        			return new ArtToken(OBTokenType.SUBSUB, startPostionOfToken, startLinePostionOfToken);
        		} else if(hasNext() && peekChar() == '=') {
        			nextChar();
        			return new ArtToken(OBTokenType.SUBEQ, startPostionOfToken, startLinePostionOfToken);
        		} else if(hasNext() && peekChar() == '>') {
        			nextChar();
        			return new ArtToken(OBTokenType.ARROW, startPostionOfToken, startLinePostionOfToken);
        		} else {
        			return new ArtToken(OBTokenType.SUB, startPostionOfToken, startLinePostionOfToken);
        		}
            case '/':
                if (hasNext() && (peekChar() == '*')) {
                    // This is to go over the * character
                    nextChar();
                    do {
                        c = nextChar();
                    } while (c != '*' || peekChar() != '/');
                    // This is to go over the / character
                    nextChar();
                    return new ArtToken(OBTokenType.COMMENT, startPostionOfToken, startLinePostionOfToken);
                } else if(hasNext() && (peekChar() == '=')){
                	return new ArtToken(OBTokenType.SLASHEQ, startPostionOfToken, startLinePostionOfToken);
                } else {
                	return new ArtToken(OBTokenType.SLASH, startPostionOfToken, startLinePostionOfToken);
                }
            case '%':
            	if(hasNext() && peekChar() == '=') {
        			nextChar();
        			return new ArtToken(OBTokenType.PERCENTEQ, startPostionOfToken, startLinePostionOfToken);
        		} else {
        			return new ArtToken(OBTokenType.PERCENT, startPostionOfToken, startLinePostionOfToken);
        		}
            //case '\\':
            case ',':
            	return new ArtToken(OBTokenType.COMMA, startPostionOfToken, startLinePostionOfToken);
            //case '#':
            case '=':
            	if(hasNext() && peekChar() == '=') {
        			nextChar();
        			return new ArtToken(OBTokenType.EQEQ, startPostionOfToken, startLinePostionOfToken);
        		} else {
        			return new ArtToken(OBTokenType.EQ, startPostionOfToken, startLinePostionOfToken);
        		}
            case '>':
            	if(hasNext() && peekChar() == '=') {
        			nextChar();
        			return new ArtToken(OBTokenType.GTEQ, startPostionOfToken, startLinePostionOfToken);
        		} else if(hasNext() && peekChar() == '>') {
        			nextChar();
        			if(hasNext() && peekChar() == '=') {
            			nextChar();
            			return new ArtToken(OBTokenType.GTGTEQ, startPostionOfToken, startLinePostionOfToken);
            		} else if(hasNext() && peekChar() == '>') {
            			nextChar();
            			if(hasNext() && peekChar() == '=') {
                			nextChar();
                			return new ArtToken(OBTokenType.GTGTGTEQ, startPostionOfToken, startLinePostionOfToken);
                		} else {
                			return new ArtToken(OBTokenType.GTGTGT, startPostionOfToken, startLinePostionOfToken);
                		}
            		} else {
            			return new ArtToken(OBTokenType.GTGT, startPostionOfToken, startLinePostionOfToken);
            		}
        		} else {
        			return new ArtToken(OBTokenType.GT, startPostionOfToken, startLinePostionOfToken);
        		}
            case '<':
                if (hasNext() && peekChar() == '=') {
                    // This is to go over the = character
                    nextChar();
                    return new ArtToken(OBTokenType.LTEQ, startPostionOfToken, startLinePostionOfToken);
                }
                else if (hasNext() && peekChar() == '<') {
                    // This is to go over the > character
                    nextChar();
                    if (hasNext() && peekChar() == '=') {
                        // This is to go over the = character
                        nextChar();
                        return new ArtToken(OBTokenType.LTLTEQ, startPostionOfToken, startLinePostionOfToken);
                    }
                    else {
                        return new ArtToken(OBTokenType.LTLT, startPostionOfToken, startLinePostionOfToken);
                    }
                }
                else if (hasNext() && peekChar() == '>') {
                    // This is to go over the > character
                    nextChar();
                    return new ArtToken(OBTokenType.BANGEQ, startPostionOfToken, startLinePostionOfToken);
                }
                else {
                	return new ArtToken(OBTokenType.LT, startPostionOfToken, startLinePostionOfToken);
                }
            case '!':
                if (hasNext() && peekChar() == '=') {
                    // This is to go over the = character
                    nextChar();
                    return new ArtToken(OBTokenType.BANGEQ, startPostionOfToken, startLinePostionOfToken);
                }
                else {
                	return new ArtToken(OBTokenType.BANG, startPostionOfToken, startLinePostionOfToken);
                }
            default:
                return null;
            }
        }
        
	}
	
	private void skipWhitespace() {
        while (hasNext() && Character.isWhitespace(peekChar())) {
            nextChar();
        }
    }
	
	private ArtToken readString(char c) {
		//ignore the ' or ", and reset start position
		startPostionOfToken = this.currentPosition;
        startLinePostionOfToken = this.lineCurrentPosition;
		if(c == '\'') {
			while(hasNext() && peekChar() != '\'') {
				nextChar();
			} 
			if(hasNext() && peekChar() == '\'') {
				nextChar();
				return new ArtToken(OBTokenType.CHARLITERAL, startPostionOfToken, startLinePostionOfToken, 
						currentPosition-1, lineCurrentPosition-1);
			}
		} else if (c == '"') {
			while(hasNext() && peekChar() != '"') {
				nextChar();
			} 
			if(hasNext() && peekChar() == '"') {
				nextChar();
				return new ArtToken(OBTokenType.STRINGLITERAL, startPostionOfToken, startLinePostionOfToken,
						currentPosition-1, lineCurrentPosition-1);
			}		
		}
		return null;
	}
	
	private ArtToken readNumeric(char c) {
		
		// If the first character is a plus or minus, skip over it.
        if (c == '+' || c == '-') {
            nextChar();
        }
        
        // As long as the next character will be a digit, we should include it.
        while (hasNext() && (Character.isDigit(peekChar()) || (peekChar() == 'e' || peekChar() == 'E'))) {
            nextChar();
        }
        
        // If the next character is a decimal point, continue.
        if (hasNext() && peekChar() == '.' ) {
            nextChar();
            return scanFractionAndSuffix();
        } else if(hasNext() && 
       			(peekChar() == 'd' || peekChar() == 'D') ||
       			(peekChar() == 'f' || peekChar() == 'F') ||
       			(peekChar() == 'l' || peekChar() == 'L') ||
            	(peekChar() == 'i' || peekChar() == 'I') ||
            	(peekChar() == 's' || peekChar() == 'S') ||
            	(peekChar() == 'b' || peekChar() == 'B')) {
			char ch = nextChar();
			switch(ch) {
			case 'd': case 'D':
				return new ArtToken(OBTokenType.DOUBLELITERAL, startPostionOfToken-1, startLinePostionOfToken-1); 
			case 'f': case 'F':
				return new ArtToken(OBTokenType.FLOATLITERAL, startPostionOfToken-1, startLinePostionOfToken-1);
			case 'l': case 'L':
				return new ArtToken(OBTokenType.LONGLITERAL, startPostionOfToken-1, startLinePostionOfToken-1); 
			default :
				return new ArtToken(OBTokenType.INTLITERAL, startPostionOfToken-1, startLinePostionOfToken-1); 
			}
		} else {
			return new ArtToken(OBTokenType.INTLITERAL, startPostionOfToken-1, startLinePostionOfToken-1); 
        }
	}
	
	private ArtToken scanFractionAndSuffix() {
		while (hasNext() && Character.isDigit(peekChar())) {
			nextChar();
		}
		if(hasNext() && (peekChar() == 'f' || peekChar() == 'F')) {
			nextChar();
			return new ArtToken(OBTokenType.FLOATLITERAL, startPostionOfToken, startLinePostionOfToken);
		} else if(hasNext() && (peekChar() == 'd' || peekChar() == 'D')) {
			nextChar();
			return new ArtToken(OBTokenType.DOUBLELITERAL, startPostionOfToken, startLinePostionOfToken); 
		} else {
			String numLiteral = in.subSequence(startPostionOfToken, currentPosition).toString();
			double number = Double.valueOf(numLiteral);
			if (number <= Float.MAX_VALUE) {
				return new ArtToken(OBTokenType.FLOATLITERAL, startPostionOfToken, startLinePostionOfToken);
			} else {
				return new ArtToken(OBTokenType.DOUBLELITERAL, startPostionOfToken, startLinePostionOfToken); 
			}
		}
	}
	
	private boolean isValidIdentifier(char c) {
        return ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')||
                c == '_' || c == '$' || Character.isDigit(c));
    }
	
	private ArtToken readIdentifier() {
		while (hasNext() && isValidIdentifier(peekChar())) {
            nextChar();
       }
       
       String word = in.subSequence(this.startPostionOfToken, this.currentPosition).toString();
       OBTokenType wordType = OBReservedWord.getTokenType(word.toLowerCase());
       if (wordType == null) {
           wordType = OBTokenType.IDENTIFIER;
       }
       
       return new ArtToken(wordType, startPostionOfToken, startLinePostionOfToken); 
	}
	
	private char nextChar(){
        this.lineCurrentPosition++;
        char next = in.charAt(this.currentPosition++);

        if (next == '\n') {
            this.lineNumber++;
            this.lineCurrentPosition = 0;
        }
        
        return next;
    }
	
	private char peekChar() {
        return in.charAt(currentPosition);
    }
	
	private boolean hasNext() {
        return currentPosition < length;
    }
	
	public ArtToken split(ArtToken token) {
		String tokenName = token.tokenType().getName();
		OBTokenType tokenType = OBTokenType.lookupByName(tokenName.substring(0, 1));
		this.currentPosition = token.beginPos + 1;
		this.lineCurrentPosition = token.lineBeginPos + 1;
		ArtToken prevToken = new ArtToken(tokenType, token.beginPos, token.lineBeginPos);
		this.currentToken = this.next();	 
		return prevToken;
	}
}
