package com.util.device;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.basic.elements.Device;
import com.basic.elements.Port;
import com.basic.elements.Switch;
import com.main.provider.DataProvider;
import com.tools.util.JSONException;
import com.util.db.DBHelper;
import com.util.xen.XenTools;

public class DeviceUtil {
	private static Map<String, Device> devices = new ConcurrentHashMap<String, Device>();
	private static DBHelper db = null;

	public static void addDevice(Device device) {
		// newly get from sw
		if (!device.isActive()) {
			db = new DBHelper();
			String sql = "INSERT INTO `mydatabase`.`device` "
					+ "(`id`, `vmUuid`, `vifUuid`, `vifNumber`, `switchPort`, `ipAddr`, `macAddr`)"
					+ " VALUES (NULL, \'" + device.getVmUuid() + "\'," + " \'" + device.getVifUuid() + "\', " + "\'"
					+ device.getVifNumber() + "\', " + "\'" + device.getSwitchPort() + "\', " + "\'"
					+ device.getIpAddr() + "\', " + "\'" + device.getMacAddr() + "\');";
			try {
				if (db.executeUpdate(sql) == 0) {
					throw new Exception("Insert device, 0 returned.");
				}

				// means it has update to db
				device.setActive(true);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void addQosForDevice(String mac, String qosUuid) {
		// TODO Auto-generated method stub
		if (devices.containsKey(mac)) {
			devices.get(mac).setQosUuid(qosUuid);

			db = new DBHelper();
			String sql = "SELECT * FROM `devicetoqos` WHERE `macAddr`=\'" + mac + "\'";
			ResultSet result;
			try {
				// see if there is a qos to device exist, if yes, remove it.
				result = db.executeQuery(sql);
				if (result.getRow() != 0) {
					removeQosForDevice(result.getString("macAddr"), result.getString("qosUuid"));
				}

				// insert qos
				sql = "INSERT INTO `mydatabase`.`devicetoqos` (`id`, `macAddr`, `qosUuid`) VALUES (NULL, \'" + mac
						+ "\', \'" + qosUuid + "\');";
				if (db.executeUpdate(sql) == 0) {
					throw new Exception("Insert qos for device, 0 returned.");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				db.close();
			}
		}
	}

	public static Device getDevice(String mac) {
		return devices.get(mac);
	}

	private static void getDeviceFromController() throws JSONException {
		DevicesJSON.getDeviceSummaries(devices);

		for(Device dev : devices.values()){
			if (dev.getSwitchDpid() != null) {
				Switch sw = DataProvider.getSwitch(dev.getSwitchDpid());
				if (sw != null) {
					Map<String, Port> ports = sw.getPorts();
					if (ports.containsKey(dev.getSwitchPort())) {
						dev.setVifNumber(ports.get(dev.getSwitchPort()).getName());
						if (dev.isActive() == false) {
							addDevice(dev);
						}
					}
				}
			}
		}
	}

	private static void getDeviceFromXenServer() {
		for (Device device : devices.values()) {
			if (device.getVifNumber() != null) {
				List<String> uuids = XenTools.getUuids(device.getVifNumber());
				if (uuids.size() == 2) {
					device.setVmUuid(uuids.get(0));
					device.setVifUuid(uuids.get(1));
				}
			}
		}
	}

	public static Map<String, Device> getDevices() throws JSONException {
		getDeviceFromController();
		// getDeviceFromXenServer();
		return devices;
	}

	public static Map<String, Device> getDevicesFromDB() {
		// return all
		db = new DBHelper();
		String sql = "SELECT * FROM `device` LIMIT 0, 30 ";
		try {
			ResultSet result = db.executeQuery(sql);
			while (result.next()) {
				Device device = new Device();
				device.setVmUuid(result.getString("vmUuid"));
				device.setVifUuid(result.getString("vifUuid"));
				device.setVifNumber(result.getString("vifNumber"));
				device.setSwitchPort(result.getString("switchPort"));
				device.setIpAddr(result.getString("ipAddr"));
				device.setMacAddr(result.getString("macAddr"));
				device.setActive(true);
				devices.put(device.getMacAddr(), device);
			}
			result.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			db.close();
		}
		return devices;
	}

	public static String getQosForDevice(String mac) {
		String sql = "SELECT * FROM `devicetoqos` LIMIT 0, 30 ";
		db = new DBHelper();
		try {
			ResultSet result = db.executeQuery(sql);
			if (result.next()) {
				return result.getString("qosUuid");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void removeDevice(String mac) {
		if (devices.containsKey(mac)) {
			devices.remove(mac);

			db = new DBHelper();
			String sql = "DELETE FROM `device` WHERE `macAddr`=\'" + mac + "\'";
			try {
				if (db.executeUpdate(sql) == 0) {
					throw new Exception("Remove device, 0 returned.");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				db.close();
			}
		}
	}

	public static void removeQosForDevice(String mac, String qosUuid) {
		// TODO Auto-generated method stub
		if (devices.containsKey(mac)) {
			devices.get(mac).setQosUuid(null);

			DBHelper db1 = new DBHelper();
			String sql = "DELETE FROM `devicetoqos` WHERE `qosUuid`=\'" + qosUuid + "\' AND `macAddr`=\'" + mac + "\'";
			try {
				if (db.executeUpdate(sql) == 0) {
					throw new Exception("Remove qos for device, 0 returned.");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				db1.close();
			}
		}
	}

	public static void updateDevice(String mac, String key, Object value) {
		// TODO Auto-generated method stub
		if (devices.containsKey(mac)) {
			db = new DBHelper();
			String sql;
			switch (key) {
			case "uploadRate":
				// deal with long value
				devices.get(mac).setUploadRate(((Long) value).longValue());

				sql = "UPDATE `mydatabase`.`device` SET `" + key + "` = " + ((Long) value).longValue()
						+ " WHERE `device`.`macAddr` = \'" + mac + "\';";
				try {
					db.executeUpdate(sql);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} finally {
					db.close();
				}
				break;
			case "vifNumber":
				devices.get(mac).setVifNumber(value.toString());
			case "switchPort":
				devices.get(mac).setSwitchPort(value.toString());
			case "ipAddr":
				devices.get(mac).setIpAddr(value.toString());
				sql = "UPDATE `mydatabase`.`device` SET `" + key + "` = \'" + value.toString()
						+ "\' WHERE `device`.`macAddr` = \'" + mac + "\';";
				try {
					db.executeUpdate(sql);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					db.close();
				}
				break;
			}
		}
	}
}
