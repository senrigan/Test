package com.gdc.nms.test.util;

import com.gdc.nms.model.Device;

public class SillyDevice {
	
	private Device device;
	private String  driverName;
	private String 	serverHost;
	
	public String getServerHost() {
		return serverHost;
	}
	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}
	public Device getDevice() {
		return device;
	}
	public void setDevice(final Device device) {
		this.device = device;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	@Override
	public String toString() {
		return "SillyDevice [driverName=" + driverName + ", serverHost="
				+ serverHost + "]";
	}
	

	
	

}
