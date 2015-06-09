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

public class PipeLineTest {
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
	
	

	
	
	
}
