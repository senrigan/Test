package com.gdc.nms.test.util.connection;

import java.util.List;
import java.io.File;

import org.apache.commons.io.FileUtils;

import com.gdc.nms.tester.connection.ServerObject;



public class ConectorServer {
	private List<ServerObject> servers;
	private String dataPath="src/ServersData.xml";
	
	public ConectorServer(){
		DataServer data=new DataServer();
		servers=data.getServerData(new File(dataPath));
		this.updateProjectsServers(servers);
	}
	
	public List<ServerObject> getServerList(){
		return this.servers;
	}

	
	public ServerObject getServerObject(String hostName){
		DataServer data=new DataServer();
		List<ServerObject> servers=data.getServerData(new File(dataPath));
		for(ServerObject serv:servers){
			System.out.println("server "+serv.getSshCredential().getHostname()+"igual "+hostName);
			if(serv.getSshCredential().getHostname().equals(hostName)||serv.getNameServer().equals(hostName)){
				return serv;
			}
		}
		return null;
	}
	
	
	public String getProjects(ServerObject server){
		
		try {
			File file=new File(MysqlData.getFolderParentTest()+
					server.getSshCredential().getHostname()+"/"+"projects.txt");
			String xmlProjects=FileUtils.readFileToString(file);
			return xmlProjects;
		} catch(Exception ex){
			return null;
		}
		
	}
	
	
	private List<ServerObject> updateProjectsServers(List<ServerObject>servers){
		for(ServerObject serv:servers){
			String projects=this.getProjects(serv);
			serv.setProjects(projects);
		}
		return servers;
	}
	
	

	
	
	
	
	

}
