package com.main.app.staticflow.json;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.basic.elements.Match;
import com.main.provider.DataProvider;
import com.tools.util.Deserializer;
import com.tools.util.JSONArray;
import com.tools.util.JSONException;
import com.tools.util.JSONObject;

public class MatchManagerJSON {

	String dataLayerDestination, dataLayerSource, dataLayerType, dataLayerVLAN,
			dataLayerPCP, inputPort, networkDestination,
			networkDestinationMaskLength, networkProtocol, networkSource,
			networkSourceMaskLength, networkTypeOfService,
			transportDestination, transportSource, wildcards;

	private static String IP = DataProvider.getIP();
	private static String PORT = DataProvider.getPORT();
	private static JSONObject obj, jsonobj;
	private static JSONArray json;
	private static Future<Object> future;

	// This parses JSON from the restAPI to get the match of a flow and all it's
	// values
	public static Match getMatch(String dpid, String flowName)
			throws JSONException, IOException {
		Match match = new Match();
		// Get the match object
		future = Deserializer.readJsonObjectFromURL("http://" + IP
				+ ":" + PORT + "/wm/staticflowpusher/list/" + dpid + "/json");
		try {
			obj = (JSONObject) future.get(5, TimeUnit.SECONDS);
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
		json = obj.getJSONArray(dpid);
		
		for(int i = 0; i < json.length(); i++){
			if(!json.getJSONObject(i).has(flowName))
				continue;
			obj = json.getJSONObject(i).getJSONObject(flowName).getJSONObject("match");
			
			// Here we check the values, if they are default we set them to emptry strings.
			// This way they don't confuse the user into thinking they set something
			// they didn't		
					
			if(obj.length() == 0){
				return match;
			}
			if(obj.has("eth_dst"))
				if (!obj.getString("eth_dst").equals("00:00:00:00:00:00"))
					match.setDataLayerDestination(obj.getString("eth_dst"));
			if(obj.has("eth_src"))
				if (!obj.getString("eth_src").equals("00:00:00:00:00:00"))
					match.setDataLayerSource(obj.getString("eth_src"));
			if(obj.has("eth_type")){
				String eth_type = obj.getString("eth_type");
				eth_type = eth_type.replaceAll("0x", "");
				eth_type = "0x" + eth_type;
				if (!eth_type.equals("0x0000"))
					match.setDataLayerType(eth_type);
			}
			if(obj.has("eth_vlan_vid"))
				if (obj.getInt("eth_vlan_vid") > 0)
					match.setDataLayerVLAN(String.valueOf(obj
							.getInt("eth_vlan_vid")));
			if(obj.has("eth_vlan_pcp"))
				if (obj.getInt("eth_vlan_pcp") != 0)
					match.setDataLayerPCP(String.valueOf(obj
							.getInt("eth_vlan_pcp")));
			if(obj.has("in_port"))
				if (obj.getInt("in_port") != 0)
					match.setInputPort(String.valueOf(obj.getInt("in_port")));
			if(obj.has("ipv4_dst"))
				if (!obj.getString("ipv4_dst").equals("0.0.0.0"))
					match.setNetworkDestination(obj.getString("ipv4_dst"));
			if(obj.has("ip_proto"))
					match.setNetworkProtocol(obj.getString("ip_proto"));
			if(obj.has("ipv4_src"))
				if (!obj.getString("ipv4_src").equals("0.0.0.0"))
					match.setNetworkSource(obj.getString("ipv4_src"));
			if(obj.has("ip_tos"))
				if (obj.getInt("ip_tos") != 0)
					match.setNetworkTypeOfService(String.valueOf(obj
							.getInt("ip_tos")));
			if(obj.has("tp_dst"))
				if (obj.getInt("tp_dst") != 0)
					match.setTransportDestination(String.valueOf(obj
							.getInt("tp_dst")));
			if(obj.has("tp_src"))
				if (obj.getInt("tp_src") != 0)
					match.setTransportSource(String.valueOf(obj
							.getInt("tp_src")));
			if(obj.has("tcp_dst"))
				if (obj.getInt("tcp_dst") != 0)
					match.setTcpDestination(String.valueOf(obj
							.getInt("tcp_dst")));
			if(obj.has("tcp_src"))
				if (obj.getInt("tcp_src") != 0)
					match.setTcpSource(String.valueOf(obj
							.getInt("tcp_src")));
			if(obj.has("udp_dst"))
				if (obj.getInt("udp_dst") != 0)
					match.setUdpDestination(String.valueOf(obj
							.getInt("udp_dst")));
			if(obj.has("udp_src"))
				if (obj.getInt("udp_src") != 0)
					match.setUdpSource(String.valueOf(obj
							.getInt("udp_src")));
			
		}
		return match;
	}
}
