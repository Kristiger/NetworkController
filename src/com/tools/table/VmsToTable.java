package com.tools.table;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.basic.elements.Device;

public class VmsToTable {
	public static String[][] vmdatasToTable(List<Device> vms) {
		String[][] tableArr = new String[vms.size()][8];
		int count = 0;
		Iterator<Device> it = vms.iterator();
		Device device = null;
		while (it.hasNext()) {
			device = it.next();
			List<String> stringList = new ArrayList<String>();
			stringList.add(String.valueOf(count + 1));
			stringList.add(device.getIpAddr() != null ? device.getIpAddr() : "None");
			stringList.add(device.getMacAddr() != null ? device.getMacAddr() : "None");
			stringList.add(device.getSwitchDpid() != null ? device.getSwitchDpid() : "None");
			stringList.add(device.getSwitchPort() != null ? device.getSwitchPort() : "None");
			stringList.add(device.getLastSeen() != null ? device.getLastSeen().toString() : "None");
			stringList.add(device.getVifNumber() != null ? device.getVifNumber() : "None");
			stringList.add(device.getVmUuid() != null ? device.getVmUuid() : "None");
			tableArr[count] = stringList.toArray(new String[stringList.size()]);
			count ++;
		}
		
		return tableArr;
	}
}
