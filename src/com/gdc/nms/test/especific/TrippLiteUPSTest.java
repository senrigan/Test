package com.gdc.nms.test.especific;



import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Rule;


import org.junit.Test;

import com.gdc.nms.model.DeviceResource;
import com.gdc.nms.model.DeviceResource.Type;
import com.gdc.nms.test.configuration.TestRule;
import com.gdc.nms.test.runner.ClientManager;
import com.gdc.nms.test.runner.TestManager;

public class  TrippLiteUPSTest {
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
	
}
