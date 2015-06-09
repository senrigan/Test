package com.gdc.nms.test.especific.process;



import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.gdc.nms.model.DeviceResource;
import com.gdc.nms.model.DeviceResource.Type;
import com.gdc.nms.test.configuration.TestRule;
import com.gdc.nms.test.runner.ClientManager;
import com.gdc.nms.test.runner.TestManager;

public class Nti_Net_Snmp_LinuxTest {
	//this test correspon to NtiDriver
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
	public void sensor() throws Exception{
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
	

}
