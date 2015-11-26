package com.main.provider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.basic.elements.Device;
import com.basic.elements.Flow;
import com.basic.elements.Port;
import com.basic.elements.Switch;
import com.basic.elements.UPDATETYPE;
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
import com.util.db.DBHelper;
import com.util.device.DeviceUtil;
import com.util.flow.FlowJSON;
import com.util.sw.SwitchesJSON;

public class DataProvider {
	private static String IP;
	private static String PORT;

	private static Map<String, Device> devices = null;
	private static Map<String, QosPolicy> qoses = null;
	private static Map<String, QosQueue> queues = null;
	private static Map<String, Switch> switches = null;
	private static Map<String, Flow> staticFlows = null;

	private static DBHelper db = null;

	public static Map<String, String> getControllerInfo() throws JSONException {
		return ControllerJSON.getControllerInfo();
	}

	public static Map<String, Device> getDevices(boolean update)
			throws JSONException {
		if (devices == null || update) {
			devices = DeviceUtil.getDevices(true);
		}
		return devices;
	}

	public static String[][] getDeviceTableFormat(Map<String, Device> devices) {
		// TODO Auto-generated method stub
		return DeviceToTable.deviceSummariesToTable(devices);
	}

	public static List<Flow> getFlows(String dpid) throws IOException,
			JSONException {
		// TODO Auto-generated method stub
		return FlowJSON.getFlows(dpid);
	}

	public static String[][] getFlowTableFormat(List<Flow> flows) {
		// TODO Auto-generated method stub
		return FlowToTable.getFlowTableFormat(flows);
	}

	public static String getIP() {
		return IP;
	}

	public static String getPORT() {
		return PORT;
	}

	public static String[][] getPortTableFormat(List<Port> ports) {
		// TODO Auto-generated method stub
		return PortToTable.getPortTableFormat(ports);
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

	public static List<Flow> getRealFlows(String sw) throws IOException,
			JSONException {
		// TODO Auto-generated method stub
		if (switches != null) {
			return FlowJSON.getFlows(sw);
		}
		return new ArrayList<Flow>();
	}

	public static Map<String, Flow> getStaticFlows(String currentSwtichDpid,
			boolean b) throws IOException, JSONException {
		// TODO Auto-generated method stub
		if (b || staticFlows == null) {
			return StaticFlowManagerJSON.getFlows(currentSwtichDpid);
		}
		return staticFlows;
	}

	public static Switch getSwitch(String dpid) throws JSONException {
		// TODO Auto-generated method stub
		if (switches != null) {
			return switches.get(dpid);
		}
		return null;
	}

	public static Map<String, Switch> getSwitches(boolean update)
			throws JSONException {
		if (switches == null || update) {
			switches = SwitchesJSON.getSwitches();
		}
		return switches;
	}

	public static void setIP(String iP) {
		IP = iP;
	}

	public static void setPORT(String pORT) {
		PORT = pORT;
	}

	public static void updateQosStore(String vmUuid, QosPolicy qos,
			UPDATETYPE type) {
		// TODO Auto-generated method stub
		db = new DBHelper();
		if (type == UPDATETYPE.INSERT) {
			db.insertQos(qos);
		} else if (type == UPDATETYPE.DELETE) {
			db.removeQos(qos.getUuid());
		} else if (type == UPDATETYPE.BAND) {
			db.insertQosForVm(vmUuid, qos.getUuid());
		} else if (type == UPDATETYPE.UNBAND) {
			db.removeQosToDevice(vmUuid, qos.getUuid());
		}
		db.closeDBConnection();
	}

	public static void updateQueueStore(String qosUuid, QosQueue queue, int i,
			UPDATETYPE type) {
		// TODO Auto-generated method stub
		db = new DBHelper();
		if (type == UPDATETYPE.INSERT) {
			db.insertQueue(queue);
			db.insertQueueForQos(qosUuid, queue.getUuid(), i);
		} else if (type == UPDATETYPE.DELETE) {
			db.removeQueue(queue.getUuid());
			db.removeQueueToQos(qosUuid, queue.getUuid());
		}
		db.closeDBConnection();
	}

	public static void updateSwitch(Switch sw) throws JSONException {
		// TODO Auto-generated method stub
		SwitchesJSON.updateSwitch(sw);
	}

	public static void updateDeviceStore(Device device, UPDATETYPE type,
			String key) {
		// TODO Auto-generated method stub
		db = new DBHelper();
		
		switch (type) {
		case INSERT:
			db.insertDevice(device);
			break;
		case DELETE:
			db.removeDevice(device.getVmUuid());
			break;
		case UPDATE:
			db.updateDevice(key, device);
			break;
		case BAND:
			db.insertQosForVm(device.getVmUuid(), device.getQosUuid());
		default:
			break;
		}
		db.closeDBConnection();
	}
}
