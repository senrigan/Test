package com.gdc.nms.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.gdc.nms.model.DeviceResource;
import com.gdc.nms.model.Interface;
import com.gdc.nms.test.configuration.TestRule;
import com.gdc.nms.test.runner.ClientManager;
import com.gdc.nms.test.runner.TestManager;


//@RunWith(Parameterized.class)
public class TestDevice {
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
	public void TestInterface() throws Exception{
		Set<Interface> interfaces=clientAdmin.getIntefaces();
		assertNotNull("el objeto interfaces es nulo",interfaces);
		assertFalse("no existen interfaces",interfaces.isEmpty());
		
	}

	@Test
	public void testResource() throws Exception{
		List<DeviceResource> resources=clientAdmin.getResources();
		assertNotNull("los recursos devolvio nulo",resources);
		assertFalse("la lista esta vacia",resources.isEmpty());
			
	}


	

	
	
	
}