package com.gdc.nms.test.runner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gdc.nms.model.Device;
import com.gdc.nms.model.DeviceResource;
import com.gdc.nms.model.Interface;
import com.gdc.nms.model.IpSla;
import com.gdc.nms.model.QosClass;
import com.gdc.nms.model.util.IpSlas;
import com.gdc.nms.tester.client.Client;
import com.gdc.nms.tester.common.response.DeviceInterfacesResponse;
import com.gdc.nms.tester.common.response.DeviceIpSlasResponse;
import com.gdc.nms.tester.common.response.DeviceQousResponce;
import com.gdc.nms.tester.common.response.DeviceResourcesResponse;
import com.gdc.nms.tester.common.response.SqlInsertableRowDescriptorResponse;

import jade.core.AID;

public class ClientManager {
	
	private Client client;
	private AID	receiver;
	private Device device;
	private DeviceResource resource;
	public DeviceResource getResource() {
		return resource;
	}
	public void setResource(DeviceResource resource) {
		this.resource = resource;
	}
	public ClientManager(Client client,AID receiver){
		this.client=client;
		this.receiver=receiver;
		
	}
	public Device getDevice() {
		return device;
	}
	public void setDevice(Device device) {
		this.device = device;
	}
	public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
		this.client = client;
	}
	public AID getReciver() {
		return receiver;
	}
	public void setReciver(AID receiver) {
		this.receiver = receiver;
	}
	
	private int getTimeOut(){
		System.out.println("device"+device);
		return device.getSnmpTimeout()*2;
	}
	public Set<Interface> getIntefaces() throws Exception{
		
		DeviceInterfacesResponse interf=client.getDeviceInterfaces(device, receiver, getTimeOut());
		
		return interf.getInterfaces();
	}
	
	public List<DeviceResource>  getResources() throws Exception{
		DeviceResourcesResponse resources=client.getDeviceResources(device, receiver, getTimeOut());
		return resources.getDeviceResources();
	}
	
	
	public List<IpSla> getIpSlas() throws Exception{
		DeviceIpSlasResponse response=client.getDeviceIpSlas(device, receiver, getTimeOut());
		return response.getIpSlas();
	}
	
	public List<QosClass> getQous() throws Exception{
		DeviceQousResponce qous=client.getDeviceQous(device, receiver, getTimeOut());
		return qous.getQos();
	}
	/**
	 * this method return the stat or resources previusly setting by setResource
	 * @return tehe stadistic of the device resource
	 * @throws Exception
	 */
	public SqlInsertableRowDescriptorResponse getStatResource() throws Exception{
		return client.getDeviceResourceStats(device, resource, receiver, getTimeOut());
	}
	
	public SqlInsertableRowDescriptorResponse getStatResource(DeviceResource reso) throws Exception{
		
		return client.getDeviceResourceStats(device, reso, receiver, getTimeOut());
	}
	
	public SqlInsertableRowDescriptorResponse getStastInterface(Interface iface) throws Exception{
		return client.getDeviceResourceStats(device, iface, receiver, getTimeOut());

	}
	
	public SqlInsertableRowDescriptorResponse getStatipSla(IpSla ipSlas) throws Exception{
		return client.getDeviceResourceStats(device,ipSlas , receiver, getTimeOut());
	}
	
	public SqlInsertableRowDescriptorResponse getQous(QosClass qous) throws Exception{
		return client.getDeviceQousStast(device, qous, receiver, getTimeOut());
	}
	
	
	public DeviceIpSlasResponse getIpSla() throws Exception{
		return client.getDeviceIpSlas(device, receiver, getTimeOut());
	}
	
	public Map<String,Object> getPropertiesIPSLAs(IpSla ipSlas) throws Exception{
		return getStatipSla(ipSlas).getDescriptor().getProperties();
	}
	
	public List<DeviceResource> getResourcesByType(DeviceResource.Type type) throws Exception{
		List<DeviceResource> resources=getResources();
		List<DeviceResource> newList=new ArrayList<DeviceResource>();
		for(DeviceResource reso:resources){
			if(reso.getResourceType()==type){
				newList.add(reso);
			}
		}
		if(newList.isEmpty()){
			throw  new Exception("this deviceResource not have "+type);
		}
		return newList;
	}
	
	public Object getPropertiDecriptor(SqlInsertableRowDescriptorResponse decriptor,String property){
		return decriptor.getDescriptor().getProperties().get(property);
	}
	/**
	 * get the value of field Used of the device reources input if not found throw exception
	 * @param resource
	 * @return
	 * @throws Exception
	 */
	public Object getUsedValueStat(DeviceResource resource) throws Exception{
		return getStatResource(resource).getDescriptor().getProperties().get("USED");
	}
	
	/**
	 * get value of statistics Used and convert object Number for easy 
	 * conversion to number value this method is for you are sure the device return number value
	 * @param resource
	 * @return
	 * @throws Exception  if cannot obtain the used valuo or cast the object
	 */
	public Number  getUsedValueStatNumber(DeviceResource resource) throws Exception{
		Object obj;
	
			obj = getUsedValueStat(resource);
			return (Number) obj;
	
	}
	
	
	
	
}
