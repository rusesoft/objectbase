package org.objectbase.parser;



public enum OBTokenType {
	EOF(),
    ERROR(),
    AND("and"),
	OR("or"),
	NOT("not"),
	LIKE("like"),
    IDENTIFIER(Tag.NAMED),
    ABSTRACT("abstract"),
    ASSERT("assert", Tag.NAMED),
    BOOLEAN("boolean", Tag.NAMED),
    BREAK("break"),
    BYTE("byte", Tag.NAMED),
    CASE("case"),
    CATCH("catch"),
    CHAR("char", Tag.NAMED),
    
    CONST("const"),
    CONTINUE("continue"),
    DEFAULT("default"),
    DO("do"),
    DOUBLE("double", Tag.NAMED),
    ELSE("else"),
    ENUM("enum", Tag.NAMED),
    EXTENDS("extends"),
    FINAL("final"),
    FINALLY("finally"),
    FLOAT("float", Tag.NAMED),
    FOR("for"),
    GOTO("goto"),
    IF("if"),
    IMPLEMENTS("implements"),
    IMPORT("import"),
    INSTANCEOF("instanceof"),
    INT("int", Tag.NAMED),
    INTERFACE("interface"),
    LONG("long", Tag.NAMED),
    NATIVE("native"),
    NEW("new"),
    PACKAGE("package"),
    PRIVATE("private"),
    PROTECTED("protected"),
    PUBLIC("public"),
    RETURN("return"),
    SHORT("short", Tag.NAMED),
    
    STRICTFP("strictfp"),
    SUPER("super", Tag.NAMED),
    SWITCH("switch"),
    SYNCHRONIZED("synchronized"),
    THIS("this", Tag.NAMED),
    THROW("throw"),
    THROWS("throws"),
    TRANSIENT("transient"),
    TRY("try"),
    VOID("void", Tag.NAMED),
    VOLATILE("volatile"),
    WHILE("while"),
    
    INTLITERAL(Tag.NUMERIC),
    LONGLITERAL(Tag.NUMERIC),
    FLOATLITERAL(Tag.NUMERIC),
    DOUBLELITERAL(Tag.NUMERIC),
    
    CHARLITERAL(Tag.NUMERIC),
    
    STRINGLITERAL(Tag.STRING),
    
    TRUE("true", Tag.NAMED),
    FALSE("false", Tag.NAMED),
    NULL("null", Tag.NAMED),
    UNDERSCORE("_", Tag.NAMED),
    ARROW("->"),
    COLCOL("::"),
    LPAREN("("),
    RPAREN(")"),
    LBRACE("{"),
    RBRACE("}"),
    LBRACKET("["),
    RBRACKET("]"),
    SEMI(";"),
    COMMA(","),
    DOT("."),
    DOTDOT("."),
    ELLIPSIS("..."),
    EQ("="),
    GT(">"),
    LT("<"),
    BANG("!"),
    TILDE("~"),
    QUES("?"),
    COLON(":"),
    EQEQ("=="),
    LTEQ("<="),
    GTEQ(">="),
    BANGEQ("!="),
    AMPAMP("&&"),
    BARBAR("||"),
    PLUSPLUS("++"),
    SUBSUB("--"),
    PLUS("+"),
    SUB("-"),
    STAR("*"),
    SLASH("/"),
    AMP("&"),
    BAR("|"),
    CARET("^"),
    PERCENT("%"),
    LTLT("<<"),
    GTGT(">>"),
    GTGTGT(">>>"),
    PLUSEQ("+="),
    SUBEQ("-="),
    STAREQ("*="),
    SLASHEQ("/="),
    AMPEQ("&="),
    BAREQ("|="),
    CARETEQ("^="),
    PERCENTEQ("%="),
    LTLTEQ("<<="),
    GTGTEQ(">>="),
    GTGTGTEQ(">>>="),
    MONKEYS_AT("@"),
    COMMENT(),
    
    CREATE("create"),
    DROP("drop"),
    PROJECT("project"),
    ENTITY("entity"),
    CLASS("class"),
    STATIC("static"),
    
    
    PRINT("print"),
    PRINTLN("println"),
    
    
    CUSTOM;

	enum Tag {
		DEFAULT,
		NAMED,
		STRING,
		NUMERIC,
		MODIFIER;
	}
	
    private final String name;
    private final Tag tag;
    
    OBTokenType() {
    	this(null, Tag.DEFAULT);
    }
    
    OBTokenType(String name) {
    	this(name, Tag.DEFAULT);
    }
    
    OBTokenType(Tag tag) {
    	this(null, tag);
    }
    
    OBTokenType(String name, Tag tag) {
    	this.name = name;
    	this.tag = tag;
    }

    public static OBTokenType lookupByName(String name) {
    	for (OBTokenType tokenType : OBTokenType.values()) {
			if(name.equalsIgnoreCase(tokenType.getName())) {
				return tokenType;
			}
		}
		return null;
    }
	public String getName() {
		return name;
	}

	public Tag getTag() {
		return tag;
	}
}
