package com.gdc.nms.test.especific;

import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.gdc.nms.model.IpSla;
import com.gdc.nms.test.configuration.TestRule;
import com.gdc.nms.test.runner.ClientManager;
import com.gdc.nms.test.runner.TestManager;

public class _3ComTest {
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
}
