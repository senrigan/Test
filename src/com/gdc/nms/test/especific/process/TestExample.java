package com.gdc.nms.test.especific.process;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.gdc.nms.model.DeviceResource;
import com.gdc.nms.model.Interface;
import com.gdc.nms.model.IpSla;
import com.gdc.nms.model.DeviceResource.Type;
import com.gdc.nms.model.QosClass;
import com.gdc.nms.test.configuration.TestRule;
import com.gdc.nms.test.runner.ClientManager;
import com.gdc.nms.test.runner.TestManager;


public class TestExample {
	private static ClientManager clientAdmin;
	@Rule
	public TestRule ruleInform=new TestRule();

	@BeforeClass
	public static void SetUP(){
		TestManager manager=TestManager.getIntance();
		clientAdmin=manager.getClientManager();
		clientAdmin.setDevice(manager.getSillyDevice().getDevice());
	}
	
	
	@Test
	public void ResourceList() throws Exception{
		List<DeviceResource> resources=clientAdmin.getResources();
		for(DeviceResource reso:resources){
			System.out.println("tipo de recurso"+reso.getResourceType()+"\n key: "+clientAdmin.getStatResource(reso).getDescriptor().getProperties().keySet());
		}
	
	}
	
	@Test
	public void InterfaceList() throws Exception{
		Set<Interface> interfaces=clientAdmin.getIntefaces();
		for(Interface inter:interfaces){
			System.out.println("tipo de interfaz"+inter.getAlias()+" "+inter.getAliasOrName()+" "+inter.getDescription());
			System.out.println(""+clientAdmin.getStastInterface(inter).getDescriptor().getProperties().keySet());
		}
		
	}
	
	
	@Test
	public void haveIpSlasStadistic() throws Exception{
		List<IpSla> ipSlas=clientAdmin.getIpSlas();
		System.out.println("no hay ipslas"+ipSlas.isEmpty());
		Assume.assumeTrue(containIpSlas());		
		for(IpSla ipslas:ipSlas){
			assertFalse(clientAdmin.getPropertiesIPSLAs(ipslas).isEmpty());
		}
		
	}
	
	private boolean containIpSlas() throws Exception{
		List<IpSla> ipSlas=clientAdmin.getIpSlas();
		if(ipSlas==null || ipSlas.isEmpty()){
			return false;
		}
		return true;
	}
	
	
	@Test
	public void memoryRamUsed() throws Exception{
		List<DeviceResource> resources=clientAdmin.getResourcesByType(Type.STORAGE_RAM);
		for(DeviceResource reso:resources){			
			Number num =clientAdmin.getUsedValueStatNumber(reso);
			Long valueMemory=num.longValue();
			assertTrue("la memoria se desbordo",valueMemory>=1 );
				
		}
		
	}
	
	@Test
	public void processorUsed() throws  Exception{
		List<DeviceResource> resources=clientAdmin.getResourcesByType(Type.DEVICE_PROCESSOR);
		for(DeviceResource reso:resources){
			Number num=(Number) clientAdmin.getUsedValueStatNumber(reso);
			int procesorUsed=num.intValue();
			assertFalse("the procesor is overload",procesorUsed>100 );
			assertFalse("the procesor is download",procesorUsed<0);
			assertFalse("the procesor is not in use",procesorUsed==0);
			

		}
	}
	
	@Test
	public void virtualMemoryUse() throws Exception{
		
		List<DeviceResource>  resources=clientAdmin.getResourcesByType(Type.STORAGE_VIRTUAL_MEMORY);
		for(DeviceResource reso:resources){
			Number num=clientAdmin.getUsedValueStatNumber(reso);
			Long used=num.longValue();
			assertTrue("the storage vitual memory is <0",used>=0);
		}
		
	}
	
	@Test
	public void storageDiskUse() throws Exception{
		
		List<DeviceResource> resources=clientAdmin.getResourcesByType(Type.STORAGE_FIXED_DISK);
		for(DeviceResource reso:resources){
			Number num=clientAdmin.getUsedValueStatNumber(reso);
			Long used=num.longValue();
			assertTrue("the disk is <0",used>=0);
		}
		
	}
	
	@Test
	public void OSPFLinkStatus() throws Exception{
		Assume.assumeTrue(existOSPF());
		List<DeviceResource> resources=clientAdmin.getResourcesByType(Type.OSPF_LINK);
		for(DeviceResource reso:resources){
			assertNotNull(clientAdmin.getStatResource(reso));
		}
		
	}
	
	private boolean existOSPF(){
		try{
			clientAdmin.getResourcesByType(Type.OSPF_LINK);
			return true;
		}catch(Exception ex){
			return false;
		}
	}
	
	@Test 
	public void BGPSession() throws Exception{
		Assume.assumeTrue(existBGP());
		List<DeviceResource> resources=clientAdmin.getResourcesByType(Type.BGP_SESSION);
		for(DeviceResource reso:resources){
			assertNotNull(clientAdmin.getStatResource(reso));
		}
	}
	
	private boolean existBGP(){
		try{
			clientAdmin.getResourcesByType(Type.BGP_SESSION);
			return true;
		}catch(Exception ex){
			return false;
		}
	}
	
	@Test
	public void testQous() throws Exception{
		List<QosClass> qous=clientAdmin.getQous();
		assertNotNull("el dispositivo no contine qous",qous);
		assertFalse("los qous estan vacioas",qous.isEmpty());
		
	}
	
	
	@Test
	public void checkQous() throws Exception{
		List<QosClass> qous=clientAdmin.getQous();
		for(QosClass qo:qous){
			System.out.println(qo.toString());
		}
	}
	@Test
	public void QOUStatus() throws Exception{
		List<QosClass> qos=clientAdmin.getQous();
		assertNotNull("no contains qos",qos);
	}
	
	
	@Test
	public void containFanSensor() throws Exception{
		assertNotNull("no contains fansensor",clientAdmin.getResourcesByType(Type.FAN_SENSOR));
	}
	///UPS TEST 

	@Test
	public void voltageStatus() throws Exception{
		List<DeviceResource> resources=clientAdmin.getResourcesByType(Type.UPS_VOLTAGE);
		for(DeviceResource reso:resources){
			Number num=clientAdmin.getUsedValueStatNumber(reso);
			Long voltage=num.longValue();
			assertTrue("the voltage does not correspond",voltage>=0 && voltage<=500);
		}
	}
	
	
	@Test
	public void autonomyStatus() throws Exception{
		List<DeviceResource> resources=clientAdmin.getResourcesByType(Type.UPS_AUTONOMY);
		for(DeviceResource reso:resources){
			Number num=clientAdmin.getUsedValueStatNumber(reso);
			Long voltage=num.longValue();
			assertTrue("the battery value is bad",voltage>=0 && voltage<=100);
		}
	}
	
	
	//PIPELINE
	@Test
	public void VoltageBattery() throws Exception{
		List<DeviceResource> resources=clientAdmin.getResourcesByType(Type.PIPELINE_BATTERY_VOLTAGE_SENSOR);
		for(DeviceResource reso:resources){
			Number num=clientAdmin.getUsedValueStatNumber(reso);
			Long voltaje=num.longValue();
			assertTrue("voltaje is incorrect",voltaje>0);
		}
	}
	
	@Test
	public void VoltageSensor() throws Exception{
		List<DeviceResource> resources=clientAdmin.getResourcesByType(Type.PIPELINE_RTU_VOLTAGE_SENSOR);
		for(DeviceResource reso:resources){
			Number num=clientAdmin.getUsedValueStatNumber(reso);
			Long voltaje=num.longValue();
			assertTrue("voltaje is incorrect",voltaje>0);
		}
	}
	
	@Test
	public void VoltageInput() throws Exception{
		List<DeviceResource> resources=clientAdmin.getResourcesByType(Type.PIPELINE_INPUT_CURRENT_SENSOR);
		for(DeviceResource reso:resources){
			Number num=clientAdmin.getUsedValueStatNumber(reso);
			Long voltaje=num.longValue();
			assertTrue("voltaje is incorrect",voltaje>0);
		}
	}
	
	@Test
	public void VoltageOutPut() throws Exception{
		List<DeviceResource> resources=clientAdmin.getResourcesByType(Type.PIPELINE_OUTPUT_CURRENT_SENSOR);
		for(DeviceResource reso:resources){
			Number num=clientAdmin.getUsedValueStatNumber(reso);
			Long voltaje=num.longValue();
			assertTrue("voltaje is incorrect",voltaje>0);
		}
	}
	
	@Test
	public void temperatureSensor() throws Exception{
		List<DeviceResource> resources=clientAdmin.getResourcesByType(Type.PIPELINE_TEMPERATURE_SENSOR);
		for(DeviceResource reso:resources){
			Number num=clientAdmin.getUsedValueStatNumber(reso);
			Long voltaje=num.longValue();
			assertTrue("voltaje is incorrect",voltaje>0);
		}
	}
	
	@Test
	public void solarPanelSensor() throws Exception{
		List<DeviceResource> resources=clientAdmin.getResourcesByType(Type.PIPELINE_SOLAR_PANEL_CURRENT_SENSOR);
		for(DeviceResource reso:resources){
			Number num=clientAdmin.getUsedValueStatNumber(reso);
			Long voltaje=num.longValue();
			assertTrue("voltaje is incorrect",voltaje>0);
		}
	}
	
	
	
	//nti
	
	@Test
	public void ntiInternalsensor() throws Exception{
		List<DeviceResource> resources=clientAdmin.getResourcesByType(Type.NTI_INTERNAL_SENSOR);
		for(DeviceResource reso:resources){
			assertNotNull("no contain ntisensor",clientAdmin.getUsedValueStat(reso));
		}
	}
	@Test
	public void ntiExternalsensor() throws Exception{
		List<DeviceResource> resources=clientAdmin.getResourcesByType(Type.NTI_EXTERNAL_SENSOR);
		for(DeviceResource reso:resources){
			assertNotNull("no contain ntisensor",clientAdmin.getUsedValueStat(reso));
		}
	}
	@Test
	public void ntiIP() throws Exception{
		List<DeviceResource> resources=clientAdmin.getResourcesByType(Type.NTI_IP_DEVICE);
		for(DeviceResource reso:resources){
			assertNotNull("no contain ntisensor",clientAdmin.getUsedValueStat(reso));
		}
	}
	@Test
	public void ntiRelaysensor() throws Exception{
		List<DeviceResource> resources=clientAdmin.getResourcesByType(Type.NTI_RELAY_OUTPUT);
		for(DeviceResource reso:resources){
			assertNotNull("no contain ntisensor",clientAdmin.getUsedValueStat(reso));
		}
	}
	
	
	
	//Telecontroller
	@Test
	public void currentSensor() throws Exception {
		List<DeviceResource> resources=clientAdmin.getResourcesByType(Type.TELECONTROLLER_CURRENT_SENSOR);
		for(DeviceResource reso:resources){
			assertNotNull("",reso);
		}
	}
	@Test
	public void dorrsensor() throws Exception {
		List<DeviceResource> resources=clientAdmin.getResourcesByType(Type.TELECONTROLLER_DOOR_SENSOR);
		for(DeviceResource reso:resources){
			assertNotNull("",reso);
		}
	}
	@Test
	public void hhumiditysensor() throws Exception {
		List<DeviceResource> resources=clientAdmin.getResourcesByType(Type.TELECONTROLLER_HUMIDITY_SENSOR);
		for(DeviceResource reso:resources){
			assertNotNull("",reso);
		}
	}
	@Test
	public void telecontrollerport() throws Exception {
		List<DeviceResource> resources=clientAdmin.getResourcesByType(Type.TELECONTROLLER_PORT);
		for(DeviceResource reso:resources){
			assertNotNull("",reso);
		}
	}
	@Test
	public void temperatureTelecontrollersensor() throws Exception {
		List<DeviceResource> resources=clientAdmin.getResourcesByType(Type.TELECONTROLLER_TEMPERATURE_SENSOR);
		for(DeviceResource reso:resources){
			assertNotNull("",reso);
		}
	}
	@Test
	public void temperaturesensor() throws Exception {
		List<DeviceResource> resources=clientAdmin.getResourcesByType(Type.TEMPERATURE_SENSOR);
		for(DeviceResource reso:resources){
			assertNotNull("",reso);
		}
	}
	

}
