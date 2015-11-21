package com.util.controller;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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

	public static List<String> getControllerInfo() throws JSONException,
			IOException {

		List<String> info = new ArrayList<String>();

		// Add the ip address of the controller
		info.add(0, IP);
		

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
			info.add(1, "Yes");
		} else {
			info.add(1, "No");
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
		long free = obj.getLong("free");
		long total = obj.getLong("total");
		info.add(2, FormatLong.formatBytes(free, true, false) + " free of "
				+ FormatLong.formatBytes(total, true, false));

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
		Iterator<?> myIter = obj.keys();
		String modules = "";
		while (myIter.hasNext()) {
			try {
				String key = (String) myIter.next();
				if (obj.get(key) instanceof JSONObject) {
					modules = modules.concat(key + "\t\t");
				}
			} catch (Exception e) {
				// Fail silently
			}
		}
		info.add(modules);

		return info;
	}
}
