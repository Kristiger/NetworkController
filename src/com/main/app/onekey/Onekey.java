package com.main.app.onekey;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.basic.elements.Action;
import com.basic.elements.Device;
import com.basic.elements.Flow;
import com.basic.elements.Match;
import com.basic.elements.UPDATETYPE;
import com.main.app.qos.QosPolicy;
import com.main.app.staticflow.pusher.FlowManagerPusher;
import com.main.provider.DataProvider;
import com.tools.util.JSONException;
import com.util.xen.XenTools;

public class Onekey {
	private static final int RDPQUEUEID = 7;
	private static final int TCPQUEUEID = 2;
	private static final int UDPQUEUEID = 3;

	private static final int RDPPIORITY = 20;
	private static final int TCPPIORITY = 18;
	private static final int UDPPIORITY = 18;

	private static final long UPLOADRATE = 10000000;
	private static final long UPLOADBURST = 100;

	public static void setdefault(Device device) {
		setDefaultQos(device);
		setDefaultFlows(device);
	}

	private static void setDefaultQos(Device device) {
		// TODO Auto-generated method stub

		// set upload
		// XenTools.setUploadRate(device.getVifNumber(), UPLOADRATE,   //cause sound not fluent
		// UPLOADBURST);

		// set download qos
		Map<String, QosPolicy> qoses = DataProvider.getQoses();
		if (qoses.size() > 0) {
			for (QosPolicy qos : qoses.values()) {
				String qosUuid = qos.getUuid();
				DataProvider.updateDeviceStore(device, UPDATETYPE.BAND, null, null);
				XenTools.setPortQos(device.getSwitchPort(), qosUuid);
				device.setQosUuid(qosUuid);
				return;
			}
		}
	}

	private static void setDefaultFlows(Device device) {
		// TODO Auto-generated method stub

		// rdp flow
		Flow flowrdp = new Flow();
		flowrdp.setSwitch(device.getSwitchDpid());
		flowrdp.setName("flowrdp" + device.getSwitchPort());
		flowrdp.setPriority(String.valueOf(RDPPIORITY));

		Match matchrdp = new Match();
		matchrdp.setDataLayerType("0x0800");
		matchrdp.setNetworkProtocol("0x6");
		matchrdp.setNetworkDestination(device.getIpAddr());
		matchrdp.setTransportDestination("3389");

		List<Action> actionsrdp = new ArrayList<Action>();
		Action actionrdp = new Action("enqueue", device.getSwitchPort() + ":" + RDPQUEUEID);
		actionsrdp.add(actionrdp);

		flowrdp.setMatch(matchrdp);
		flowrdp.setActions(actionsrdp);

		// tcp flow
		Flow flowtcp = new Flow();
		flowtcp.setSwitch(device.getSwitchDpid());
		flowtcp.setName("flowtcp" + device.getSwitchPort());
		flowtcp.setPriority(String.valueOf(TCPPIORITY));

		Match matchtcp = new Match();
		matchtcp.setDataLayerType("0x0800");
		matchtcp.setNetworkProtocol("0x6");
		matchtcp.setNetworkDestination(device.getIpAddr());

		List<Action> actionstcp = new ArrayList<Action>();
		Action actiontcp = new Action("enqueue", device.getSwitchPort() + ":" + TCPQUEUEID);
		actionstcp.add(actiontcp);

		flowtcp.setMatch(matchtcp);
		flowtcp.setActions(actionstcp);

		// udp flow
		Flow flowudp = new Flow();
		flowudp.setSwitch(device.getSwitchDpid());
		flowudp.setName("flowudp" + device.getSwitchPort());
		flowudp.setPriority(String.valueOf(UDPPIORITY));

		Match matchudp = new Match();
		matchudp.setDataLayerType("0x0800");
		matchudp.setNetworkProtocol("0x11");
		matchudp.setNetworkDestination(device.getIpAddr());

		List<Action> actionsudp = new ArrayList<Action>();
		Action actionudp = new Action("enqueue", device.getSwitchPort() + ":" + UDPQUEUEID);
		actionsudp.add(actionudp);

		flowudp.setMatch(matchudp);
		flowudp.setActions(actionsudp);

		// push
		try {
			System.out.println(FlowManagerPusher.push(flowrdp));
			System.out.println(FlowManagerPusher.push(flowtcp));
			System.out.println(FlowManagerPusher.push(flowudp));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
