package com.util.sw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.basic.elements.Port;
import com.basic.elements.Switch;
import com.main.provider.DataProvider;
import com.tools.util.Deserializer;
import com.tools.util.JSONArray;
import com.tools.util.JSONException;
import com.tools.util.JSONObject;

public class SwitchesJSON {

	static String IP = DataProvider.getIP();
	static Map<String, Switch> switches = new ConcurrentHashMap<String, Switch>();
	static Map<String, Switch> old_switches = null;
	static List<String> switchDpids = null;
	static JSONObject obj;
	static JSONArray json;

	// This parses JSON from the restAPI to get all the switches connected to
	// the controller
	@SuppressWarnings("unchecked")
	public static Map<String, Switch> getSwitches() throws JSONException {
		Map<String, Future<Object>> futureStats = new HashMap<String, Future<Object>>();
		switchDpids = getSwitchDpids();

		for (String dpid : switchDpids)
			futureStats.put(dpid, SwitchJSON.startSwitchRestCalls(dpid, false));

		for (String dpid : futureStats.keySet()) {
			Switch sw = null;
			boolean switchExist = false;
			if (switches.containsKey(dpid)) {
				sw = switches.get(dpid);
				switchExist = true;
			} else {
				sw = new Switch(dpid);
				switches.put(dpid, sw);
			}

			Map<String, Port> ports = new ConcurrentHashMap<>();
			Map<String, Future<Object>> stats;
			JSONObject descriptionObj = null, aggregateObj = null, portObj = null, featuresObj = null;
			try {
				stats = (Map<String, Future<Object>>) futureStats.get(dpid).get(5L, TimeUnit.SECONDS);
				// Don't bother if we are updating this switch, since
				// description is static
				if (!switchExist)
					descriptionObj = (JSONObject) stats.get("description").get(5L, TimeUnit.SECONDS);
				aggregateObj = (JSONObject) stats.get("aggregate").get(5L, TimeUnit.SECONDS);
				portObj = (JSONObject) stats.get("port").get(5L, TimeUnit.SECONDS);
				featuresObj = (JSONObject) stats.get("features").get(5L, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Description stats
			if (!switchExist && descriptionObj != null) {
				descriptionObj = descriptionObj.getJSONObject("desc");
				sw.setManufacturerDescription(descriptionObj.getString("manufacturerDescription"));
				sw.setHardwareDescription(descriptionObj.getString("hardwareDescription"));
				sw.setSoftwareDescription(descriptionObj.getString("softwareDescription"));
				sw.setSerialNumber(descriptionObj.getString("serialNumber"));
				sw.setDatapathDescription(descriptionObj.getString("datapathDescription"));
			}

			// Aggregate stats, ignore
			if (aggregateObj != null) {
				aggregateObj = aggregateObj.getJSONObject("aggregate");
				sw.setPacketCount(aggregateObj.getString("packetCount"));
				sw.setByteCount(aggregateObj.getString("byteCount"));
				sw.setFlowCount(aggregateObj.getString("flowCount"));
			}

			// Port and Features stats
			if (portObj == null || !portObj.has("port_reply")
					|| !portObj.getJSONArray("port_reply").getJSONObject(0).has("port")) {
				continue;
			}

			JSONArray json = portObj.getJSONArray("port_reply").getJSONObject(0).getJSONArray("port");

			JSONArray jsontwo = new JSONArray();
			if (featuresObj.has("portDesc")) {
				jsontwo = featuresObj.getJSONArray("portDesc");
			}

			for (int i = 0; i < json.length(); i++) {
				obj = (JSONObject) json.get(i);
				if (!obj.has("portNumber")) {
					continue;
				}
				Port port = new Port(String.valueOf(obj.getString("portNumber")));

				port.setReceivePackets(String.valueOf(obj.getLong("receivePackets")));
				port.setTransmitPackets(String.valueOf(obj.getLong("transmitPackets")));
				port.setReceiveBytes(String.valueOf(obj.getLong("receiveBytes")));
				port.setTransmitBytes(String.valueOf(obj.getLong("transmitBytes")));
				port.setReceiveDropped(String.valueOf(obj.getLong("receiveDropped")));
				port.setTransmitDropped(String.valueOf(obj.getLong("transmitDropped")));
				port.setReceiveErrors(String.valueOf(obj.getLong("receiveErrors")));
				port.setTransmitErrors(String.valueOf(obj.getLong("transmitErrors")));
				port.setReceieveFrameErrors(String.valueOf(obj.getInt("receiveFrameErrors")));
				port.setReceieveOverrunErrors(String.valueOf(obj.getInt("receiveOverrunErrors")));
				port.setReceiveCRCErrors(String.valueOf(obj.getInt("receiveCRCErrors")));
				port.setCollisions(String.valueOf(obj.getInt("collisions")));

				if (!jsontwo.isNull(i)) {
					obj = (JSONObject) jsontwo.get(i);
					if (port.getPortNumber().equals(obj.get("portNumber"))) {
						port.setName(obj.getString("name"));
						port.setState(String.valueOf(obj.getInt("state")));
						port.setConfig(String.valueOf(obj.getInt("config")));
						port.setHardwareAddress(obj.getString("hardwareAddress"));
						port.setAdvertisedFeatures(String.valueOf(obj.getInt("advertisedFeatures")));
						port.setCurrentFeatures(String.valueOf(obj.getInt("currentFeatures")));
						port.setPeerFeatures(String.valueOf(obj.getInt("peerFeatures")));
						port.setSupportedFeatures(String.valueOf(obj.getInt("supportedFeatures")));
					}
				}

				
				ports.put(port.getPortNumber(), port);
			}
			sw.setPorts(ports);
		}
		old_switches = switches;
		return switches;
	}

	@SuppressWarnings("unchecked")
	public static void updateSwitch(Switch sw) throws JSONException {

		String dpid = sw.getDpid();
		Map<String, Port> ports = new ConcurrentHashMap<>();
		JSONObject obj, portObj = null, featuresObj = null;
		Map<String, Future<Object>> stats;
		// Start the rest calls, true is passed since we are updating and don't
		// care about the description
		Future<Object> futureStat = SwitchJSON.startSwitchRestCalls(dpid, true);

		try {
			stats = (Map<String, Future<Object>>) futureStat.get(5L, TimeUnit.SECONDS);
			portObj = (JSONObject) stats.get("port").get(5L, TimeUnit.SECONDS);
			featuresObj = (JSONObject) stats.get("features").get(5L, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Port and Features stats
		if (portObj == null || !portObj.has("port_reply")
				|| !portObj.getJSONArray("port_reply").getJSONObject(0).has("port")) {
			return;
		}
		JSONArray json = portObj.getJSONArray("port_reply").getJSONObject(0).getJSONArray("port");

		JSONArray jsontwo = new JSONArray();
		if (featuresObj.has("portDesc")) {
			jsontwo = featuresObj.getJSONArray("portDesc");
		}

		for (int i = 0; i < json.length(); i++) {
			obj = (JSONObject) json.get(i);
			if (!obj.has("portNumber")) {
				continue;
			}
			Port port = new Port(String.valueOf(obj.getString("portNumber")));

			port.setReceivePackets(String.valueOf(obj.getLong("receivePackets")));
			port.setTransmitPackets(String.valueOf(obj.getLong("transmitPackets")));
			port.setReceiveBytes(String.valueOf(obj.getLong("receiveBytes")));
			port.setTransmitBytes(String.valueOf(obj.getLong("transmitBytes")));
			port.setReceiveDropped(String.valueOf(obj.getLong("receiveDropped")));
			port.setTransmitDropped(String.valueOf(obj.getLong("transmitDropped")));
			port.setReceiveErrors(String.valueOf(obj.getLong("receiveErrors")));
			port.setTransmitErrors(String.valueOf(obj.getLong("transmitErrors")));
			port.setReceieveFrameErrors(String.valueOf(obj.getInt("receiveFrameErrors")));
			port.setReceieveOverrunErrors(String.valueOf(obj.getInt("receiveOverrunErrors")));
			port.setReceiveCRCErrors(String.valueOf(obj.getInt("receiveCRCErrors")));
			port.setCollisions(String.valueOf(obj.getInt("collisions")));

			if (!jsontwo.isNull(i)) {
				obj = (JSONObject) jsontwo.get(i);
				if (port.getPortNumber().equals(obj.get("portNumber"))) {
					port.setName(obj.getString("name"));
					port.setState(String.valueOf(obj.getInt("state")));
					port.setConfig(String.valueOf(obj.getInt("config")));
					port.setHardwareAddress(obj.getString("hardwareAddress"));
					port.setAdvertisedFeatures(String.valueOf(obj.getInt("advertisedFeatures")));
					port.setCurrentFeatures(String.valueOf(obj.getInt("currentFeatures")));
					port.setPeerFeatures(String.valueOf(obj.getInt("peerFeatures")));
					port.setSupportedFeatures(String.valueOf(obj.getInt("supportedFeatures")));
				}
			}
			
			if (old_switches != null) {
				if (old_switches.get(sw.getDpid()).getPorts().containsKey(port.getPortNumber())) {
					Port oldport = old_switches.get(sw.getDpid()).getPorts().get(port.getPortNumber());
					Long transmitbytes = Long.valueOf(port.getTransmitBytes())
							- Long.valueOf(oldport.getTransmitBytes());
					Long receivedbytes = Long.valueOf(port.getReceiveBytes())
							- Long.valueOf(oldport.getReceiveBytes());
					port.setPortUploadRate(transmitbytes);
					port.setPortDownloadRate(receivedbytes);
				}
			}
			ports.put(port.getPortNumber(), port);
		}
		sw.setPorts(ports);
	}

	public static List<String> getSwitchDpids() throws JSONException {
		List<String> switchDpids = new ArrayList<String>();

		Future<Object> futureSwDpids = Deserializer
				.readJsonArrayFromURL("http://" + IP + ":8080/wm/core/controller/switches/json");
		try {
			json = (JSONArray) futureSwDpids.get(5, TimeUnit.SECONDS);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (ExecutionException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (TimeoutException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		if (json == null) {
			return switchDpids;
		}
		for (int i = 0; i < json.length(); i++) {
			obj = json.getJSONObject(i);
			String dpid = obj.getString("switchDPID");
			switchDpids.add(dpid);
		}
		return switchDpids;
	}
}
