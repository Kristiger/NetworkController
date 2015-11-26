package com.util.device;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.basic.elements.Device;
import com.basic.elements.Port;
import com.basic.elements.Switch;
import com.main.app.qos.QosPolicy;
import com.main.provider.DataProvider;
import com.tools.util.JSONException;
import com.util.db.DBHelper;
import com.util.xen.XenTools;

public class DeviceUtil {
	private static Map<String, Device> devices = new ConcurrentHashMap<String, Device>();
	private static DBHelper db = null;

	public static void addDevice(Device device) {
		if (!devices.containsKey(device.getVmUuid())) {
			devices.put(device.getVmUuid(), device);

			db = new DBHelper();
			String sql = "INSERT INTO `mydatabase`.`device` "
					+ "(`id`, `vmUuid`, `vifUuid`, `vifNumber`, `switchPort`, `ipAddr`, `macAddr`)"
					+ " VALUES (NULL, \'" + device.getVmUuid() + "\'," + " \'"
					+ device.getVifUuid() + "\', " + "\'"
					+ device.getVifNumber() + "\', " + "\'"
					+ device.getSwitchPort() + "\', " + "\'"
					+ device.getIpAddr() + "\', " + "\'" + device.getMacAddr()
					+ "\');";
			try {
				if (db.executeUpdate(sql) == 0) {
					throw new Exception("Insert device, 0 returned.");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void addQosForDevice(String vmUuid, String qosUuid) {
		// TODO Auto-generated method stub
		if (devices.containsKey(vmUuid)) {
			devices.get(vmUuid).setQosUuid(qosUuid);

			db = new DBHelper();
			String sql = "SELECT * FROM `devicetoqos` WHERE `vmUuid`=\'"
					+ vmUuid + "\'";
			ResultSet result;
			try {
				// see if there is a qos to device exist, if yes, remove it.
				result = db.executeQuery(sql);
				if (result.getRow() != 0) {
					removeQosForDevice(result.getString("vmUuid"),
							result.getString("qosUuid"));
				}

				// insert qos
				sql = "INSERT INTO `mydatabase`.`devicetoqos` (`id`, `vmUuid`, `qosUuid`) VALUES (NULL, \'"
						+ vmUuid + "\', \'" + qosUuid + "\');";
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

	public static Device getDevice(String vmUuid) {
		return devices.get(vmUuid);
	}

	private static void getDeviceFromController() throws JSONException {
		DevicesJSON.getDeviceSummaries(devices);
		for (Device device : devices.values()) {
			if (device.getSwitchDpid() != null) {
				Switch sw = DataProvider.getSwitch(device.getSwitchDpid());
				if (sw != null) {
					List<Port> ports = sw.getPorts();
					if (ports != null) {
						Iterator<Port> it = ports.iterator();
						while (it.hasNext()) {
							Port port = it.next();
							if (port.getPortNumber().equals(
									device.getSwitchPort())) {
								device.setVifNumber(port.getName());
								break;
							}
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
		getDeviceFromXenServer();
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
				devices.put(device.getVmUuid(), device);
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

	public static String getQosForDevice(String vmUuid) {
		return devices.get(vmUuid).getQosUuid();
	}

	public static void removeDevice(String vmUuid) {
		if (devices.containsKey(vmUuid)) {
			devices.remove(vmUuid);

			db = new DBHelper();
			String sql = "DELETE FROM `device` WHERE `vmUuid`=\'" + vmUuid
					+ "\'";
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

	public static void removeQosForDevice(String vmUuid, String qosUuid) {
		// TODO Auto-generated method stub
		if (devices.containsKey(vmUuid)) {
			devices.get(vmUuid).setQosUuid(null);

			DBHelper db1 = new DBHelper();
			String sql = "DELETE FROM `devicetoqos` WHERE `qosUuid`=\'"
					+ qosUuid + "\' AND `vmUuid`=\'" + vmUuid + "\'";
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

	public static void updateDevice(String vmUuid, String key, Object value) {
		// TODO Auto-generated method stub
		if (devices.containsKey(vmUuid)) {
			db = new DBHelper();
			String sql;
			switch (key) {
			case "uploadRate":
				// deal with long value
				devices.get(vmUuid).setUploadRate(((Long) value).longValue());

				sql = "UPDATE `mydatabase`.`device` SET `" + key + "` = "
						+ ((Long) value).longValue()
						+ " WHERE `device`.`vmUuid` = \'" + vmUuid + "\';";
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
				devices.get(vmUuid).setVifNumber(value.toString());
			case "switchPort":
				devices.get(vmUuid).setSwitchPort(value.toString());
			case "ipAddr":
				devices.get(vmUuid).setIpAddr(value.toString());
				sql = "UPDATE `mydatabase`.`device` SET `" + key + "` = \'"
						+ value.toString() + "\' WHERE `device`.`vmUuid` = \'"
						+ vmUuid + "\';";
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
