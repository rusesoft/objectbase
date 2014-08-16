package org.objectbase;

import java.util.ArrayList;
import java.util.List;


public class OBBlock extends AbstractSyntax implements OBStatement{

	private List<OBStatement> statements = new ArrayList<OBStatement>();
	
	private boolean containsDDL;
	
	public OBBlock() {}

	public OBBlock(List<OBStatement> statements) {
		this.statements = statements;
	}
	
	public void addStatement(OBStatement statement) {
		statements.add(statement);
	}
	
	@Override
	public void execute() throws OBException {
		for (OBStatement statement : statements) {
			statement.execute();
		}
	}

	@Override
	public void compile() throws OBException {
		for (OBStatement statement : statements) {
			statement.compile();
		}
	}
	
	public boolean isContainsDDL() {
		return containsDDL;
	}

	public void setContainsDDL(boolean containsDDL) {
		this.containsDDL = containsDDL;
	}
}
