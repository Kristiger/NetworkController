package com.tools.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.basic.elements.Device;

public class DeviceToTable {

	public static String[][] deviceSummariesToTable(Map<String, Device> devices) {

		String[][] tableArr = new String[devices.size()][6];
		int count = 0;

		for (Device device : devices.values()) {
			List<String> stringList = new ArrayList<String>();
			stringList.add(String.valueOf(count + 1));
			
			stringList.add(device.getIpAddr() != null ? device.getIpAddr() : "None");
			stringList.add(device.getMacAddr());
			stringList.add(device.getSwitchDpid() != null ? device.getSwitchDpid() : "None");
			stringList.add(device.getSwitchPort() != null ? device.getSwitchPort() : "None");
			//stringList.add(device.getLastSeen() != null ? device.getLastSeen().toString() : "None");
			stringList.add(device.getVifNumber() != null ? device.getVifNumber() : "None");
			stringList.add(device.getVmUuid() != null ? device.getVmUuid() : "None");
			
			tableArr[count] = stringList.toArray(new String[stringList.size()]);
			count++;
		}

		return tableArr;
	}
}
