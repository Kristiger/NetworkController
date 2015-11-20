package com.main.provider;

import java.util.Map;

import com.basic.elements.Device;
import com.util.device.DeviceUtil;

public class DataProvider {
	private static String IP;
	private static String PORT;
	
	private static Map<String, Device> devices = DeviceUtil.getDevices();
	private static Map<String, QosPolicy> qoses = 
	
	
	public static String getIP() {
		return IP;
	}
	public static void setIP(String iP) {
		IP = iP;
	}
	public static String getPORT() {
		return PORT;
	}
	public static void setPORT(String pORT) {
		PORT = pORT;
	}
	
}
