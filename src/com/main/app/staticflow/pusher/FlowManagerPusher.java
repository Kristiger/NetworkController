package com.main.app.staticflow.pusher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import com.basic.elements.Flow;
import com.main.provider.DataProvider;
import com.tools.util.JSONException;
import com.tools.util.JSONObject;

public class FlowManagerPusher {

	static String IP = DataProvider.getIP();
	static String PORT = DataProvider.getPORT();

	public static String push(Flow flow) throws IOException, JSONException {

		String warning = "Warning! Pushing a static flow entry that matches IP "
				+ "fields without matching for IP payload (ether-type 2048) will cause "
				+ "the switch to wildcard higher level fields.";

		String jsonResponse = "";
		URL url = new URL("http://" + IP + ":" + PORT
				+ "/wm/staticflowpusher/json");
		URLConnection conn = url.openConnection();
		conn.setDoOutput(true);
		OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		System.out.println("send : " + flow.serialize());
		wr.write(flow.serialize());
		wr.flush();

		// Get the response
		BufferedReader rd = new BufferedReader(new InputStreamReader(
				conn.getInputStream()));
		String line;
		while ((line = rd.readLine()) != null) {
			jsonResponse = jsonResponse.concat(line);
		}
		wr.close();
		rd.close();

		// Wrap the response
		JSONObject json = new JSONObject(jsonResponse);

		// Make sure the static flow pusher throws no errors
		if (json.getString("status").equals("Entry pushed")
				|| json.getString("status").equals(warning)) {
			// Get actual flows, we pass null as first parameter
			// to denote that we are not supplying a JSON object
			/*List<Flow> actualFlows = DataProvider.getRealFlows(
					flow.getSwitch());*/
			Map<String, Flow> actualFlows = DataProvider.getStaticFlows(flow.getSwitch(), true);
			// Compare the flow you just pushed with those actually on the
			// switch
			// If found, success message printed.
			System.out.println("Push : " + flow.serialize());
			for (Flow actualFlow : actualFlows.values()) {
				System.out.println("recv : " + actualFlow.serialize());
				if (flow.equals(actualFlow)) {
					return "Flow successfully pushed down to switches";
				}
			}
			// If not found give user guidance
			return "Flow pushed but not recognized on any switches. "
					+ "It may have been dropped by the switch of modified "
					+ "by the static flow pusher on the controller."
					+ " Check your controller log for more details.";
		} else {
			return json.getString("status");
		}
	}

	public static String remove(Flow flow) throws IOException, JSONException {

		String jsonResponse = "";

		URL url = new URL("http://" + IP + ":8080/wm/staticflowpusher/json");
		HttpURLConnection connection = null;
		connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		// We have to override the post method so we can send data
		connection.setRequestProperty("X-HTTP-Method-Override", "DELETE");
		connection.setDoOutput(true);

		// Send request
		OutputStreamWriter wr = new OutputStreamWriter(
				connection.getOutputStream());
		wr.write(flow.deleteString());
		wr.flush();

		// Get Response
		BufferedReader rd = new BufferedReader(new InputStreamReader(
				connection.getInputStream()));
		String line;
		while ((line = rd.readLine()) != null) {
			jsonResponse = jsonResponse.concat(line);
		}
		wr.close();
		rd.close();

		JSONObject json = new JSONObject(jsonResponse);
		// Return result string from key "status"
		return json.getString("status");
	}

	public static void deleteAll(String dpid) throws IOException, JSONException {
		// This makes a simple get request that will delete all flows from a
		// switch
		String urlString = "http://" + IP + ":8080/wm/staticflowpusher/clear/"
				+ dpid + "/json";
		URL url = new URL(urlString);
		URLConnection conn = url.openConnection();
		@SuppressWarnings("unused")
		InputStream is = conn.getInputStream();
	}

	// Checks the entries for valid values
	public static boolean errorChecksPassed(String p) {
		return true;
		/*if (!p.equals("") && !ErrorCheck.isNumeric(p)) {
			DisplayMessage.displayError(StaticFlowManager.getShell(),
					"Priority must be a valid number.");
			return false;
		} else
			return true;*/
	}
}
