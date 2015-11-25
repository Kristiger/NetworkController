package com.util.device;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.basic.elements.Device;
import com.main.provider.DataProvider;
import com.tools.util.Deserializer;
import com.tools.util.JSONArray;
import com.tools.util.JSONException;
import com.tools.util.JSONObject;

public class DevicesJSON {

	private static String IP = DataProvider.getIP();
	private static String PORT = DataProvider.getPORT();
	private static JSONObject obj;
	private static JSONArray jsonip;

	/**
	 * Get the string IDs of all the switches and create switch summary objects
	 * for each one
	 * 
	 * @return devices
	 * @throws JSONException
	 */
	public static Map<String, Device> getDeviceSummaries() throws JSONException {

		Map<String, Device> devs = new ConcurrentHashMap<String, Device>();

		try {
			Future<Object> devices = Deserializer
					.readJsonArrayFromURL("http://" + IP + ":" + PORT
							+ "/wm/device/");
			JSONArray json = (JSONArray) devices.get(5, TimeUnit.SECONDS);
			
			if(json == null){
				return devs;
			}
			for (int i = 0; i < json.length(); i++) {
				obj = json.getJSONObject(i);

				// set port >= 1 to remove hosts not in xen server
				if (obj.getJSONArray("ipv4").isNull(0)
						|| obj.getJSONArray("attachmentPoint").isNull(0)
						|| obj.getJSONArray("attachmentPoint").getJSONObject(0)
								.getInt("port") < 1) {
					continue;
				}

				String mac = obj.getJSONArray("mac").getString(0);
				Device temp;
				if (devs.containsKey(mac)) {
					temp = devs.get(mac);
				} else {
					temp = new Device(mac);
					devs.put(temp.getMacAddr(), temp);
				}

				jsonip = obj.getJSONArray("ipv4");
				String ip = jsonip.getString(0);

				for (int j = 1; j < jsonip.length(); j++) {
					ip = ip + "," + jsonip.getString(j);
				}
				temp.setIpAddr(ip);

				temp.setSwitchDpid(obj.getJSONArray("attachmentPoint")
						.getJSONObject(0).getString("switchDPID"));
				temp.setSwitchPort(String.valueOf(obj
						.getJSONArray("attachmentPoint").getJSONObject(0)
						.getInt("port")));
				if (obj.has("lastSeen")) {
					Date d = new Date(obj.getLong("lastSeen"));
					temp.setLastSeen(d);
				}
			}
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
		return devs;
	}
}
