package com.tools.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.basic.elements.Device;
import com.tools.util.JSONException;
import com.util.device.DevicesJSON;

public class DeviceToTable {

	public static String[][] deviceSummariesToTable(Map<String, Device> devices) {

		String[][] tableArr = new String[devices.size()][6];
		int count = 0;

		for (Device dev : devices.values()) {
			List<String> stringList = new ArrayList<String>();
			stringList.add(String.valueOf(count + 1));
			if (dev.getIpAddr() != null) {
				stringList.add(dev.getIpAddr());
			} else {
				stringList.add("None");
			}

			stringList.add(dev.getMacAddr());

			if (dev.getSwitchDpid() != null) {
				stringList.add(dev.getSwitchDpid());
			} else {
				stringList.add("None");
			}

			if (dev.getSwitchPort() != null) {
				stringList.add(dev.getSwitchPort());
			} else {
				stringList.add("None");
			}

			// stringList.add(String.valueOf(dev.getLastSeen()));

			if (dev.getVifNumber() != null) {
				stringList.add(dev.getVifNumber());
			} else {
				stringList.add("None");
			}

			if (dev.getVmUuid() != null) {
				stringList.add(dev.getVmUuid());
			} else {
				stringList.add("None");
			}

			tableArr[count] = stringList.toArray(new String[stringList.size()]);
			count++;
		}

		return tableArr;
	}
}
