package com.tools.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.basic.elements.Flow;
import com.tools.util.FormatLong;

public class FlowToTable {

	// This returns a table representation of a flow, formatted for the
	// controller overview
	public static String[][] getFlowTableFormat(List<Flow> flows) {

		if (flows != null) {
			Collections.sort(flows);
			int count = 0;
			String[][] arrData = new String[flows.size()][8];

			for (Flow flow : flows) {
				List<String> stringList = new ArrayList<String>();
				stringList.add(String.valueOf(count + 1));
				stringList.add(flow.getPriority());
				stringList.add(flow.getMatch().toString());
				stringList.add(flow.actionsToString());
				stringList.add(FormatLong.formatPackets(Long.valueOf(flow
						.getPacketCount())));
				stringList.add(FormatLong.formatPackets(Long.valueOf(flow
						.getByteCount())));
				stringList.add(flow.getDurationSeconds());

				if (flow.getIdleTimeOut() != null) {
					stringList.add(flow.getIdleTimeOut());
				} else {
					stringList.add("Static");
				}
				arrData[count] = stringList.toArray(new String[stringList
						.size()]);
				count++;
			}
			return arrData;
		} else
			return new String[0][0];
	}
}