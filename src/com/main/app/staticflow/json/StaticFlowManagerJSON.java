package com.main.app.staticflow.json;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.basic.elements.Flow;
import com.main.provider.DataProvider;
import com.tools.util.Deserializer;
import com.tools.util.JSONArray;
import com.tools.util.JSONException;
import com.tools.util.JSONObject;

public class StaticFlowManagerJSON {

	static String IP = DataProvider.getIP();
	static JSONObject jsonobj, obj;
	static JSONArray json;
	static Future<Object> future;

	// This parses JSON from the restAPI to get all the flows from a switch
	public static Map<String, Flow> getFlows(String sw) throws IOException,
			JSONException {

		Map<String, Flow> flows = new ConcurrentHashMap<String, Flow>();

		// Get the string names of all the specified switch's flows
		future = Deserializer.readJsonObjectFromURL("http://" + IP
				+ ":8080/wm/staticflowpusher/list/" + sw + "/json");
		try {
			jsonobj = (JSONObject) future.get(5, TimeUnit.SECONDS);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (TimeoutException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(jsonobj == null){
			return flows;
		}
		if (jsonobj.length() != 0) {
			json = jsonobj.getJSONArray(sw);

			for (int i = 0; i < json.length(); i++) {
				jsonobj = json.getJSONObject(i);

				// Get the keys for the JSON Object
				Iterator<?> myIter = jsonobj.keys();
				// If a key exists, get the JSON Object for that key and create
				// a flow using that object
				while (myIter.hasNext()) {
					try {
						String key = (String) myIter.next();
						if (jsonobj.has(key)) {
							if (jsonobj.get(key) instanceof JSONObject) {
								obj = (JSONObject) jsonobj.get(key);
								Flow flow = new Flow();
								flow.setSwitch(sw);
								flow.setName(key);
								// Get the actions
								flow.setActions(ActionManagerJSON.getActions(
										sw, key));
								// Get the match
								flow.setMatch(MatchManagerJSON
										.getMatch(sw, key));
								flow.setPriority(String.valueOf(obj
										.getInt("priority")));
								flows.put(flow.getName(),flow);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return flows;
	}
}
