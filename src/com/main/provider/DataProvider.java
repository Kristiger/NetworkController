package com.main.provider;

import java.io.IOException;
import java.util.ArrayList;
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
import com.main.app.staticflow.json.StaticFlowManagerJSON;
import com.tools.table.DeviceToTable;
import com.tools.table.FlowToTable;
import com.tools.table.PortToTable;
import com.tools.util.JSONException;
import com.util.controller.ControllerJSON;
import com.util.device.DeviceUtil;
import com.util.flow.FlowJSON;
import com.util.sw.SwitchJSON;
import com.util.sw.SwitchesJSON;

public class DataProvider {
	private static String IP;
	private static String PORT;

	private static Map<String, Device> devices = null;
	private static Map<String, QosPolicy> qoses = null;
	private static Map<String, QosQueue> queues = null;
	private static Map<String, Switch> switches = null;
	private static Map<String, Flow> staticFlows = null;

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

	public static Map<String, Device> getDevices(boolean update)
			throws JSONException {
		if (devices == null || update) {
			devices = DeviceUtil.getDevices(true);
		}
		return devices;
	}

	public static Map<String, QosPolicy> getQoses() {
		if (qoses == null) {
			qoses = QosUtil.getQoses();
		}
		return qoses;
	}

	public static Map<String, QosQueue> getQueues() {
		if (queues == null) {
			queues = QueueUtil.getQueues();
		}
		return queues;
	}

	public static Map<String, Switch> getSwitches(boolean update)
			throws JSONException {
		if (switches == null || update) {
			switches = SwitchesJSON.getSwitches();
		}
		return switches;
	}

	public static Switch getSwitch(String dpid) throws JSONException {
		// TODO Auto-generated method stub
		if (switches != null) {
			return switches.get(dpid);
		}
		return null;
	}

	public static void updateSwitch(Switch sw) throws JSONException {
		// TODO Auto-generated method stub
		SwitchesJSON.updateSwitch(sw);
	}

	public static String[][] getPortTableFormat(List<Port> ports) {
		// TODO Auto-generated method stub
		return PortToTable.getPortTableFormat(ports);
	}

	public static String[][] getFlowTableFormat(List<Flow> flows) {
		// TODO Auto-generated method stub
		return FlowToTable.getFlowTableFormat(flows);
	}

	public static List<Flow> getFlows(String dpid) throws IOException,
			JSONException {
		// TODO Auto-generated method stub
		return FlowJSON.getFlows(dpid);
	}

	public static Map<String, Flow> getStaticFlows(String currentSwtichDpid, boolean b) {
		// TODO Auto-generated method stub
		return staticFlows;
	}

	public static Map<String, String> getControllerInfo() throws JSONException {
		return ControllerJSON.getControllerInfo();
	}

	public static String[][] getDeviceTableFormat(Map<String, Device> devices) {
		// TODO Auto-generated method stub
		return DeviceToTable.deviceSummariesToTable(devices);
	}

	public static List<Flow> getRealFlows(String sw) throws IOException,
			JSONException {
		// TODO Auto-generated method stub
		if (switches != null) {
			return FlowJSON.getFlows(sw);
		}
		return new ArrayList<Flow>();
	}
}
