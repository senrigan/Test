package com.gdc.nms.test.runner;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.gdc.nms.test.util.SillyDevice;
import com.gdc.nms.tester.client.Client;
import com.gdc.nms.tester.common.exception.StartException;
import com.gdc.nms.tester.connection.ServerObject;

import jade.core.AID;

public class TestManager {
	private static TestManager instance;
	private static Client client;
	private static AID receiver ;
	private static ClientManager clientAdministrator;
	private static SillyDevice sillyDevice;
	private boolean started=false;
	private static Logger log;
    

	
	private TestManager(){
		
	}
	
	public static TestManager getIntance(){
		if(instance==null){
			instance=new TestManager();
			clientAdministrator=new ClientManager(client,receiver);
			log=Logger.getLogger(TestManager.class);
		}
		return instance;
	}
	
	public void setReciver(String host){
		System.out.println("se esta cambiando el nombre del reciver"+host);
		receiver=client.getAIDByHostname(host);
		System.out.println(""+receiver);
		clientAdministrator.setReciver(receiver);
		
	}
	
	public  AID getReciver(){
		return receiver;
	}
	
	public  Client getClient(){
		
		return client;
	}
	
	
	public SillyDevice getSillyDevice(){
		return TestManager.sillyDevice;
	}
	
	public void setDevice(SillyDevice device){
		TestManager.sillyDevice=device;
	}
	

	
	public void setClient(Client client){
		TestManager.client=client;
		clientAdministrator.setClient(client);
	}
	
	public void startClientConnection(){
		
		try {
			client.start();
			started=true;
		} catch (StartException e) {
			// TODO Auto-generated catch block
			System.out.println(""+e.getCause());
			log.error(""+e.getCause());
			log.error(""+Arrays.toString(e.getStackTrace()));
		}

	}
	
	public List<ServerObject> getServer(){
		return client.getStartedServers();
	}
	
	public boolean isStart(){
		return started;
	}
	
	public ClientManager getClientManager(){
		return TestManager.clientAdministrator;
	}
	
	public List<ServerObject> getServerReady(){
		return client.getStartedServers();
	}
	
	public void stopClientConection(){
		client.stop();
	}
	
	
	public static String tabulator(int numOfTabulation){
		String tabulation="";
		for(int i=0;i<numOfTabulation;i++){
			tabulation+="\t";
			
		}
		return tabulation;
	}
	
	
}
