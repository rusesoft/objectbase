package org.objectbase.statement;

import org.objectbase.AbstractSyntax;
import org.objectbase.OBException;
import org.objectbase.OBStatement;


public abstract class DDLStatement extends AbstractSyntax implements OBStatement{

	public abstract void execute() throws OBException;
	public void compile() throws OBException {
		//DDL statement won't compile, it will execute directly
	}
}
