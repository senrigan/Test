package com.gdc.nms.test.util.connection;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.gdc.nms.model.Device;
import com.gdc.nms.model.Project;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class DeviceXML {
	public static String toXML(Device device){
		XStream xstream = new XStream(new DomDriver());
		return xstream.toXML(device);
	}
	
	public static String projectsToXML(List<Project> projects){
		XStream xstream = new XStream(new DomDriver());
		return xstream.toXML(projects);
	}
	
	public static String toXML(ArrayList<Device> devices){
		XStream xstream=new XStream(new DomDriver());
		return xstream.toXML(devices);
		
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<Device> fromXML(String xmlFile){
		XStream xstream = new XStream(new DomDriver());
		System.out.println("paseando"+xmlFile);
		File file=new File(xmlFile);
		String fileXML="";
		try {
			fileXML=IOUtils.toString(file.toURI());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (ArrayList<Device>) xstream.fromXML(fileXML);
//		return (ArrayList<Device>) xstream.fromXML(new File(xmlFile));
//		return  (ArrayList<Device> ) xstream.fromXML(xmlFile);
	}
}
