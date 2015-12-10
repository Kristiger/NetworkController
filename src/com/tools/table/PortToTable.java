package com.tools.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.basic.elements.Port;
import com.tools.util.FormatLong;

public class PortToTable {

	// This returns a table representation of a list of ports on a switch
	public static String[][] getPortTableFormat(Map<String, Port> ports) {

		if (!ports.isEmpty()) {
			int count = 0;
			String[][] arrData = new String[ports.size()][8];

			for (Port port : ports.values()) {
				List<String> stringList = new ArrayList<String>();
				stringList.add(count + 1 + "");
				stringList.add(port.getName());
				stringList.add(port.getPortNumber());
				stringList.add(FormatLong.formatPackets(Long.valueOf(port.getTransmitPackets())));
				stringList.add(FormatLong.formatBytes(Long.valueOf(port.getTransmitBytes())));
				stringList.add(FormatLong.formatPackets(Long.valueOf(port.getReceivePackets())));
				stringList.add(FormatLong.formatBytes(Long.valueOf(port.getReceiveBytes())));
				stringList.add(String.valueOf(FormatLong.formatBytes(port.getPortDownloadRate()) + "/s" + " | "
						+ String.valueOf(FormatLong.formatBytes(port.getPortUploadRate())) + "/s"));

				arrData[count] = stringList.toArray(new String[stringList.size()]);
				count++;
			}
			return arrData;
		} else
			return new String[0][0];
	}
}