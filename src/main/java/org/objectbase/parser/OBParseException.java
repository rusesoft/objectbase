package org.objectbase.parser;

import org.objectbase.OBException;

public class OBParseException extends OBException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1242623486750151292L;

	public OBParseException(int line, int linePos, String detail) {
		super("Syntax error at line " + line + "."+linePos+": "+detail);
	}
}
