package com.gdc.nms.test;



import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.validator.routines.InetAddressValidator;
import org.apache.log4j.Logger;
import org.junit.Test;

import com.gdc.nms.test.configuration.TestConfig;
import com.gdc.nms.test.configuration.TestConfig.TestMode;
import com.gdc.nms.test.especific.BlueCoatDriverTest;
import com.gdc.nms.test.especific.Cisco619Test;
import com.gdc.nms.test.especific.CiscoAceTest;
import com.gdc.nms.test.especific.CiscoDriverTest;
import com.gdc.nms.test.especific.CiscoIPSTest;
import com.gdc.nms.test.especific.CiscoNXTest;
import com.gdc.nms.test.especific.CiscoNexus7000SeriesTest;
import com.gdc.nms.test.especific.EmersonUPSUnitronicsTest;
import com.gdc.nms.test.especific.FortinetTest;
import com.gdc.nms.test.especific.FoundryNetworksTest;
import com.gdc.nms.test.especific.GenericDriverTest;
import com.gdc.nms.test.especific.H3CTest;
import com.gdc.nms.test.especific.Huawei1220_2220_2240Test;
import com.gdc.nms.test.especific.HuaweiNE40Test;
import com.gdc.nms.test.especific.HuaweiS5300Test;
import com.gdc.nms.test.especific.HuaweiS5700_10P_PWR_LI_AC_Test;
import com.gdc.nms.test.especific.HuaweiS7700Test;
import com.gdc.nms.test.especific.HuaweiTest;
import com.gdc.nms.test.especific.Huaweis5700Test;
import com.gdc.nms.test.especific.Huaweis5710_52C_Pwr_ElTest;
import com.gdc.nms.test.especific.JuniperTest;
import com.gdc.nms.test.especific.NetScreenTest;
import com.gdc.nms.test.especific.PipeLineTest;
import com.gdc.nms.test.especific.RiverBedSteelHeadTest;
import com.gdc.nms.test.especific.TelDatTest;
import com.gdc.nms.test.especific.TrippLiteUPSTest;
import com.gdc.nms.test.especific.UpsSoniCupsTest;
import com.gdc.nms.test.especific._3ComTest;
import com.gdc.nms.test.especific.process.GdcTelecontrollerTest;
import com.gdc.nms.test.especific.process.ImageStreamTelecoControllerTest;
import com.gdc.nms.test.especific.process.ImageStreamTest;
import com.gdc.nms.test.especific.process.ImageStream_Net_Snm_LinuxTest;
import com.gdc.nms.test.especific.process.Nti_Net_Snmp_LinuxTest;
import com.gdc.nms.test.especific.process.ProximTest;
import com.gdc.nms.test.especific.process.VYattaTest;
import com.gdc.nms.test.generic.TestUPSDevice;
import com.gdc.nms.test.runner.LoaderDriver;
import com.gdc.nms.test.runner.TestInformed;
import com.gdc.nms.test.runner.TestManager;
import com.gdc.nms.test.util.SillyDevice;
import com.gdc.nms.test.util.connection.ConectorServer;
import com.gdc.nms.test.util.connection.MysqlData;
import com.gdc.nms.tester.client.Client;
import com.gdc.nms.tester.connection.ServerObject;






/**
 * 
 * @author Gilberto Cordero
 *
 */
public class RunTest {
	private String testConfigPath="./src./TestProperties.ini";
	private static String serverJarPath="release/release/server/tester-server.jar";
	public static SillyDevice device;
	public static String serverInUse;
	private boolean ipExist=false;
	public static  TestManager  managerDriverTest;
	private static Logger log;
	private TestConfig configTest;

	
	
	public  void setUp(){
		managerDriverTest=TestManager.getIntance();
		ConectorServer conector=new ConectorServer();
		ArrayList <ServerObject> servers2=(ArrayList<ServerObject>) conector.getServerList();
		System.out.println("servers2"+servers2);
		Client client=new Client(servers2);
		client.setServerTesterFile(new File(serverJarPath));
		managerDriverTest.setClient(client);
		generateDataServer();
		
		
	}
	
	

	
	
	private void generateDataServer(){
		try {
			configTest = new TestConfig(testConfigPath);
			if(configTest.canGenerateData()){
				System.out.println("generando data");
				generateDataPerRun();
			}else{
				System.out.println("usando data");
				useDataPerRun();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("error leyendo el archivo de configuracion");
			e.printStackTrace();
		}
	}
	
	
	

	/**
	 * 
	 * this enum is because i running in java 6 and i canot use a switch with String
	 * the method run by the test mode in the testconfiguration file
	 * @throws Exception 
	 * 
	 */
	
	public HashMap<String,ArrayList<SillyDevice>>  runTestMode() throws Exception {
		TestMode mode=configTest.getTestMode();
		if(configTest.isvalidFile()){
			return getDriversByTestMode(mode);
		}else{
			throw new Exception("the Test file configuration is no correct");
		}

	}
	
	
	private HashMap<String,ArrayList<SillyDevice>> getDriversByTestMode(TestMode mode) throws IOException{
		LoaderDriver driverLoader=new LoaderDriver();
		HashMap<String,ArrayList<SillyDevice>> driverToTest=new HashMap<String,ArrayList<SillyDevice>>();
		switch(mode){
		case ALL:
			if(configTest.existDriverNames()){
				driverToTest=driverLoader.getDriversWithNames(configTest.getDriverNames(),configTest.getMaximumPerDriver());
			}else{
				driverToTest=driverLoader.getDriversWithoutNames(configTest.getMaximumPerDriver());
			}
			
			break;
		case BOTH:
			if(configTest.existDriverNames()){
				driverToTest=driverLoader.getEspecifcAndGeneralDriver(configTest.getDriverNames(),configTest.getMaximumPerDriver(),configTest.getIpDataDevice());
				ipExist=true;
			}else{
				driverToTest=driverLoader.getEspecifcAndGeneralDriver(configTest.getMaximumPerDriver(),configTest.getIpDataDevice());
				driverLoader.getDeviceByIp(configTest.getIpDataDevice());
			}
			
			break;
		case ESPECIFIC:
			
			driverToTest=driverLoader.getDeviceByIp(configTest.getIpDataDevice());
			ipExist=true;
			System.out.println("driver to tes test mode"+driverToTest);
			break;
	
			
		}
		return driverToTest;
	}
	
	
	
	public boolean existIpDevice(){
		return ipExist;
	}
	
	public void generateDataPerRun(){
	
			ConectorServer conector=new ConectorServer();
			
			List<ServerObject> servers=conector.getServerList();
			MysqlData mysql=new MysqlData();
			for(ServerObject serv:servers){
				mysql.createFilesAndFolders(serv);
				

			}
	
	}
	
	public void useDataPerRun(){
		ConectorServer conector=new ConectorServer();
		List<ServerObject> servers=conector.getServerList();
		MysqlData mysql=new MysqlData();
		System.out.println("servers"+servers);
		for(ServerObject serv:servers){
			if(mysql.isServerFilesDevicesCreated(serv)){
				if(mysql.existProjectFile(serv)){
					System.out.println("existe el rpoeject file");
				}else{
					mysql.createFilesAndFolders(serv);
				}
			}else{
				System.out.println("no existe el project file");
				mysql.createFilesAndFolders(serv);

			}
			

		}
	}
	
	
	
	
	
	@Test
	public void runAllTest(){
		setUp();
		
		//managerDriverTest=TestManager.getIntance();
		log=Logger.getLogger(RunTest.class);
		managerDriverTest.startClientConnection();
		System.out.println("server Readye"+TestManager.getIntance().getServerReady());
		System.out.println("numero de servidores listos"+TestManager.getIntance().getServerReady().size());
		try {
			HashMap<String,ArrayList<SillyDevice>> driverToTest=runTestMode();
			System.out.println("in the tes**"+driverToTest);
			
			
			if(managerDriverTest.isStart()){
				StringBuffer logInfoAppender=new StringBuffer("Iniciando Test");
				logInfoAppender.append(String.format("%35.30s",Calendar.getInstance().getTime() ));
				log.info(logInfoAppender);
				System.out.println("key set driver"+driverToTest.keySet());
				Set<String> driverNames=driverToTest.keySet();
				TestInformed tester=new TestInformed();
				for(String driverName:driverNames){
					System.out.println("DRIVER nAME"+driverName);
					if(driverName.contains(".txt")){
						driverName=driverName.substring(0,driverName.indexOf(".txt"));
					}
					logInfoAppender=TestInformed.getTabulatorEspace(1);
					logInfoAppender.append("Probando el driver: ");
					logInfoAppender.append(driverName);
					log.info(logInfoAppender);
					System.out.println("\n\n"+"probando dispositivos del driver"+driverName);
					ArrayList<SillyDevice> devices=driverToTest.get(driverName);
					logInfoAppender=TestInformed.getTabulatorEspace(2);
					logInfoAppender.append("numero de device a probar: ");
					logInfoAppender.append(devices.size());
					log.info(logInfoAppender);
					for(SillyDevice deviceToTest:devices){
						managerDriverTest.setDevice(deviceToTest);
						System.out.println("drive a rpobar "+driverName);
						logInfoAppender=TestInformed.getTabulatorEspace(3);
						logInfoAppender.append(" ip "+deviceToTest.getDevice().getIp());
						logInfoAppender.append(" SysoOID"+deviceToTest.getDevice().getSysObjectID());
						logInfoAppender.append(" servidor: "+deviceToTest.getServerHost());
						log.info(logInfoAppender);
						changeServerInUse(deviceToTest.getServerHost());
						if(isUPSDevice(driverName)){
							tester.runTestClass(TestUPSDevice.class);
							System.out.println(""+tester);
						}else{
							tester.runTestClass(TestDevice.class);
							System.out.println(""+tester);
						}
						
						if(tester.testIsSucessful()){
							InetAddressValidator validator=InetAddressValidator.getInstance();

							if(!validator.isValid(driverName)){
								driverName=driverStringToEnum(driverName);
								System.out.println("driver Name cambiado"+driverName);
								typeTest testerDriver=typeTest.valueOf(driverName);
								System.out.println("tester driver"+testerDriver);
								runTestByEspecific(testerDriver,tester);
							
							}else{
								runTestByIP(tester,deviceToTest);
								
							}
						}	
					}
				}
				finishTest(tester);
				
			}else{
				log.error("no se pudo iniciar el server");
				System.out.println("no se pudo inicar el server");
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("no se pudieron cargar los Devices");
			System.out.println("hubo un error al leer el archivo");
			e.printStackTrace();
			
		}catch(Exception ex){
			ex.printStackTrace();
		
		System.out.println("error"+Arrays.toString(ex.getStackTrace()));
			System.out.println("hubo un error muy grave");
		
		}
		
		
	}
	
	
	private void finishTest(TestInformed tester){
		System.out.println("se esta deteniendo el cliente");
		managerDriverTest.stopClientConection();
		createLogFinalCount(tester);
	}
	
	
	private void createLogFinalCount(TestInformed tester){
		StringBuffer logInfo; 
		logInfo=TestInformed.getTabulatorEspace(5);
		logInfo.append("Numero de Test: "+tester.getNumofTestClass());
		logInfo.append(" Test passed: "+tester.getNumofSuccedClass());
		logInfo.append(" Test Failder: "+tester.getNumofFailedClass());
		log.info(logInfo);
	}
	
	
	private boolean isUPSDevice(String className){
		className=className.toLowerCase();
		if(className.contains("ups")){
			return true;
		}
		return false;
	}
	
	
	private void runTestByIP(TestInformed tester,SillyDevice device){
		Class<?> unknow;
		try {
			unknow = testClassByIp(device.getDriverName());
			changeServerInUse(device.getServerHost());
			tester.runTestClass(unknow);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("no se encontro la clase");
			e.printStackTrace();
		}
		
	}
	
	private void  runTestByEspecific(typeTest typeDriver,TestInformed tester){
		switch(typeDriver){
		
		case BLUECOATPACKETSHAPER9_2_7:
			System.out.println("probando el bliecoat");
			tester.runTestClass(BlueCoatDriverTest.class);
			System.out.println(""+tester);
			
			break;
		case CISCO:
			System.out.println("probando el Cisco");

			tester.runTestClass(CiscoDriverTest.class);
			System.out.println(""+tester);
		
			break;
		case CISCO619:
			System.out.println("probando el Cisco 619");
			tester.runTestClass(Cisco619Test.class);
			System.out.println(""+tester);
			break;
		case CISCOACE:
			System.out.println("probando el "+typeDriver);
			System.out.println(""+tester+"---");
			tester.runTestClass(CiscoAceTest.class);
			break;
		case CISCOIPS:
			System.out.println("probando el "+typeDriver);
			System.out.println(""+tester+"---");
			tester.runTestClass(CiscoIPSTest.class);
			break;
		case CISCONEXUS7000SERIES:
			System.out.println("probando el "+typeDriver);
			System.out.println(""+tester+"---");
			tester.runTestClass(CiscoNexus7000SeriesTest.class);
			break;
		case CISCONX:
			System.out.println("probando el "+typeDriver);
			System.out.println(""+tester+"---");
			tester.runTestClass(CiscoNXTest.class);
			break;
		case EMERSONUPS_UNITRONICS:
			System.out.println("probando el "+typeDriver);
			System.out.println(""+tester+"---");
			tester.runTestClass(EmersonUPSUnitronicsTest.class);
			break;
		case FORTINET:
			System.out.println("probando el "+typeDriver);
			System.out.println(""+tester+"---");
			tester.runTestClass(FortinetTest.class);
			break;
		case FOUNDRYNETWORKS:
			System.out.println("probando el "+typeDriver);
			System.out.println(""+tester+"---");
			tester.runTestClass(FoundryNetworksTest.class);
			break;
		case GDCTELECONTROLLER:
			System.out.println("probando el "+typeDriver);
			System.out.println(""+tester+"---");
			tester.runTestClass(GdcTelecontrollerTest.class);
			break;
		case GENERICANDPINGONLYSTATS:
			System.out.println("probando el "+typeDriver);
			System.out.println(""+tester+"---");
			tester.runTestClass(GenericDriverTest.class);
			break;
		case H3C:
			System.out.println("probando el "+typeDriver);
			System.out.println(""+tester+"---");
			tester.runTestClass(H3CTest.class);
			break;
		case HUAWEI:
			System.out.println("probando el "+typeDriver);
			System.out.println(""+tester+"---");
			tester.runTestClass(HuaweiTest.class);
			break;
		case HUAWEIAR1220_2220_2240:
			System.out.println("probando el "+typeDriver);
			System.out.println(""+tester+"---");
			tester.runTestClass(Huawei1220_2220_2240Test.class);
			break;
		case HUAWEINE40:
			System.out.println("probando el "+typeDriver);
			System.out.println(""+tester+"---");
			tester.runTestClass(HuaweiNE40Test.class);
			break;
		case HUAWEIS5300:
			System.out.println("probando el "+typeDriver);
			System.out.println(""+tester+"---");
			tester.runTestClass(HuaweiS5300Test.class);
			break;
		case HUAWEIS5700:
			System.out.println("probando el "+typeDriver);
			System.out.println(""+tester+"---");
			tester.runTestClass(Huaweis5700Test.class);
			break;
		case HUAWEIS5700_10P_PWR_LI_AC:
			System.out.println("probando el "+typeDriver);
			System.out.println(""+tester+"---");
			tester.runTestClass(HuaweiS5700_10P_PWR_LI_AC_Test.class);
			break;
		case HUAWEIS5710_52C_PWR_EL:
			System.out.println("probando el "+typeDriver);
			System.out.println(""+tester+"---");
			tester.runTestClass(Huaweis5710_52C_Pwr_ElTest.class);
			break;
		case HUAWEIS7700:
			System.out.println("probando el "+typeDriver);
			System.out.println(""+tester+"---");
			tester.runTestClass(HuaweiS7700Test.class);
			break;
		case IMAGESTREAM:
			System.out.println("probando el "+typeDriver);
			System.out.println(""+tester+"---");
			tester.runTestClass(ImageStreamTest.class);
			break;
		case IMAGESTREAMTELECONTROLLER:
			System.out.println("probando el "+typeDriver);
			System.out.println(""+tester+"---");
			tester.runTestClass(ImageStreamTelecoControllerTest.class);

			break;
		case IMAGESTREAM_NET_SNMP_LINUX:
			System.out.println("probando el "+typeDriver);
			System.out.println(""+tester+"---");
			tester.runTestClass(ImageStream_Net_Snm_LinuxTest.class);

			break;
		case JUNIPER:
			System.out.println("probando el "+typeDriver);
			System.out.println(""+tester+"---");
			tester.runTestClass(JuniperTest.class);

			break;
		case NETSCREEN:
			System.out.println("probando el "+typeDriver);
			System.out.println(""+tester+"---");
			tester.runTestClass(NetScreenTest.class);

			break;
		case NTI_NET_SNMP_LINUX:
			System.out.println("probando el "+typeDriver);
			System.out.println(""+tester+"---");
			tester.runTestClass(Nti_Net_Snmp_LinuxTest.class);

			break;
		case PIPELINE:
			System.out.println("probando el "+typeDriver);
			System.out.println(""+tester+"---");
			tester.runTestClass(PipeLineTest.class);

			break;
		case PROXIM:
			System.out.println("probando el "+typeDriver);
			System.out.println(""+tester+"---");
			tester.runTestClass(ProximTest.class);

			break;
		case RIVERBEDSTEELHEAD:
			System.out.println("probando el "+typeDriver);
			System.out.println(""+tester+"---");
			tester.runTestClass(RiverBedSteelHeadTest.class);

			break;
		case TELDAT:
			System.out.println("probando el "+typeDriver);
			System.out.println(""+tester+"---");
			tester.runTestClass(TelDatTest.class);

			break;
		case TRIPPLITEUPS:
			System.out.println("probando el "+typeDriver);
			System.out.println(""+tester+"---");
			tester.runTestClass(TrippLiteUPSTest.class);

			break;
		case UPSONICUPS:
			System.out.println("probando el "+typeDriver);
			System.out.println(""+tester+"---");
			tester.runTestClass(UpsSoniCupsTest.class);

			break;
		case VYATTA:
			System.out.println("probando el "+typeDriver);
			System.out.println(""+tester+"---");
			tester.runTestClass(VYattaTest.class);

			break;
		case _3COM:
			System.out.println("probando el "+typeDriver);
			System.out.println(""+tester+"---");
			tester.runTestClass(_3ComTest.class);

			break;
		default:
				System.out.println("llego algo inesperado");
			break;
	
		
	}
	}
	
	private void changeServerInUse(String newServer){
		if(!(RunTest.serverInUse==null)){
			if(RunTest.serverInUse.equals(newServer)){
				return;
			}else{
				RunTest.serverInUse=newServer;
				managerDriverTest.setReciver(newServer);
			}
		}else{
			RunTest.serverInUse=newServer;
			managerDriverTest.setReciver(newServer);
		}
		
	}
	private Class<?> testClassByIp(String className) throws ClassNotFoundException{
		String packageName="com.gdc.nms.test.custom.";
		System.out.println("class full name ip:"+packageName+className);
		Class<?> classUnknow =this.getClass().getClassLoader().loadClass(packageName+className);
		return classUnknow;
	}
	private String driverStringToEnum(String driverName){
		if(startWithNumber(driverName)){
			System.out.println("el driver tiene numero"+driverName);
		driverName="_"+driverName.substring(0,driverName.length());
		System.out.println("driver sin numero"+driverName);
		}
		driverName=driverName.replaceAll(".txt", "");
		driverName=driverName.replaceAll("\\(", "_");
		driverName=driverName.replaceAll("\\)","_");
		driverName=driverName.replaceAll("-","_");
		
		driverName=driverName.replaceAll(" ","_");	
		if(driverName.endsWith("_")){
			driverName=driverName.substring(0,driverName.length()-1);
		}

		return driverName.toUpperCase();
	}
	
	private boolean startWithNumber(String driverName){
	        return Character.isDigit(driverName.charAt(0));
	        
	    
	}
	

	
	enum typeTest{
		TRIPPLITEUPS,
		IMAGESTREAM_NET_SNMP_LINUX,
		NTI_NET_SNMP_LINUX,
		HUAWEIS5710_52C_PWR_EL,
		CISCO619,
		NETSCREEN,
		RIVERBEDSTEELHEAD,
		HUAWEI,
		IMAGESTREAMTELECONTROLLER,
		JUNIPER,
		UPSONICUPS,
		H3C,
		IMAGESTREAM,
		GDCTELECONTROLLER,
		HUAWEIS5300,
		FORTINET,
		HUAWEINE40,
		BLUECOATPACKETSHAPER9_2_7,
		EMERSONUPS_UNITRONICS,
		PIPELINE,
		GENERICANDPINGONLYSTATS,
		CISCONX,
		HUAWEIS5700,
		HUAWEIS5700_10P_PWR_LI_AC,
		FOUNDRYNETWORKS,
		CISCONEXUS7000SERIES,
		VYATTA,
		HUAWEIS7700,
		PROXIM,
		_3COM,
		CISCOIPS,
		CISCOACE,
		TELDAT,
		HUAWEIAR1220_2220_2240,
		CISCO,
		
		
	}
	
	public static void main(String[] args) {
		RunTest run=new RunTest();
			run.setUp();
			run.runAllTest();
//		File file=new File(RunTest.serverJarPath);
//		URL url =RunTest.class.getResource("tester-client");
//		System.out.println("url"+url);
//		System.out.println(file.getAbsolutePath()+file.exists());
		
	}

}
