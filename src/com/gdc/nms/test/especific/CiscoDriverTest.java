package com.gdc.nms.test.especific;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.gdc.nms.model.DeviceResource;
import com.gdc.nms.model.IpSla;
import com.gdc.nms.test.configuration.TestRule;
import com.gdc.nms.test.runner.ClientManager;
import com.gdc.nms.test.runner.TestManager;

public class CiscoDriverTest {
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
			List<DeviceResource> resources=clientAdmin.getResourcesByType(DeviceResource.Type.STORAGE_RAM);
			for(DeviceResource reso:resources){			
				Number num =clientAdmin.getUsedValueStatNumber(reso);
				Long valueMemory=num.longValue();
				assertTrue("la memoria se desbordo",valueMemory>=1 );
					
			}
		
	}
	
	@Test
	public void processorUsed() throws  Exception{
		List<DeviceResource> resources=clientAdmin.getResourcesByType(DeviceResource.Type.DEVICE_PROCESSOR);
		for(DeviceResource reso:resources){
			Number num=(Number) clientAdmin.getUsedValueStatNumber(reso);
			int procesorUsed=num.intValue();
			assertFalse("the procesor is overload",procesorUsed>100 );
			assertFalse("the procesor is download",procesorUsed<0);
			assertFalse("the procesor is not in use",procesorUsed==0);

		}
	}
}
