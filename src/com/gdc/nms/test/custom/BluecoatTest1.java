package com.gdc.nms.test.custom;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.gdc.nms.model.DeviceResource;
import com.gdc.nms.model.Interface;
import com.gdc.nms.model.IpSla;
import com.gdc.nms.test.configuration.TestRule;
import com.gdc.nms.test.runner.ClientManager;
import com.gdc.nms.test.runner.TestManager;
import com.gdc.nms.tester.common.response.SqlInsertableRowDescriptorResponse;

public class BluecoatTest1 {
	private static ClientManager clientAdmin;
	private static Logger log;
	@Rule
	public TestRule ruleInform=new TestRule();

	@BeforeClass
	public static void SetUP(){
		TestManager manager=TestManager.getIntance();
		clientAdmin=manager.getClientManager();
		clientAdmin.setDevice(manager.getSillyDevice().getDevice());
		log= Logger.getLogger(BluecoatTest1.class);
		
	}
	
	
	@Test
	public void ResourceList(){
		try{
			List<DeviceResource> resources=clientAdmin.getResources();
			for(DeviceResource reso:resources){
				System.out.println("tipo de recurso"+reso.getResourceType()+"\n key: "+clientAdmin.getStatResource(reso).getDescriptor().getProperties().keySet());
			}
		}catch(Exception e){
			log.error("occurrio error en obtener los recursos"+e.getCause());
		}
		
	}
	
	@Test
	public void InterfaceList(){
		try{
			Set<Interface> interfaces=clientAdmin.getIntefaces();
			for(Interface inter:interfaces){
				System.out.println("tipo de interfaz"+inter.getAlias()+" "+inter.getAliasOrName()+" "+inter.getDescription());
				System.out.println(""+clientAdmin.getStastInterface(inter).getDescriptor().getProperties().keySet());
			}
		}catch(Exception e){
			log.error("occurrio error en obtener los recursos"+e.getCause());
		}
		
	}
	
	@Test
	public void haveIpSlasStadistic(){
		try {
			List<IpSla> ipSlas=clientAdmin.getIpSlas();
			System.out.println("no hay ipslas"+ipSlas.isEmpty());
			assertFalse("no have ipSlas",ipSlas.isEmpty());
//			for(IpSla ipslas:ipSlas){
//				System.out.println(ipslas.getAlias());
//				System.out.println(ipslas.getKey());
//				System.out.println(ipslas.getTargetAddress());
//				System.out.println(Arrays.toString(ipslas.getNmsProperties()));
//				//System.out.println(clientAdmin.getStatipSla(ipslas).getDescriptor().getProperties().keySet());
//			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//log.error("ocurrio error al obtener las estadisticas de ipSlas");
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void memoryRamUsed(){
		try {
			List<DeviceResource> resources=clientAdmin.getResourcesByType(DeviceResource.Type.STORAGE_RAM);
			for(DeviceResource reso:resources){			
				Object obj =clientAdmin.getPropertiDecriptor(clientAdmin.getStatResource(reso),"USED");
				Long valueMemory=(Long)obj;
				assertTrue("la memoria se desbordo",valueMemory>=1 );
					
			}
			log.info("paso la prueba de memoria");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("ocurrio un error en memoryRam test"+Arrays.toString(e.getStackTrace()));
			e.printStackTrace();
		}
	}
	
	@Test
	public void processorUsed(){
		List<DeviceResource> resources;
		try {
			//resources = getResourcesTypeList(DeviceResource.Type.DEVICE_PROCESSOR);
			resources=clientAdmin.getResourcesByType(DeviceResource.Type.DEVICE_PROCESSOR);
			if(!resources.isEmpty()){
				for(DeviceResource reso:resources){
					SqlInsertableRowDescriptorResponse descripto=clientAdmin.getStatResource(reso);
					Object obj=descripto.getDescriptor().getProperties().get("USED");
					Number num=(Number) obj;
					System.out.println("procesor Value"+num.intValue());
					assertTrue("the procesor is overload",num.intValue()>0 && num.intValue()<=100 );
					assertFalse("the procesor is download",num.intValue()<0);
					
					
				}
			}else{
				fail("this device not contains "+DeviceResource.Type.DEVICE_PROCESSOR);
				log.error("encontro un error no se encontro el "+DeviceResource.Type.DEVICE_PROCESSOR);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("error cause "+e.getCause());
			System.out.println("message "+e.getMessage());
			e.printStackTrace();
		}
		
		
		
	}
}
