package org.objectbase;


public interface OBStatement extends OBSyntax{
	public void execute() throws OBException;
	public void compile() throws OBException;
}
