package com.gdc.nms.mock;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.mail.MethodNotSupportedException;

import com.gdc.nms.model.Device;
import com.gdc.nms.model.DeviceResource;
import com.gdc.nms.model.Interface;
import com.gdc.nms.model.Interface.IfXTableMode;
import com.gdc.nms.model.IpSla;
import com.gdc.nms.model.NmsEntity;
import com.gdc.nms.model.NmsProperty;
import com.gdc.nms.model.QosClass;
import com.gdc.nms.server.drivers.snmp.Driver;
import com.gdc.nms.server.drivers.snmp.SnmpConnector;
import com.gdc.nms.server.drivers.snmp.SnmpConnectorException;
import com.gdc.nms.server.eclipselink.SqlInsertableRowDescriptor;

public class MockDriver implements Driver {
	private final Lock lock=new ReentrantLock();
	private final String hostName="mock device";
	//if you will modify this sysoid you remember modify the device.properties file
	public static final String SYSOID="1.3.6.1.4.1.99999";
	protected Device device;

	@Override
	public void bind(Device device) {
		   lock.lock();
	       this.device = device;
	        
	}

	@Override
	public void unbind() {
	
            lock.unlock();
        
		
	}

	@Override
	public Device getDevice() {
		return this.device;
	}

	@Override
	public SnmpConnector getSnmpConnector() {
		return null;
	}

	@Override
	public void setSnmpConnector(SnmpConnector snmpConnector) {
		
	}

	@Override
	public Set<Interface> getInterfaces() throws SnmpConnectorException {
		return Collections.emptySet();
	}

	@Override
	public List<DeviceResource> getDeviceResources() throws SnmpConnectorException {
		return Collections.emptyList();
	}

	@Override
	public List<QosClass> getQosClasses(List<Integer> ifIndexes)
			throws SnmpConnectorException {
		return null;
	}

	@Override
	public List<IpSla> getIpSlas() throws SnmpConnectorException {
		return Collections.emptyList();
	}

	@Override
	public Map<NmsProperty, Object> discoverDevice()
			throws SnmpConnectorException {
		
		return Collections.emptyMap();
	}

	@Override
	public String getSysOid() throws SnmpConnectorException {
		
		return  SYSOID;
	}

	@Override
	public String getHostname() throws SnmpConnectorException {
		return this.hostName;
	}

	@Override
	public long getSysUpTime() throws SnmpConnectorException {
		
		return 0;
	}

	@Override
	public Map<NmsEntity, Map<NmsProperty, Object>> retrieveChanges()
			throws SnmpConnectorException {
		
		return Collections.emptyMap();
	}

	@Override
	public String getInventory() throws SnmpConnectorException,
			MethodNotSupportedException {
		
		return "";
	}

	@Override
	public IfXTableMode getIfXTableMode(Interface iface) {
		
		return null;
	}

	@Override
	public SqlInsertableRowDescriptor getStats(long time, String tableName,
			boolean measureLatency) throws SnmpConnectorException {
		
		return null;
	}

	@Override
	public SqlInsertableRowDescriptor getStats(DeviceResource deviceResource,
			long time, String tableName) throws SnmpConnectorException {
		
		return null;
	}

	@Override
	public SqlInsertableRowDescriptor getStats(Interface iface, long time,
			String tableName) throws SnmpConnectorException {
		
		return null;
	}

	@Override
	public SqlInsertableRowDescriptor getMobileStats(Interface iface,
			long time, String tableName) throws SnmpConnectorException {
		
		return null;
	}

	@Override
	public SqlInsertableRowDescriptor getStats(QosClass qosClass, long time,
			String tableName) throws SnmpConnectorException {
		
		return null;
	}

	@Override
	public SqlInsertableRowDescriptor getStats(IpSla ipSla, long time,
			String tableName) throws SnmpConnectorException {
		
		return null;
	}

	@Override
	public boolean measureLatency() {
		
		return false;
	}

	@Override
	public int getLatency(int sleepMillis) throws SnmpConnectorException {
		
		return 0;
	}

	@Override
	public Set<Integer> getIfIndexesWithQos() throws SnmpConnectorException {
		return Collections.emptySet();
	}

}
