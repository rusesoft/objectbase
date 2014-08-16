package org.objectbase;


public interface OBSyntax {

	public void setSyntaxPosition(int beginLineNum, int  beginLinePos, int endLineNum, int endLinePos);
	public SyntaxPosition getSyntaxPosition();
	
	public boolean isCompiled();
	public void setCompiled(boolean isCompiled);
}
