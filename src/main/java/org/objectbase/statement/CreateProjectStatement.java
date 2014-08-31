package org.objectbase.statement;

import java.util.List;

import org.objectbase.OBException;
import org.objectbase.OBPair;
import org.springframework.roo.model.JavaPackage;
import org.springframework.roo.project.MavenCommands;
import org.springframework.roo.project.packaging.JarPackaging;

public class CreateProjectStatement extends DDLStatement{

	private String projectId;
	private String groupId;
	private String artifactId;
	private String version;
	
	public CreateProjectStatement(String projectId, List<OBPair> pairs) {
		this.projectId = projectId;
		this.artifactId = projectId;
		this.version = "1.0";
		
		for (OBPair pair : pairs) {
			if(pair.getKey().equals("groupId")) {
				this.groupId = pair.getValue().asString();
			}
			else if(pair.getKey().equals("artifactId")) {
				this.artifactId = pair.getValue().asString();
			}
			else if(pair.getKey().equals("version")) {
				this.version = pair.getValue().asString();
			}
		}
	}
	
	public CreateProjectStatement(String projectId, String groupId) {
		this.projectId = projectId;
		this.groupId = groupId;
		this.artifactId = projectId;
		this.version = "1.0";
	}
	
	public CreateProjectStatement(String projectId, String groupId,
			String artifactId, String version) {
		this.projectId = projectId;
		this.groupId = groupId;
		this.artifactId = artifactId;
		this.version = version;
	}


	@Override
	public void execute() throws OBException {
		System.out.println("creating maven project " + "projectId: " + projectId +
				", groupId: "+groupId + ", artifactId: " + artifactId + ", version: "+version);
		
		MavenCommands mavenCmd = new MavenCommands();
		JavaPackage topLevelPackage  = new JavaPackage(groupId);
		
		mavenCmd.createProject(topLevelPackage, projectId, null, null, new JarPackaging());
	}

}
