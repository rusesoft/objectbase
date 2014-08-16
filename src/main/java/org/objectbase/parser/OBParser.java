package org.objectbase.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.objectbase.OBBlock;
import org.objectbase.OBException;
import org.objectbase.OBStatement;
import org.objectbase.OBPair;
import org.objectbase.OBType;
import org.objectbase.OBValue;
import org.objectbase.parser.OBLexer.ArtToken;
import org.objectbase.statement.CreateProjectStatement;
import org.objectbase.statement.DDLStatement;


public class OBParser {
	
	private final OBLexer scan;
	private final ArtToken dummyToken;
	private ArtToken token;
	
	private ParserContext parserContext;
	
	static final int EXPR = 0x1;
	static final int TYPE = 0x2;
	
	private int mode = 0;
	private int lastmode = 0;
	
	public OBParser(CharSequence command) {
        scan = new OBLexer(command);
        parserContext = new OBParserContext();
        dummyToken = scan.new ArtToken(OBTokenType.ERROR, 0, 0);
    }
	
	private void nextToken() {
		token = scan.nextToken();
	}
	
	private void nextIdentifier() {
		token = scan.nextIdentifier();
	}
	
	public OBBlock parseAndExecute() throws Exception {
		parserContext = new OBParserContext();
		
		//why nextToken here?
		nextToken();
		
		ArtToken beginToken = token;
		OBBlock block = new OBBlock();
		

		final ExecutorService excutorPool = Executors.newSingleThreadExecutor();
		List<Future<Object>> futures = new ArrayList<Future<Object>>();
		try {
			
			final OBStatement statement = parseStatement();
			if(statement == null) return null;
			block.addStatement(statement);
			//execute the statement right away in executor thread
			futures.add(excutorPool.submit(new Callable<Object>() {
				public Object call() throws OBException{
					statement.execute();
					return null;
				}
			}));
			
			if (statement instanceof DDLStatement) {
				block.setContainsDDL(true);
			}
			
			while (token.tokenType() == OBTokenType.SEMI) {
				nextToken();
				if (token.tokenType() == OBTokenType.RBRACE ||
					token.tokenType() == OBTokenType.EOF) {
					break;
				}
				
				final OBStatement state = parseStatement();
				block.addStatement(state);
				
				//execute the statement right away in executor thread
				futures.add(excutorPool.submit(new Callable<Object>() {
					public Object call() throws OBException{
						state.execute();
						return null;
					}
				}));
				
				if (!block.isContainsDDL() && statement instanceof DDLStatement) {
					block.setContainsDDL(true);
				}
			}
			
			//get from the future, not for result, but check whether there will be exception
			try {
				for (Future<Object> future : futures) {
					future.get();
				}
			} catch (Exception e) {
				throw e;
			}

		} catch (ExecutionException e) {
			throw e;
		} finally {
			excutorPool.shutdown();
		}
		
		block.setSyntaxPosition(beginToken.lineNumber(), beginToken.lineBeginPosition(), token.lineNumber(), token.lineEndPosition());
		
		if(token.tokenType() != OBTokenType.EOF) {
			parserContext.addParseError(token);
		}
		return block;
	}
	
	public OBBlock parse() {
		parserContext = new OBParserContext();
		nextToken();
		OBBlock block = parseBlock();
		if(token.tokenType() != OBTokenType.EOF) {
			parserContext.addParseError(token);
		}
		return block;
	}
	
	private OBBlock parseBlock() {
		ArtToken beginToken = token;
		OBBlock block = new OBBlock();
		OBStatement statement = parseStatement();
		block.addStatement(statement);
		
		if (statement instanceof DDLStatement) {
			block.setContainsDDL(true);
		}
		
		while (token.tokenType() == OBTokenType.SEMI) {
			nextToken();
			if (token.tokenType() == OBTokenType.RBRACE ||
				token.tokenType() == OBTokenType.EOF) {
				break;
			}
			
			statement = parseStatement();
			block.addStatement(statement);
			
			if (!block.isContainsDDL() && statement instanceof DDLStatement) {
				block.setContainsDDL(true);
			}
		}
		
		block.setSyntaxPosition(beginToken.lineNumber(), beginToken.lineBeginPosition(), token.lineNumber(), token.lineEndPosition());
		return block;
	}
	
	private OBStatement parseStatement() {
		OBStatement statement = null;
		switch (token.tokenType()) {
		case CREATE: case DROP:
			statement = parseDDLStatement();
			break;
		default:
			
		}
		return statement;
	}
	
	private OBStatement parseDDLStatement() {
		OBStatement statement = null;
		ArtToken beginToken = token;
		switch (token.tokenType()) {
		case CREATE: {
			nextToken();
			switch (token.tokenType()) {
			case PROJECT:
				statement = createProjectStatement();
				break;
			}
			break;
		}
		case DROP: {
			nextToken();
			switch (token.tokenType()) {
			case PROJECT:
				//statement = dropProjectStatement();
				break;
			}	
		}
		}
		statement.setSyntaxPosition(beginToken.lineNumber(), beginToken.lineBeginPosition(), token.lineNumber(), token.lineEndPosition());
		return statement;
	}

	private CreateProjectStatement createProjectStatement(){
		nextIdentifier();
		String projectId = token.tokenValue();
		nextToken();
		List<OBPair> properties = new ArrayList<OBPair>();
		while(token.tokenType() != OBTokenType.SEMI && token.tokenType() != OBTokenType.EOF) {
			OBPair pair = pair();
			accept(OBTokenType.COMMA);
			properties.add(pair);
		}
		accept(OBTokenType.SEMI);
		return new CreateProjectStatement(projectId, properties);
	}
	
	
	/*private DropProjectStatement dropProjectStatement(){
		
	}*/
	
	private OBPair pair() {
		String key = ident();
		accept(OBTokenType.EQ);
		OBValue value = literal();
		return new OBPair(key, value);
	}
	
	private String ident()	 {
		if (token.tokenType() == OBTokenType.IDENTIFIER) {
			String name = token.tokenValue();
			nextToken();
			return name;
		} else {
			parserContext.addParseError(token);
			return token.tokenValue();
		}
	}
	
	private void accept(OBTokenType tk) {
		if (token.tokenType() == tk) {
            nextToken();
        } else {
        	parserContext.addParseError(token);
        	nextToken();
        }
	}
	
	private int ignoreLeftParen() {
		int i = 0;
		while(token.tokenType() == OBTokenType.LPAREN) {
			nextToken();
			i++;
		}
		return i;
	}
	
	private void ignoreRightParen(int number) {
		while(number-- > 0) {
			if(token.tokenType() == OBTokenType.RPAREN) {
				nextToken();
			} else {
				parserContext.addParseError("expecte token: " + OBTokenType.RPAREN, token.lineNumber(), token.lineBeginPosition(), token.lineEndPosition());
			}
		}
	}
	
	private OBValue literal() {
		ArtToken beginToken = token;
		OBValue value = null;
		switch(token.tokenType()) {
		case INTLITERAL:
			value = new OBValue(OBType.INTEGER, Integer.valueOf(token.tokenValue()));
			break;
		case LONGLITERAL:
			value = new OBValue(OBType.LONG, Long.valueOf(token.tokenValue()));
			break;
		case FLOATLITERAL:
			value =new OBValue(OBType.FLOAT, Float.valueOf(token.tokenValue()));
			break;
		case DOUBLELITERAL:
			value = new OBValue(OBType.DOUBLE, Double.valueOf(token.tokenValue()));
			break;
		case CHARLITERAL:
			String charValue = token.tokenValue();
//			if(charValue.length() != 3 || !charValue.startsWith("\'") ||  !charValue.endsWith("\'")) {
//				parserContext.addParseError(token, "Illegal char literal");
//	        	nextToken();
//			}
			value = new OBValue(OBType.CHAR, charValue.charAt(0));
			break;
		case STRINGLITERAL: {
			String stringValue = token.tokenValue();
			if(stringValue.startsWith("\"") ) {
				stringValue = stringValue.substring(1);
			}
			if(stringValue.endsWith("\"") ) {
				stringValue = stringValue.substring(0, stringValue.length() - 1);
			}
			value = new OBValue(OBType.STRING, stringValue);
			break;
		}
		case TRUE:
			value = new OBValue(OBType.BOOLEAN, true);
			break;
		case FALSE:
			value = new OBValue(OBType.BOOLEAN, false);
			break;
		case NULL:
			value = new OBValue(OBType.NULL, null);
			break;
		default:
			break;
		}
		nextToken();
		value.setSyntaxPosition(beginToken.lineNumber(), beginToken.lineBeginPosition(), token.lineNumber(), token.lineEndPosition());
		return value;
	}
}
