package org.objectbase;

public abstract class AbstractSyntax implements OBSyntax {

	private SyntaxPosition syntaxPosition;
	
	private boolean isCompiled;
	
	public AbstractSyntax() {}
	
	@Override
	public void setSyntaxPosition(int beginLineNum, int  beginLinePos, int endLineNum, int endLinePos) {
		this.syntaxPosition = new SyntaxPosition(beginLineNum, beginLinePos, endLineNum, endLinePos);
	}
	
	@Override
	public SyntaxPosition getSyntaxPosition() {
		return syntaxPosition;
	}

	@Override
	public boolean isCompiled() {
		return isCompiled;
	}

	@Override
	public void setCompiled(boolean isCompiled) {
		this.isCompiled = isCompiled;
	}

}
