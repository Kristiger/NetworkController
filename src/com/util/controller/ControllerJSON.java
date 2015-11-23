package com.util.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.main.provider.DataProvider;
import com.tools.util.Deserializer;
import com.tools.util.FormatLong;
import com.tools.util.JSONException;
import com.tools.util.JSONObject;

/**
 * 
 * Get Controller Health,Modules,Memory By RestAPI
 * 
 */
public class ControllerJSON {

	private static String IP = DataProvider.getIP();
	private static String PORT = DataProvider.getPORT();
	private static Future<Object> futureHealth, futureModules, futureMemory;
	private static JSONObject obj;

	public static Map<String, String> getControllerInfo() throws JSONException {

		Map<String, String> info = new HashMap<String, String>();

		// Add the ip address of the controller
		info.put("IP", IP);
		info.put("PORT", PORT);

		// Start threads that make calls to the restAPI
		futureHealth = Deserializer.readJsonObjectFromURL("http://" + IP + ":"
				+ PORT + "/wm/core/health/json");
		futureMemory = Deserializer.readJsonObjectFromURL("http://" + IP + ":"
				+ PORT + "/wm/core/memory/json");
		futureModules = Deserializer.readJsonObjectFromURL("http://" + IP + ":"
				+ PORT + "/wm/core/module/loaded/json");

		// HEALTH
		try {
			obj = (JSONObject) futureHealth.get(5, TimeUnit.SECONDS);
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

		if (obj == null)
			return info;
		if (obj.getBoolean("healthy")) {
			info.put("health", "Yes");
		} else {
			info.put("health", "No");
		}

		// MEMORY
		try {
			obj = (JSONObject) futureMemory.get(5, TimeUnit.SECONDS);
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
		if (obj == null) {
			return info;
		} else if (obj.has("free")) {
			long free = obj.getLong("free");
			if (obj.has("total")) {
				long total = obj.getLong("total");
				info.put("memory", FormatLong.formatBytes(free) + " / "
						+ FormatLong.formatBytes(total));
			}
		}

		// MODULES
		try {
			obj = (JSONObject) futureModules.get(5, TimeUnit.SECONDS);
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
		if (obj == null) {
			return info;
		}
		Iterator<?> myIter = obj.keys();
		String modules = "";
		while (myIter.hasNext()) {
			try {
				String key = (String) myIter.next();
				if (obj.get(key) instanceof JSONObject) {
					modules = modules.concat(key + "\t");
				}
			} catch (Exception e) {
				// Fail silently
			}
		}
		info.put("modules", modules);

		return info;
	}
}
