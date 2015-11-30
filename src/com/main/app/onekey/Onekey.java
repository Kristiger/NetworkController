package com.main.app.onekey;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.basic.elements.Action;
import com.basic.elements.Device;
import com.basic.elements.Flow;
import com.basic.elements.Match;
import com.main.app.qos.QosPolicy;
import com.main.provider.DataProvider;
import com.util.device.DeviceUtil;

public class Onekey {
	private static String qosUuid;

	public Onekey(){
		getQosUuid();
	}


	private void getQosUuid() {
		// TODO 自动生成的方法存根
		Map<String, QosPolicy> qoses = DataProvider.getQoses();
		if(qoses.size() > 0){
			for (QosPolicy qos : qoses.values()) {
				qosUuid = qos.getUuid();
				return;
			}
		}
	}


	public static void setVifPortQos(String mac) {
		// TODO 自动生成的方法存根
		DeviceUtil.addQosForDevice(mac, qosUuid);
	}


	public static void addFlowToSwitch(Device device) {
		// TODO 自动生成的方法存根
		Flow flow1 = new Flow();
		Match match = new Match();

		flow1.setSwitch(device.getSwitchDpid());
		flow1.setName("RDP");
		flow1.setPriority("3");
		
		match.setDataLayerType("0x0800");
		match.setNetworkProtocol("0x6");
		match.setNetworkDestination(device.getIpAddr());
		match.setTransportDestination("3389");
		
		List<Action> actions = new ArrayList<Action>();
		Action action = new Action("enqueue", device.getSwitchPort() + ":2");
		actions.add(action);
		
		flow1.setMatch(match);
		flow1.setActions(actions);
		
		
		Flow flow2 = new Flow();
		Match match2 = new Match();
		
		flow2.setSwitch(device.getSwitchDpid());
		flow2.setName("BT");
		flow2.setPriority("2");
		
		match2.setDataLayerType("0x0800");
		match2.setNetworkProtocol("0x11");
		match2.setNetworkDestination(device.getIpAddr());
		match2.setTransportDestination("15000");
		
		List<Action> actions2 = new ArrayList<Action>();
		Action action2 = new Action("enqueue", device.getSwitchPort() + ":3");
		actions2.add(action2);
		
		flow2.setMatch(match2);
		flow2.setActions(actions2);
		
		Flow flow3 = new Flow();
		Match match3 = new Match();
		
		flow3.setSwitch(device.getSwitchDpid());
		flow3.setName("TCP");
		flow3.setPriority("2");
		
		match3.setDataLayerType("0x0800");
		match3.setNetworkProtocol("0x6");
		match3.setNetworkDestination(device.getIpAddr());
		match3.setTransportSource("80");
		
		List<Action> actions3 = new ArrayList<Action>();
		Action action3 = new Action("enqueue", device.getSwitchPort() + ":0");
		actions3.add(action3);
		
		flow3.setMatch(match3);
		flow3.setActions(actions3);
		
		Flow flow4 = new Flow();
		Match match4 = new Match();
		
		flow4.setSwitch(device.getSwitchDpid());
		flow4.setName("UDP");
		flow4.setPriority("2");
		
		match4.setDataLayerType("0x0800");
		match4.setNetworkProtocol("0x11");
		match4.setNetworkDestination(device.getIpAddr());
		
		List<Action> actions4 = new ArrayList<Action>();
		Action action4 = new Action("enqueue", device.getSwitchPort() + ":1");
		actions4.add(action4);
		
		flow4.setMatch(match4);
		flow4.setActions(actions4);
	}
}
