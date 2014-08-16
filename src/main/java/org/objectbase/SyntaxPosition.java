package org.objectbase;


public class SyntaxPosition {
	
	private final int beginLineNum;
	private final int beginLinePos;
	private final int endLineNum;
	private final int endLinePos;
	
	public SyntaxPosition(int beginLineNum, int  beginLinePos, int endLineNum, int endLinePos) {
		this.beginLineNum = beginLineNum;
		this.beginLinePos = beginLinePos;
		this.endLineNum = endLineNum;
		this.endLinePos = endLinePos;
	}

	public int getBeginLineNum() {
		return beginLineNum;
	}

	public int getBeginLinePos() {
		return beginLinePos;
	}

	public int getEndLineNum() {
		return endLineNum;
	}

	public int getEndLinePos() {
		return endLinePos;
	}

}
