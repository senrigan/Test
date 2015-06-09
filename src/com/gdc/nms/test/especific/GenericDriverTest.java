package com.gdc.nms.test.especific;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
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

public class GenericDriverTest {
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
	public void BGPSession() throws Exception{
		List<DeviceResource> resources=clientAdmin.getResourcesByType(Type.BGP_SESSION);
		for(DeviceResource reso:resources){
			assertNotNull("no exist bgpSession",clientAdmin.getStatResource(reso));
		}
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
	
	
	
	
	
	
	
}