package com.gdc.nms.test.util.connection;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;











import org.apache.commons.io.FileUtils;



import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.gdc.nms.tester.connection.SSHCredential;
import com.gdc.nms.tester.connection.ServerObject;











public class DataServer {
	private final String parentFolder="serverTest/";
	private final String supportedFileName="countDevices.txt";
	public DataServer(){
		
	}
	
	
	private XStream getXStreamConfig(){
		XStream xstream = new XStream(new DomDriver()); 
		xstream.alias("ServerItem", ServerObject.class);
		return xstream;
	}
	
	public String toXML(List<ServerObject> data){
		String xmlData="";
		XStream xstream=getXStreamConfig();
		xmlData=xstream.toXML(data);
		return xmlData;
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	public List<ServerObject> getServerData(File xmlFile){
		List<ServerObject> servers;
		XStream xstream=getXStreamConfig();
		System.out.println("xml sytream"+xstream.fromXML(xmlFile));
		servers=(List<ServerObject>) xstream.fromXML(xmlFile);
		return servers;
	}

	
	

	
	public String getDeviceSuport(ServerObject server) throws IOException{
		String filePath=parentFolder+server.getSshCredential().getHostname()+"/";
		File deviceSopported=new File(filePath+supportedFileName);
		String supportedInfo=FileUtils.readFileToString(deviceSopported);
		return supportedInfo;
		
		
	}
	
	public boolean isSuported(ServerObject server,String driverName){
		try {
			return getDeviceSuport(server).contains(driverName+"|");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return false;
		}
	}
	
	
	
	
	
	
	/**
	 * this method give the count of the device suport of the server 
	 * @param server
	 * @param driverName
	 * @return the num of device suport of driver if not exist return 0
	 * @throws IOException if error reading the file info countdevice.txt
	 */
	public int getNumDeviceSuport(ServerObject server,String driverName) throws IOException{
		
		String devicesCount=getDeviceSuport(server);
		String splitDevice[]=devicesCount.split("\n");
		System.out.println(Arrays.toString(splitDevice));
		for(int i=0;i<splitDevice.length;i++){
			if(splitDevice[i].contains(driverName+"|")){
//				System.out.println("lo contiene");
//				System.out.println(""+Arrays.toString(splitDevice[i].split("\\|")));
				String []auxSplit=splitDevice[i].split("\\|");
				
				return Integer.parseInt(auxSplit[1]);
			}
			
		}
		return 0;
	}
	
	
	
//	private ArrayList<Integer> generate
	
	
	public static void main(String[] args) {
		ServerObject server=new ServerObject();
		server.setSshCredential(new SSHCredential("192.168.207.5",null,null,0));
		DataServer data=new DataServer();
		try {
			data.getDeviceSuport(server);
			System.out.println(data.isSuported(server, "Cisco"));;
			System.out.println(data.getNumDeviceSuport(server, "RiverbedSteelhea"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
