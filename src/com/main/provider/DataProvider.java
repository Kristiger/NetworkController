package com.main.provider;

import java.util.List;
import java.util.Map;

import com.basic.elements.Device;
import com.basic.elements.Flow;
import com.basic.elements.Port;
import com.basic.elements.Switch;
import com.main.app.qos.QosPolicy;
import com.main.app.qos.QosQueue;
import com.main.app.qos.QosUtil;
import com.main.app.qos.QueueUtil;
import com.util.device.DeviceUtil;

public class DataProvider {
	private static String IP;
	private static String PORT;
	
	private static Map<String, Device> devices = DeviceUtil.getDevices();
	private static Map<String, QosPolicy> qoses = QosUtil.getQoses();
	private static Map<String, QosQueue> queues = QueueUtil.getQueues();
	private static List<Switch> switches;
	private static List<Flow> flows;
	private static List<Flow> staticflows;
	private static List<Port> ports;
	
	
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
	public static Map<String, Device> getDevices() {
		return devices;
	}
	public static Map<String, QosPolicy> getQoses() {
		return qoses;
	}
	public static Map<String, QosQueue> getQueues() {
		return queues;
	}
	public static List<Switch> getSwitches() {
		return switches;
	}
	public static List<Flow> getFlows() {
		return flows;
	}
	public static List<Flow> getStaticflows() {
		return staticflows;
	}
	public static List<Port> getPorts() {
		return ports;
	}
}
