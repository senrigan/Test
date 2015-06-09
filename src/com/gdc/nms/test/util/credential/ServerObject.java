package com.gdc.nms.test.util.credential;

import java.util.ArrayList;
import java.util.List;


//import com.thoughtworks.xstream.annotations.XStreamImplicit;
public class ServerObject {
	private String nameServer;
	private SSHCredential sshCredential;
	//@XStreamImplicit(itemFieldName="host")
	private List<String> jadeHosts;
	private MysqlCredential mysqlCredential;
	private boolean deleteAfterClose;
	private String glassFishPath;
	private String project;
	
	public boolean isDeleteAfterClose() {
		return deleteAfterClose;
	}

	public void setDeleteAfterClose(boolean deleteAfterClose) {
		this.deleteAfterClose = deleteAfterClose;
	}

	public String getGlassFishPath() {
		return glassFishPath;
	}

	public void setGlassFishPath(String glassFishPath) {
		this.glassFishPath = glassFishPath;
	}

	public String getNameServer() {
		return nameServer;
	}

	public void setNameServer(String nameServer) {
		this.nameServer = nameServer;
	}

	public List<String> getJadeHost(){
		if(jadeHosts == null){
			jadeHosts= new ArrayList<String>();
			jadeHosts.add(sshCredential.getHostname());
			return jadeHosts;
		}
		return jadeHosts;
	}

	public SSHCredential getSshCredential() {
		return sshCredential;
	}

	public void setSshCredential(SSHCredential sshCredential) {
		this.sshCredential = sshCredential;
	}

	public MysqlCredential getMysqlCredential() {
		return mysqlCredential;
	}

	public void setMysqlCredential(MysqlCredential mysqlCredential) {
		this.mysqlCredential = mysqlCredential;
	}

	public void setJadeHosts(List<String> jadeHosts) {
		this.jadeHosts = jadeHosts;
	}
	
	public void setProject(String xmlProject){
		this.project=xmlProject;
	}
	public String getProjects(){
		
		return project;
		
	}

	@Override
	public String toString() {
		return "Server [nameServer=" + nameServer + ", sshCredential="
				+ sshCredential + ", jadeHosts=" + jadeHosts
				+ ", mysqlCredential=" + mysqlCredential + "]";
	}
	
	
	
}
