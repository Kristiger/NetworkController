package com.util.device;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.basic.elements.Device;
import com.basic.elements.Port;
import com.basic.elements.Switch;
import com.main.provider.DataProvider;
import com.tools.util.JSONException;
import com.util.xen.XenTools;

public class DeviceUtil {
	private static Map<String, Device> devices = new ConcurrentHashMap<String, Device>();

	private static void getDeviceFromController() throws JSONException {
		devices = DevicesJSON.getDeviceSummaries();
		for (Device device : devices.values()) {
			Switch sw = DataProvider.getSwitch(device.getSwitchDpid());
			if (sw != null) {
				List<Port> ports = sw.getPorts();
				if (ports != null) {
					Iterator<Port> it = ports.iterator();
					while (it.hasNext()) {
						Port port = it.next();
						if (port.getPortNumber().equals(device.getSwtichPort())) {
							device.setVifNumber(port.getName());
							break;
						}
					}
				}
			}
		}
	}

	private static void getDeviceFromXenServer() {
		for (Device device : devices.values()) {
			if (device.getVifNumber() != null) {
				List<String> uuids = XenTools.getUuids(device.getVifNumber());
				if (uuids.size() == 2) {
					device.setVmUuid(uuids.get(0));
					device.setVifUuid(uuids.get(1));
				}
			}
		}
	}

	public static Map<String, Device> getDevices(boolean update)
			throws JSONException {
		if (update) {
			getDeviceFromController();
			getDeviceFromXenServer();
		}
		return devices;
	}
}
