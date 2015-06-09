package com.gdc.nms.test.especific.process;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;


import com.gdc.nms.model.QosClass;
import com.gdc.nms.test.configuration.TestRule;
import com.gdc.nms.test.runner.ClientManager;
import com.gdc.nms.test.runner.TestManager;

public class VYattaTest {
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
	public void QOUStatus() throws Exception{
		List<QosClass> qos=clientAdmin.getQous();
		assertNotNull("no contains qos",qos);
	}
	
}
