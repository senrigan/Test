package com.gdc.nms.test.configuration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.validator.routines.InetAddressValidator;

import com.gdc.nms.test.configuration.CfgFile;
import com.gdc.nms.test.util.DeviceEspecific;

public class TestConfig {
	
	private CfgFile fileIni;
	private ArrayList<String> driverNames;
	private ArrayList<String> ipDevices;
	private ArrayList<DeviceEspecific> ipsInfo;
	
	
	
	public TestConfig(String testFileConfiguration) throws IOException{
		File file=new File(testFileConfiguration);
		fileIni=new CfgFile(file.getAbsolutePath());
	}
	
	
	public TestConfig(){
		
	}
	
	public void setTestFileConfiguration(String fileTestPath) throws IOException{
		fileIni=new CfgFile(fileTestPath);
	}
	
	
	
	public TestMode getTestMode(){
		TestMode testMode=TestMode.valueOf(getValue("configuration", "testMode"));
		return testMode;
		
		
	}
	
	public int getMaximumPerDriver(){
		return Integer.parseInt(getValue("configuration","maximum.per.driver" ));
	}
	/**
	 * 
	 * @return true if the number of drivers per file is !=-1 if the number is -1 return false
	 */
	public boolean existLimitPerDriver(){
		if(getMaximumPerDriver()==-1){
			return false;
		}
		return true;
	
	}
	
	public String getValue(String section,String key){
		return fileIni.getString(section, key);
	}
	/**
	 * 
	 * @return true if in the file configuration exist a name of driver this, method no check if is valid driver names
	 */
	public boolean existDriverNames(){
		driverNames=getDriverNames();
		if(driverNames==null ||driverNames.size()==0  ){
			return false;
		}
		return true;
	}
	public boolean canGenerateData(){
		String param=getValue("configuration","generate.data.device");
		param=param.replaceAll("\n","");
		if(param.equals("yes")){
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @return a arrayList of the names of drivers in the test File Configuration return null if not found valid names
	 */
	public ArrayList<String> getDriverNames(){
	
		String names=getValue("configuration","driver.to.test");
		System.out.println("names"+names);
		if(names!=null && names.length()>1){
			driverNames=new ArrayList<String>();
			String[] separateNames=names.split(",");
			for(String name:separateNames){
				name=name.replaceAll("\n", "");
				
				driverNames.add(name);
				
			}
			
		}
		return driverNames;
		
		
	}
	/**
	 * charge the ip Section in the file of configuration
	 */
	private void chargeIpDevice() {
		ipDevices=new ArrayList<String>();
		ipsInfo=new ArrayList<DeviceEspecific>();
		Iterator<String> itera=fileIni.getKeySection().iterator();
		InetAddressValidator validator=InetAddressValidator.getInstance();
		 
		while(itera.hasNext()){
			String ip=itera.next();
			System.out.println("ip"+ip);
			if(validator.isValid(ip)){
				DeviceEspecific ipInfo =new DeviceEspecific();
				ipInfo.setHostname(getIpHostName(ip));
				ipInfo.setIp(ip);
				ipInfo.setClassTest(getClassToTest(ip));
				ipsInfo.add(ipInfo);
				ipDevices.add(ip);
				
			}
		}
	}
	
	public boolean existIpDevice(){
		if(ipDevices!=null && ipDevices.size()>0){
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @return a list of ip section in the test configuration file return null if no found nothing valid ip section
	 */
	public ArrayList<String> getIpDevices(){
		chargeIpDevice();
		return this.ipDevices;
	}
	
	public ArrayList<DeviceEspecific>  getIpDataDevice(){
		chargeIpDevice();
		return ipsInfo;
	}
	public String getIpHostName(String ip){
		return getValue(ip,"hostname");
	}
	
	public String getClassToTest(String ip){
		return getValue(ip,"classToTest").replaceAll("\n","");
							
	}
	
	/**
	 * 
	 * this enum is because i running in java 6 and i canot use a switch with STring
	 *
	 */
	public enum TestMode{
		ALL,
		BOTH,
		ESPECIFIC
	}
	
	
	private boolean validTestMode(){
		try{
			TestMode mode=getTestMode();
			return true;
		}catch(IllegalArgumentException e){
			return false;
		}
		
		
	}
	
	
	public boolean isvalidFile(){
		if(validTestMode()){
			return true;
		}
		return false;
		
	}
	
	

}
