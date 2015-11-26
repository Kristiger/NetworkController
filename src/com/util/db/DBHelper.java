package com.util.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.swt.internal.C;

import com.basic.elements.Device;
import com.main.app.qos.QosPolicy;
import com.main.app.qos.QosQueue;

public class DBHelper {
	/*
	 * Since once the application is disposed, policies stored will lose, and
	 * next time can be found except from xen, it should be stored, and when
	 * enqueue, check if it exists. cmd:ovs-vsctl list <port> | grep qos ==
	 * <queue.uuid>
	 */

	private static final String url = "jdbc:mysql://127.0.0.1:3306/mydatabase";
	private static final String userName = "root";
	private static final String password = "240077";

	private static Connection conn = null;
	private static Statement statement = null;

	public DBHelper() {
		getDBConnection();
	}

	public void close() {
		try {
			if (statement != null)
				statement.close();
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void getDBConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url, userName, password);
			statement = conn.createStatement();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*// normally execute the update sql syntax
	private void executeUpdateStatement(String sql) {

		try {
			if (statement.executeUpdate(sql) == 0) {
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	

	// return all
	public Map<String, Device> getDevice() {
		String sql = "SELECT * FROM `vm` LIMIT 0, 30 ";
		Map<String, Device> devices = new ConcurrentHashMap<String, Device>();

		try {
			ResultSet result = statement.executeQuery(sql);
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
		}
		return devices;
	}

	public Device getDevice(String vmUuid) {
		String sql = "SELECT * FROM `device` WHERE `vmUuid` = \'" + vmUuid
				+ "\' LIMIT 0, 30 ";
		Device device = new Device();
		try {
			ResultSet result = statement.executeQuery(sql);
			if (result.next()) {
				device.setVmUuid(result.getString("vmUuid"));
				device.setVifUuid(result.getString("vifUuid"));
				device.setVifNumber(result.getString("vifNumber"));
				device.setSwitchPort(result.getString("switchPort"));
				device.setIpAddr(result.getString("ipAddr"));
				device.setMacAddr(result.getString("macAddr"));
			}
			result.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return device;
	}

	// return all
	public Map<String, QosPolicy> getQos() {
		String sql = "SELECT * FROM `qos` LIMIT 0, 30 ";
		Map<String, QosPolicy> qoses = new ConcurrentHashMap<String, QosPolicy>();
		QosPolicy qos = null;
		try {
			ResultSet result = statement.executeQuery(sql);
			while (result.next()) {
				qos = new QosPolicy();
				qos.setUuid(result.getString("qosUuid"));
				qos.setMaxRate(result.getLong("maxRate"));
				qos.setMinRate(result.getLong("minRate"));
				qoses.put(qos.getUuid(), qos);
			}
			result.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return qoses;
	}

	public QosPolicy getQos(String qosUuid) {
		String sql = "SELECT * FROM `qos` WHERE `qosUuid` = \'" + qosUuid
				+ "\' LIMIT 0, 30 ";
		QosPolicy qos = null;
		try {
			ResultSet result = statement.executeQuery(sql);
			if (result.next()) {
				qos = new QosPolicy();
				qos.setUuid(result.getString("qosUuid"));
				qos.setMaxRate(result.getLong("maxRate"));
				qos.setMinRate(result.getLong("minRate"));
			}
			result.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return qos;
	}

	public QosPolicy getQosForDevice(String vmUuid) {
		String sql = "SELECT * FROM `devicetoqos` WHERE `vmUuid` = \'" + vmUuid
				+ "\'";
		QosPolicy qos = null;
		try {
			ResultSet result = statement.executeQuery(sql);
			if (result.next()) {
				qos = getQos(result.getString("qosUuid"));
			}
			result.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return qos;
	}

	public Map<String, QosQueue> getQueue() {
		String sql = "SELECT * FROM `queue` LIMIT 0, 30 ";
		Map<String, QosQueue> queues = new ConcurrentHashMap<String, QosQueue>();
		QosQueue queue = null;
		try {
			ResultSet result = statement.executeQuery(sql);
			while (result.next()) {
				queue = new QosQueue();
				queue.setUuid(result.getString("queueUuid"));
				queue.setMaxRate(result.getLong("maxRate"));
				queue.setMinRate(result.getLong("minRate"));
				queues.put(queue.getUuid(), queue);
			}
			result.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return queues;
	}

	public QosQueue getQueue(String queueUuid) {
		String sql = "SELECT * FROM `queue` WHERE `queueUuid` = \'" + queueUuid
				+ "\'";
		QosQueue queue = null;
		try {
			ResultSet result = statement.executeQuery(sql);
			if (result.next()) {
				queue = new QosQueue();
				queue.setUuid(result.getString("queueUuid"));
				queue.setMaxRate(result.getLong("maxRate"));
				queue.setMinRate(result.getLong("minRate"));
			}
			result.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return queue;
	}

	public Map<Integer, String> getQueuesForQos(String qosUuid) {
		String sql = "SELECT * FROM `qostoqueue` WHERE `qosUuid` = \'"
				+ qosUuid + "\'";
		Map<Integer, String> queues = new ConcurrentHashMap<Integer, String>();
		try {
			ResultSet result = statement.executeQuery(sql);
			while (result.next()) {
				String queueUuid = result.getString("queueUuid");
				int id = result.getInt("queueId");
				queues.put(id, queueUuid);
			}
			result.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return queues;
	}

	public void insertDevice(Device device) {
		String sql = "INSERT INTO `mydatabase`.`device` "
				+ "(`id`, `vmUuid`, `vifUuid`, `vifNumber`, `switchPort`, `ipAddr`, `macAddr`)"
				+ " VALUES (NULL, \'" + device.getVmUuid() + "\'," + " \'"
				+ device.getVifUuid() + "\', " + "\'" + device.getVifNumber()
				+ "\', " + "\'" + device.getSwitchPort() + "\', " + "\'"
				+ device.getIpAddr() + "\', " + "\'" + device.getMacAddr()
				+ "\');";
		executeUpdateStatement(sql);
	}

	public void insertQos(QosPolicy qos) {
		String sql = "INSERT INTO `mydatabase`.`qos` (`id`, `qosUuid`, `minRate`, `maxRate`) "
				+ "VALUES (NULL, \'"
				+ qos.getUuid()
				+ "\', \'"
				+ qos.getMinRate() + "\', \'" + qos.getMaxRate() + "\');";
		executeUpdateStatement(sql);
	}

	public void insertQosForDevice(String vmUuid, String qosUuid) {
		// TODO Auto-generated method stub
		String sql = "SELECT * FROM `devicetoqos` WHERE `vmUuid`=\'" + vmUuid
				+ "\'";
		ResultSet result;
		try {
			// see if there is a qos to device exist, if yes, remove it.
			result = statement.executeQuery(sql);
			if (result.getRow() != 0) {
				removeQosToDevice(result.getString("vmUuid"),
						result.getString("qosUuid"));
			}

			// insert qos
			sql = "INSERT INTO `mydatabase`.`devicetoqos` (`id`, `vmUuid`, `qosUuid`) VALUES (NULL, \'"
					+ vmUuid + "\', \'" + qosUuid + "\');";
			executeUpdateStatement(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void insertQueue(QosQueue queue) {
		String sql = "INSERT INTO `mydatabase`.`queue` (`id`, `queueUuid`, `maxRate`, `minRate`) "
				+ "VALUES (NULL, \'"
				+ queue.getUuid()
				+ "\', \'"
				+ queue.getMaxRate() + "\', \'" + queue.getMinRate() + "\');";
		executeUpdateStatement(sql);
	}

	public void insertQueueForQos(String qosUuid, String queueUuid, int queueId) {
		// TODO Auto-generated method stub

		String sql = "SELECT * FROM `qostoqueue` WHERE `qosUuid`=\'" + qosUuid
				+ "\' AND `queueId`=" + queueId;
		ResultSet result;
		try {
			// see if there is a queue to qos exist, if yes, remove it.
			result = statement.executeQuery(sql);
			if (result.getRow() != 0) {
				removeQueueToQos(result.getString("qosUuid"),
						result.getString("queueUuid"));
			}
			sql = "INSERT INTO `mydatabase`.`qostoqueue` (`id`, `qosUuid`, `queueUuid`, `queueId`) VALUES (NULL, \'"
					+ qosUuid
					+ "\', \'"
					+ queueUuid
					+ "\', \'"
					+ queueId
					+ "\');";
			executeUpdateStatement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void removeDevice(String vmUuid) {
		String sql = "DELETE FROM `device` WHERE `vmUuid`=\'" + vmUuid + "\'";
		executeUpdateStatement(sql);
	}

	public void removeQos(String qosUuid) {
		String sql = "DELETE FROM `qos` WHERE `qosUuid`=\'" + qosUuid + "\'";
		executeUpdateStatement(sql);
	}

	public void removeQosToDevice(String vmUuid, String qosUuid) {
		// TODO Auto-generated method stub
		String sql = "DELETE FROM `devicetoqos` WHERE `qosUuid`=\'" + qosUuid
				+ "\' AND `vmUuid`=\'" + vmUuid + "\'";
		executeUpdateStatement(sql);
	}

	public void removeQueue(String queueUuid) {
		String sql = "DELETE FROM `queue` WHERE `queueUuid`=\'" + queueUuid
				+ "\'";
		executeUpdateStatement(sql);
	}

	public void removeQueueToQos(String qosUuid, String queueUuid) {
		// TODO Auto-generated method stub
		String sql = "DELETE FROM `qostoqueue` WHERE `qosUuid`=\'" + qosUuid
				+ "\' AND `queueUuid`=\'" + queueUuid + "\'";
		executeUpdateStatement(sql);
	}

	public void updateDevice(String key, Device device) {
		// TODO Auto-generated method stub
		String value = "";
		switch (key) {
		case "uploadRate":
			// deal with long value
			String sql = "UPDATE `mydatabase`.`device` SET `" + key + "` = \'"
					+ Long.valueOf(device.getUploadRate())
					+ "\' WHERE `device`.`vmUuid` = \'" + device.getVmUuid()
					+ "\';";
			executeUpdateStatement(sql);
			break;
		case "vifNumber":
			value = device.getVifNumber();
			break;
		case "switchPort":
			value = device.getSwitchPort();
			break;
		case "ipAddr":
			value = device.getIpAddr();
			break;
		}
		if (!value.equals("")) {
			String sql = "UPDATE `mydatabase`.`device` SET `" + key + "` = \'"
					+ value + "\' WHERE `device`.`vmUuid` = "
					+ device.getVmUuid() + ";";
			executeUpdateStatement(sql);
		}
	}*/

	public ResultSet executeQuery(String sql) throws SQLException {
		ResultSet result = statement.executeQuery(sql);
		return result;
	}
	
	public int executeUpdate(String sql) throws SQLException{
		return statement.executeUpdate(sql);
	}
}
