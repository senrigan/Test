package com.gdc.nms.test.runner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.gdc.nms.model.Device;
import com.gdc.nms.model.DeviceResource;
import com.gdc.nms.server.ServerManager;
import com.gdc.nms.server.drivers.snmp.Driver;
import com.gdc.nms.server.drivers.snmp.DriverManager;
import com.gdc.nms.test.util.DeviceEspecific;
import com.gdc.nms.test.util.SillyDevice;
import com.gdc.nms.test.util.connection.ConectorServer;
import com.gdc.nms.test.util.connection.DataServer;
import com.gdc.nms.test.util.connection.MysqlData;
import com.gdc.nms.tester.connection.ServerObject;


/**
 * 
 * @author Gilberto Cordero Cervantes
 *
 */
public class LoaderDriver {
	
	private HashMap<String, ArrayList<SillyDevice>> devicesToTest;
	private List<ServerObject> servers;
	private DataServer serverInfo;
	
	public LoaderDriver(){
		serverInfo=new DataServer();
		chargerServerReadey();
	}

	/**
	 * this method load all devices of drivernames in the test configuration file with n number of devices per driver especefic in the 
	 * field of maximumPerDevice If this number is -1 try all numbers n that is in the database
	 * @throws IOException 
	 */
	public  HashMap<String , ArrayList<SillyDevice>> getDriversWithNames(List<String> driverNames,int maximumPerDriver) throws IOException{
		devicesToTest=new  HashMap<String , ArrayList<SillyDevice>>();
		for(String driver:driverNames){
			ArrayList<SillyDevice> devices;
			if(maximumPerDriver!=-1){
				devices=this.getDevicesRandomServer(driver, maximumPerDriver);
				devicesToTest.put(driver, devices);
			}else{
				devices=this.getAllDriverServer(driver);
				devicesToTest.put(driver, devices);
			}
		}
		return devicesToTest;
	}
	
	private ArrayList<SillyDevice>getDevicesRandomServer(String driverName,int maximumPerDriver) throws IOException{
		HashMap<ServerObject,Integer> serverAsigned=this.randomizerDevicePerServer(maximumPerDriver, servers.size());
		System.out.println("server asigned"+serverAsigned);
		ArrayList<SillyDevice> randomDevices=new ArrayList<SillyDevice>();
		Set<ServerObject> servers=serverAsigned.keySet();
		int remaningDevices=0;
		for(ServerObject server :servers){
			int numDeviceToAdd=serverAsigned.get(server)+remaningDevices;
			if(serverInfo.isSuported(server, driverName)){
				int devicesInServer=serverInfo.getNumDeviceSuport(server, driverName);
				ArrayList<SillyDevice> devices=new ArrayList<SillyDevice>();
				if(numDeviceToAdd<=devicesInServer){
					devices=this.getNDevices(numDeviceToAdd, driverName,server);
					remaningDevices=0;
				}else{
					devices=this.getNDevices(devicesInServer, driverName, server);
					remaningDevices-=devicesInServer;
				}
				randomDevices.addAll(devices);
				
			}else{
				remaningDevices+=numDeviceToAdd;
			}
		}
		return randomDevices;
		
	}
	
	private ArrayList<SillyDevice> getNDevices(int maximumPerDevice,String driverName,ServerObject server){
		MysqlData mysql=new MysqlData();
		ArrayList<Device> devices=mysql.getDevicesofDriver(server, driverName);
		ArrayList<SillyDevice> dev=new ArrayList<SillyDevice>();
		for(int i=0;i<maximumPerDevice;i++){
			SillyDevice silly=new SillyDevice();
			silly.setDevice(devices.get(i));
			silly.setDriverName(driverName);
			silly.setServerHost(server.getSshCredential().getHostname());
			dev.add(silly);
		}
		return dev;
	}
	
	
	private ArrayList<SillyDevice> getAllDriverServer(String driverName){
		MysqlData mysql=new MysqlData();
		ArrayList<SillyDevice> allDevices=new ArrayList<SillyDevice>();
		for(ServerObject server:servers){
			
			if(serverInfo.isSuported(server, driverName)){
				ArrayList<Device> devices=mysql.getDevicesofDriver(server, driverName);
				for(Device device: devices){
					SillyDevice silly=new SillyDevice();
					silly.setDevice(device);
					silly.setDriverName(driverName);
					silly.setServerHost(server.getSshCredential().getHostname());
					allDevices.add(silly);
				}
			}
			
		}
		
		return allDevices;
	}
	
	
	private ArrayList<SillyDevice> devicesToSillys(ArrayList<Device> devices,String driver,ServerObject server){
		ArrayList <SillyDevice> sillys=new ArrayList<SillyDevice>();
		for(Device device:devices){
			SillyDevice silly=new SillyDevice();
			silly.setDevice(device);
			silly.setDriverName(driver);
			silly.setServerHost(server.getSshCredential().getHostname());
			sillys.add(silly);
		}
		return sillys;
	}
	
	/**
	 * this method load all devices of drivernames in the test configuration file with n number of devices per driver especefic in the 
	 * field of maximumPerDevice If this number is -1 try all numbers n that is in the database
	 *
	 * @param maximumPerDevice
	 * @return
	 */
	
	public HashMap<String , ArrayList<SillyDevice>> getDriversWithoutNames(int maximumPerDevice){
		devicesToTest=new  HashMap<String , ArrayList<SillyDevice>>();
		MysqlData mysql=new MysqlData();
		for(ServerObject server:servers){
			File[] fileDevices=mysql.getFilesDevice(server);
			System.out.println("probando archivos"+Arrays.toString(fileDevices));
			
			for(File fileDevice:fileDevices){
				String driverName=fileDevice.getName().replace(".txt", "");
				if(serverInfo.isSuported(server, driverName)){
					System.out.println("nombre del archivo"+driverName);
					ArrayList<SillyDevice> sillys=new ArrayList<SillyDevice>();
					ArrayList<Device> devices=mysql.getDevicesofDriver(server, fileDevice.getName());
					System.out.println("devices num per driver"+devices.size());
				
					if(maximumPerDevice==-1){
						this.getAllDriverServer(driverName);
						if(devicesToTest.containsKey(fileDevice.getName())){
							sillys=devicesToSillys(devices,fileDevice.getName(),server);
							devicesToTest.get(driverName).addAll(sillys);
						}else{
							sillys=devicesToSillys(devices,fileDevice.getName(),server);

							devicesToTest.put(driverName, sillys);
						}
						
					}else{
						ArrayList<Device> deviceMaximum;
						if(devices.size()>=maximumPerDevice){
//								deviceMaximum=(ArrayList<Device>) devices.subList(0,maximumPerDevice.intValue());
							deviceMaximum=getNDevices(maximumPerDevice,devices);
							
						}else{
							deviceMaximum=getNDevices(devices.size(),devices);

						}
						if(devicesToTest.containsKey(driverName)){
							sillys=devicesToSillys(deviceMaximum,fileDevice.getName(),server);
							devicesToTest.get(driverName).addAll(sillys);
						}else{
							sillys=devicesToSillys(deviceMaximum,driverName,server);
							devicesToTest.put(driverName, sillys);
						}
						
						
					}
					
				}else{
					System.out.println("no se pudo cargar el driver"+driverName);
				}
				
				
			
			}
		}
		return devicesToTest;
	}
	
	private ArrayList<Device> getNDevices(int maximumPerDevices,ArrayList<Device> devices){
		ArrayList<Device> nDevices=new ArrayList<Device>();
		for(int i=0;i<maximumPerDevices;i++){
			nDevices.add(devices.get(i));
		}
		return nDevices;
		
	}
	public HashMap<String , ArrayList<SillyDevice>> getEspecifcAndGeneralDriver(List<String> driverNames,int maximumPerDevice,ArrayList<DeviceEspecific> ips) throws IOException{
		 
		 devicesToTest=getDriversWithNames(driverNames, maximumPerDevice);
		 devicesToTest.putAll(getDeviceByIp(ips));
		 return devicesToTest;
	}
	
	public static void print(Object message) {
		
	}
	
	public void name() {
		ServerManager i = ServerManager.getInstance();
		Device device = i.getDeviceCache().byIp("192.168.204.200");
		
		Driver driver = DriverManager.getInstance().getDriver(device);
		try{
			List resources = driver.getDeviceResources();
			if(resources == null){
				print("No mames");
			} else {
				print(resources.size());
			}
		} catch(Exception e){
			print(e.getMessage());
		} finally {
			if(driver != null){
				driver.unbind();
			}
		}
		
		
		
	}
	

	public HashMap<String , ArrayList<SillyDevice>> getEspecifcAndGeneralDriver(int maximumPerDevice,ArrayList<DeviceEspecific> ips){
		//TODO implementar el metodo para ller los dispositovos ip
		 
		 devicesToTest=getDriversWithoutNames(maximumPerDevice);
		 devicesToTest.putAll(getDeviceByIp(ips));
		 return devicesToTest;
	}
	
	/**
	 * consulta los servidores segun la ip dadas y el servidor por el cual se conectan arroja erro si no es enconytrada la ip
	 * @param ipsInfo
	 * @return
	 */
	public HashMap<String , ArrayList<SillyDevice>> getDeviceByIp(ArrayList<DeviceEspecific> ipsInfo){
		MysqlData mysql=new MysqlData();
		HashMap<String,ArrayList<SillyDevice>> dataIp=new HashMap<String,ArrayList<SillyDevice>>();
		for(DeviceEspecific ipInfo:ipsInfo){
			ConectorServer conector=new ConectorServer();
			ServerObject serv=conector.getServerObject(ipInfo.getHostname());
			System.out.println("server"+serv);
			if(serv!=null){
				
				try {
					ArrayList<Device> devicesIp=new ArrayList<Device>();

					Device device=mysql.getIPDevice(ipInfo.getIp(),serv);
					System.out.println("devices "+device.getSysObjectID());
					devicesIp.add(device);
					
					dataIp.put(ipInfo.getIp(),devicesToSillys(devicesIp,ipInfo.getClassTest(),serv));
					System.out.println("data"+dataIp);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				throw new IllegalArgumentException("el nombre del host esta mal");
			}
			
			

		}
		return dataIp;
	}
	
	private void  chargerServerReadey(){
		this.servers=TestManager.getIntance().getServerReady();
	}
	
	/**
	 * 
	 * @return
	 */
	private HashMap<ServerObject,Integer> randomizerDevicePerServer(int devicesMaxPerDriver,int numServers){
		ArrayList<Integer> indexServer=Randomizer.rangeWithoutRepeat(0, numServers);
		ArrayList<Integer> indexDevice=splitDevicePerServer(devicesMaxPerDriver);
	
		HashMap<ServerObject,Integer> serverWithDevice=new HashMap<ServerObject,Integer>();	
		for(int i=0,k=0;i<indexServer.size() && k<indexDevice.size();){
			Integer serverNum=indexServer.get(i);
			for(;k<indexDevice.size();){
				ServerObject server=this.servers.get(serverNum);
				if(serverWithDevice.containsKey(server)){
					Integer deviceTotal=serverWithDevice.get(server)+indexDevice.get(k);
					serverWithDevice.put(server, deviceTotal);
				}else{
					serverWithDevice.put(server,indexDevice.get(k));

				}
				break;
			}
			k++;
			i++;
			if(i==indexServer.size() && k<indexDevice.size()){
				i=0;
			}
			
		}

		return serverWithDevice;
	}
	
	public static void main(String[] args) {
		LoaderDriver load=new LoaderDriver();
		ConectorServer conect=new ConectorServer();
		load.servers=conect.getServerList();

		MysqlData mysql=new MysqlData();
		List<String> deviceNames=mysql.getDeviceFileName(load.servers.get(0));
		System.out.println("device Name"+deviceNames);
		
		for(String driverName:deviceNames){
			System.out.println("driverName"+driverName);
			System.out.println(""+load.getAllDriverServer(driverName));
		}
		
	}
	
	private ArrayList<Integer> splitDevicePerServer(int devicesMaxPerDriver){
		ArrayList<Integer> randomList=new ArrayList<Integer>();
 		while(devicesMaxPerDriver>0){
 			Integer randomNumber=Randomizer.range(1, devicesMaxPerDriver);
			if(randomNumber<=devicesMaxPerDriver){
				randomList.add(randomNumber);
				devicesMaxPerDriver-=randomNumber;
			}
		}
		return randomList;
	}
	
	


}
