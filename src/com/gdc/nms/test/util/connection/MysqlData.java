package com.gdc.nms.test.util.connection;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import com.gdc.nms.common.DeviceUtil;
import com.gdc.nms.model.Device;
import com.gdc.nms.model.Project;
import com.gdc.nms.server.drivers.snmp.DriverManager;
import com.gdc.nms.tester.connection.MysqlCredential;
import com.gdc.nms.tester.connection.ServerObject;



public class MysqlData {
	
	private List<Device> devices;
	private List<Project> projects;
	
	private HashMap<String,ArrayList<Device>> foldersDevice ;
	private HashMap<String,String> oidSuport;
	private HashMap<String,String> ipDevices;
	private String ipsFileName="mainIp.txt";
	private final String projectsFileName="projects.txt";
	private static String folderParentTest="serverTest/";
	
	public MysqlData(){
		
	}
	
	
	public static String getFolderParentTest(){
		return folderParentTest;
	}
	
	
	public boolean isServerFilesDevicesCreated(ServerObject server){
		if(isServerDirectoryCreated(server)){
			System.out.println("si esta creado el server -*");
			if(serverDeviceFilesCount(server)>0){
				System.out.println("si ahy archivis -*");
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param server
	 * @param driverName the name of driver suport in the server
	 * @return ArraytList<Device> if the name and server is correct in otherwise return null
	 */
	public ArrayList<Device> getDevicesofDriver(ServerObject server,String driverName){
		String name=driverName.endsWith(".txt")?driverName:driverName.concat(".txt");
		if(getDeviceFileName(server).contains(name)){
			System.out.println("esta contenido");
			File driverFile=new File(getServerFile(server).getAbsolutePath()+"/"+"Devices"+"/"+name);
			ArrayList<Device> devices=DeviceXML.fromXML(driverFile.getAbsolutePath());
			return devices;
		}
		return null;
	}
	
	/**
	 * 
	 * @param server
	 * @return the number of files inside of server directory  0 if is empaty
	 */
	private int serverDeviceFilesCount(ServerObject server){
		File serverAddressFile=getServerFile(server);
		File[] deviceFile=serverAddressFile.listFiles();
		return deviceFile.length;
	}
	
	/**
	 * 
	 * @param server
	 * @return list of name device File if no have device file return null
	 */
	public List<String> getDeviceFileName(ServerObject server){
		if(isServerFilesDevicesCreated(server)){
			File serverAddressFile=new File(getServerFile(server).getAbsolutePath()+"/"+"Devices"+"/");
			File[] deviceFiles=serverAddressFile.listFiles();
			List<String> filesName=new ArrayList<String>() ;
			for(File file:deviceFiles){
				filesName.add(file.getName());
			}
			return filesName;
			
		}
		return null;
		
	}
	
	
	private File getServerFile(ServerObject server){
		File serverAddressFile =new File(folderParentTest+server.getSshCredential().getHostname());
		return serverAddressFile;
		
	}
	
	public boolean isServerDirectoryCreated(ServerObject server){
		File file=getServerFile(server);
		if(file.isDirectory()){
			return true;
		}
		return false;
	}
	
	public File[] getFilesDevice(ServerObject server){
		File file=new File(getServerFile(server).getAbsolutePath()+"/"+"Devices"+"/");
//		List<String> list=Arrays.asList(file.list());
		//list.remove(new File(this.getServerFile(server).getAbsolutePath()+"/"+ipsFileName));
		
		return file.listFiles();
		
	}
	public void createFilesAndFolders(ServerObject infoServ){
		System.out.println("server info"+infoServ);
		devices=getMysqlDataDevice(infoServ.getMysqlCredential());
		projects=getMysqlDataProject(infoServ.getMysqlCredential());
		URL url =DeviceUtil.class.getResource("device.properties");
		searchDeviceSuport(new File(url.getPath()));
		File serverFolder=getServerFile(infoServ);
		File parentServer=serverFolder.getParentFile();
		try {
			FileUtils.deleteDirectory(serverFolder);

			if(parentServer.exists()|| parentServer.mkdirs()){
				System.out.println("total de entradas"+foldersDevice);
				try {
					System.out.println("serverFolder"+serverFolder.getAbsolutePath());
					createDriverFile(serverFolder);
					createIPMainFile(serverFolder);
					createProjectFile(serverFolder);
					generateCountDevice(serverFolder);
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("error al crear los archiivos");
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
	}
	
	public HashMap<String,ArrayList<Device>> getFolderDevice(){
		return this.foldersDevice;
	}
	/**
	 * this metod create the files of drivers suport in the server
	 * @param directoryServer
	 * @throws IOException
	 */
	private void createDriverFile(File directoryServer) throws IOException{

			
		if(directoryServer.exists()||directoryServer.mkdirs()){
			String xmlDevice;
			for(Map.Entry<String,ArrayList<Device>> entry:foldersDevice.entrySet()){
				ArrayList<Device> devices=entry.getValue();
				xmlDevice="";
				Long time_init,time_end;
				time_init=System.currentTimeMillis();
				xmlDevice+=DeviceXML.toXML(devices);
				time_end=System.currentTimeMillis();
				System.out.println("the time que tard"+(time_end-time_init));
				File driverFile=new File(directoryServer.getAbsolutePath()+"/"+"Devices"+"/"+oidSuport.get(entry.getKey())+".txt");
				File parent=driverFile.getParentFile();
				if(!parent.exists()&& !parent.mkdirs()){
					throw new IllegalStateException("Couldn't create dir: " + parent);
				}
				FileUtils.writeStringToFile(driverFile, xmlDevice);
			}
		}else{
			throw new IOException();
		}
	}
	
	public boolean existProjectFile(ServerObject serv){
		File servDir=getServerFile(serv);
		File projectFile=new File(servDir.getAbsolutePath()+"/"+projectsFileName);
		if(projectFile.exists()){
			return true;
		}
		return false;
	}
	
	private void createProjectFile(File directoryServer) throws IOException{
		System.out.println("creando project file");
		if(directoryServer.exists()||directoryServer.mkdirs()){
			String xmlProject="";
			xmlProject+=DeviceXML.projectsToXML(projects);
			File driverFile=new File(directoryServer.getAbsoluteFile()+"/"+projectsFileName);
			FileUtils.writeStringToFile(driverFile, xmlProject);
		
		}else{
			throw new IOException();
		}
	}
	
	
	
	
	private void createIPMainFile(File directoryServer) throws IOException{
		if(directoryServer.exists()||directoryServer.mkdirs()){
			for(Map.Entry<String,String> entry:ipDevices.entrySet()){
				String sysOid=entry.getValue();
				
				String line=entry.getKey()+"|"+oidSuport.get(sysOid)+"\n";
				File driverFile=new File(directoryServer.getAbsolutePath()+"/"+ipsFileName);
				FileUtils.writeStringToFile(driverFile,line ,true);
			}
		}else{
			throw new IOException();
		}
	}
	
	

	
	public Device getIPDevice(String ip,ServerObject server) throws IOException{
		HashMap<String,String> ipContent=readIPMainFile(getServerFile(server));
		
		if(ipContent.containsKey(ip)){
			String driver=ipContent.get(ip);
			System.out.println("ip"+ip+"driver"+driver);
			ArrayList<Device> devices=getDevicesofDriver(server, driver);
			return searchListDevicebyIP(devices,ip);
		}else{
			return null;
		}
	}
	
	private Device searchListDevicebyIP(ArrayList<Device> devices,String ip){
		for(Device device:devices){
			if(device.getIp().equals(ip)){
				return device;
			}
		}
		return null;
	}
	
	private HashMap<String,String> readIPMainFile(File directoryServer) throws IOException{
		File ipsFile=new File(directoryServer.getAbsolutePath()+"/"+ipsFileName);
		List<String> lines=FileUtils.readLines(ipsFile);
		System.out.println("lines"+lines);
		HashMap<String,String> ipMap=new HashMap<String,String>();
		for(String line:lines){
			
			String ip=line.substring(0,line.indexOf("|"));
			System.out.println("ip"+ip);
			String driver=line.substring(line.indexOf("|")+1,line.length());
			System.out.println("driver"+driver);
			ipMap.put(ip, driver);
		}
		return ipMap;
		
	}
	
	
	private String PreviousSysOID(String sysOID){
		System.out.println(""+sysOID);
		sysOID=sysOID.substring(0,sysOID.lastIndexOf('.'));
		return sysOID;
	}
	private HashMap<String,ArrayList<Device>>  searchDeviceSuport(File source){
		oidSuport=getSysoOIDSuport(source);
		foldersDevice=new HashMap<String,ArrayList<Device>> ();
		ipDevices=new HashMap<String,String>();
		for(Device device:devices){
			String newOID=device.getSysObjectID();
			if(oidSuport.containsKey(newOID)){

				addToFolder(device.getSysObjectID(),device);
				ipDevices.put(device.getIp(),newOID);
			}else{
				
				while(device.getSysObjectID().length()>1 ){
					if((oidSuport.containsKey(newOID))){
						System.out.println("encontrado"+device.getSysObjectID()+" "+getDriverName(device));

						addToFolder(newOID,device);
						ipDevices.put(device.getIp(),newOID);
						break;
					}else if(newOID.contains(".")){
							newOID=PreviousSysOID(newOID);

						}else{
							System.out.println("no encontrado"+device.getSysObjectID()+" "+getDriverName(device));
							addToFolder("0",device);
							ipDevices.put(device.getIp(),"0");
							break;
						}
					}
					
					
				}

			}
		
		return foldersDevice;

	}
	
	private void  generateCountDevice(File directoryServer){
		HashMap<String,Integer> countDevices=new HashMap<String,Integer>();
		for(String driverName:foldersDevice.keySet()){
			ArrayList<Device> devices=foldersDevice.get(driverName);
			countDevices.put(oidSuport.get(driverName), devices.size());
		}
		createCountDeviceFile(countDevices,directoryServer);
		
	}
	
	private void createCountDeviceFile(HashMap<String ,Integer> countDevices,File directoryServer)
	{
		File driverFile=new File(directoryServer.getAbsolutePath()+"/"+"countDevices.txt");
		try {
			FileUtils.write(driverFile, "");
			for(String driverName:countDevices.keySet()){
				
				String line=driverName+"|"+countDevices.get(driverName)+"\n";
				try {
					FileUtils.writeStringToFile(driverFile,line ,true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
	private String getDriverName(Device device){
			
		return ""+DriverManager.getInstance().getDriver(device).getClass();
	}
	
	private void  addToFolder(String sysOIDFolder,Device device){
			ArrayList<Device>fileDevice=new ArrayList<Device>();
//			System.out.println("añadiendo al folder"+sysOIDFolder);
//			System.out.println("esta en hash"+foldersDevice.containsKey(sysOIDFolder));
			//System.out.println(""+sysOIDFolder+" "+com.gdc.nms.server.drivers.snmp.DriverManager.getInstance().getDriver(device).getClass());
			if(foldersDevice.containsKey(sysOIDFolder)){
//				System.out.println("contendido en folder"+sysOIDFolder);
				foldersDevice.get(sysOIDFolder).add(device);
			}else{
				fileDevice=new ArrayList<Device>();
				fileDevice.add(device);
				foldersDevice.put(sysOIDFolder,fileDevice);
			}
		
		
	}
	
	private List<Device> getMysqlDataDevice(MysqlCredential mysqlCredent){
		System.out.println("mysqlCRedent"+mysqlCredent);
		MySql mysqlBase=new MySql(mysqlCredent);
		try {
			mysqlBase.connect();
			return mysqlBase.getDevices();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	private List<Project> getMysqlDataProject(MysqlCredential mysqlCredent){
		MySql mysqlBase=new MySql(mysqlCredent);
		try {
			mysqlBase.connect();
			return mysqlBase.getProjects();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	public String getStringProjects(MysqlCredential mysql){
		MySql mysqlBase=new MySql(mysql);
		try {
			mysqlBase.connect();
			
			String xmlProject;
			
			xmlProject="";
			xmlProject+=DeviceXML.projectsToXML(mysqlBase.getProjects());
			return  xmlProject;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private HashMap<String,String> getSysoOIDSuport(File deviceFile){


		try {

			 LineIterator it = FileUtils.lineIterator(deviceFile, "UTF-8");
			 HashMap<String,String> hash=new HashMap<String,String>(); 
			 try {
				 String name,key;

				 Pattern p=Pattern.compile("[a-zA-Z]+");
				 while (it.hasNext()) {
			     String line = it.nextLine();
			     
			     if(line.startsWith("#")){
			    	 line=line.replace("#", " ");
			    	 line=line.replaceAll(" ","");
			    	 line=line.replaceAll("/"," ");
			    	 name=line;
			    	 line=it.nextLine();
			    	 key=line.substring(0, line.indexOf("=")-1);

			    	 Matcher m=p.matcher(key);
			    	 if(key.contains(".") || key.length()>0){

				    	 m=p.matcher(key);
			    
				    	 
				    	 if(!m.find()){

				    		 key.replaceAll(" ","");
				    		 //System.out.println("nombre"+name+"key"+key+"key size"+key.length());
					    	 hash.put(key, name);
					    	 
				    	 }
			    	 }
			    		
			    	 
			    	 
			     }
			   }

				 return hash;
			 } finally {
			   LineIterator.closeQuietly(it);
			   System.out.println("si acabo");
			  
			 }

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	

	
	

}
