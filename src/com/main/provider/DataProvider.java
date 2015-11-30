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

	private static Map<String, QosPolicy> qoses = null;
	private static Map<String, QosQueue> queues = null;
	private static Map<String, Device> devices = null;
	private static Map<String, Switch> switches = null;
	private static Map<String, Flow> staticFlows = null;

	public static Map<String, String> getControllerInfo() throws JSONException {
		return ControllerJSON.getControllerInfo();
	}

	public static Map<String, Device> getDevices(boolean update) throws JSONException {
		// init from db
		if (devices == null) {
			devices = DeviceUtil.getDevicesFromDB();
			if (devices.size() > 0) {
				for (String mac : devices.keySet()) {
					String qosUuid = DeviceUtil.getQosForDevice(mac);
					if (qosUuid != null)
						devices.get(mac).setQosUuid(qosUuid);
				}
			}
		}
		// update from controller
		if (update) {
			devices = DeviceUtil.getDevices();
		}
		return devices;
	}

	public static String[][] getDeviceTableFormat(Map<String, Device> devices) {
		// TODO Auto-generated method stub
		return DeviceToTable.deviceSummariesToTable(devices);
	}

	public static List<Flow> getFlows(String dpid) throws IOException, JSONException {
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
			qoses = QosUtil.getQosFromDB();
			if (qoses.size() > 0) {
				getQueues();
				for (String qosUuid : qoses.keySet()) {
					qoses.get(qosUuid).setQueues(QosUtil.getQueuesForQos(qosUuid));
				}
			}
		}
		return qoses;
	}

	public static Map<String, QosQueue> getQueues() {
		if (queues == null) {
			queues = QueueUtil.getQueuesFromDB();
		}
		return queues;
	}

	public static List<Flow> getRealFlows(String sw) throws IOException, JSONException {
		// TODO Auto-generated method stub
		return FlowJSON.getFlows(sw);
	}

	public static Map<String, Flow> getStaticFlows(String dpid, boolean update) throws IOException, JSONException {
		// TODO Auto-generated method stub
		if (update || staticFlows == null) {
			return StaticFlowManagerJSON.getFlows(dpid);
		}
		return staticFlows;
	}

	public static Switch getSwitch(String dpid) throws JSONException {
		// TODO Auto-generated method stub
		if (switches == null) {
			getSwitches(true);
		}
		return switches.get(dpid);
	}

	public static Map<String, Switch> getSwitches(boolean update) throws JSONException {
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

	public static void updateDeviceStore(Device device, UPDATETYPE type, String key, Object value) {
		// TODO Auto-generated method stub
		switch (type) {
		case INSERT:
			DeviceUtil.addDevice(device);
			break;
		case REMOVE:
			DeviceUtil.removeDevice(device.getMacAddr());
			break;
		case UPDATE:
			DeviceUtil.updateDevice(device.getMacAddr(), key, value);
			break;
		case BAND:
			DeviceUtil.addQosForDevice(device.getMacAddr(), device.getQosUuid());
			break;
		case UNBAND:
			DeviceUtil.removeQosForDevice(device.getMacAddr(), device.getQosUuid());
			break;
		default:
			break;
		}
	}

	public static void updateQosStore(QosPolicy qos, String queueUuid, int queueId, UPDATETYPE type) {
		// TODO Auto-generated method stub
		switch (type) {
		case INSERT:
			QosUtil.addQos(qos);
			break;
		case REMOVE:
			QosUtil.removeQos(qos.getUuid());
			break;
		case BAND:
			QosUtil.addQueueForQos(qos.getUuid(), queueUuid, queueId);
			break;
		case UNBAND:
			QosUtil.removeQueueForQos(qos.getUuid(), queueUuid, queueId);
			break;
		default:
			break;
		}
	}

	public static void updateQueueStore(QosQueue queue, UPDATETYPE type) {
		// TODO Auto-generated method stub
		if (type == UPDATETYPE.INSERT) {
			QueueUtil.addQueue(queue);
		} else if (type == UPDATETYPE.REMOVE) {
			QueueUtil.removeQueue(queue.getUuid());
		}
	}

	public static void updateSwitch(Switch sw) throws JSONException {
		// TODO Auto-generated method stub
		SwitchesJSON.updateSwitch(sw);
	}
}
