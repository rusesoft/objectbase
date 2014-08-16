package org.objectbase.test;

import org.junit.Test;
import org.objectbase.parser.OBParser;

public class CreateProjectTest {
	
	@Test
	public void testCreateProject() {
		String command = "create project mca1.0 groupId = \"com.citi.oprisk\", artifactId=\"mca\", version=\"1.0.release\"";
		OBParser parser = new OBParser(command);
		try {
			parser.parseAndExecute();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
