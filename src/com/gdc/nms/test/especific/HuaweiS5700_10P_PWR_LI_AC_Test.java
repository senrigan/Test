package com.gdc.nms.test.especific;

import static org.junit.Assert.assertFalse;
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

public class HuaweiS5700_10P_PWR_LI_AC_Test {
	//this test correspond HuaweiS5710Driver
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
	
}
