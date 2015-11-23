package com.tools.table;

import java.util.ArrayList;
import java.util.List;

import com.basic.elements.Port;
import com.tools.util.FormatLong;

public class PortToTable {

	// This returns a table representation of a list of ports on a switch
	public static String[][] getPortTableFormat(List<Port> ports) {

		if (!ports.isEmpty()) {
			int count = 0;
			String[][] arrData = new String[ports.size()][8];

			for (Port port : ports) {
				List<String> stringList = new ArrayList<String>();
				stringList.add(count + 1 + "");
				stringList.add(port.getName());
				stringList.add(port.getPortNumber());
				stringList.add(FormatLong.formatPackets(Long.valueOf(port
						.getTransmitPackets())));
				stringList.add(FormatLong.formatPackets(Long.valueOf(port
						.getTransmitBytes())));
				stringList.add(FormatLong.formatPackets(Long.valueOf(port
						.getReceivePackets())));
				stringList.add(FormatLong.formatPackets(Long.valueOf(port
						.getReceiveBytes())));
				stringList.add(String.valueOf(Integer.valueOf(port
						.getTransmitDropped())
						+ Integer.valueOf(port.getReceiveDropped())));

				stringList.add(port.getErrors());
				arrData[count] = stringList.toArray(new String[stringList
						.size()]);
				count++;
			}
			return arrData;
		} else
			return new String[0][0];
	}
}