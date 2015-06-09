package com.gdc.nms.test.generic;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.gdc.nms.model.Device;
import com.gdc.nms.model.DeviceResource;
import com.gdc.nms.test.configuration.TestRule;
import com.gdc.nms.test.runner.ClientManager;
import com.gdc.nms.test.runner.TestManager;


public class TestUPSDevice {
	
	private static ClientManager clientAdmin;
	@Rule
	public TestRule ruleInform=new TestRule();
	
	
	@BeforeClass
	public static  void setUp(){
		TestManager manager=TestManager.getIntance();
		clientAdmin=manager.getClientManager();
		clientAdmin.setDevice(manager.getSillyDevice().getDevice());
		 
	}

	@Test
	public void testResource() throws Exception{
		List<DeviceResource> resources=clientAdmin.getResources();
		System.out.println("->>>>>>>>>>>>>>>>>>>>>>>>>>");
		for(DeviceResource r : resources){
			System.out.println(r.getDevice());
		}
		assertNotNull("los recursos devolvio nulo",resources);
		assertFalse("la lista esta vacia",resources.isEmpty());
	}
	
	
	


}
