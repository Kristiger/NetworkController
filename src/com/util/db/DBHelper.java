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

	public void closeDBConnection() {
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

	// return all
	public Map<String, QosPolicy> getQos() {
		String sql = "SELECT * FROM `qos` LIMIT 0, 30 ";
		Map<String, QosPolicy> qoses = Collections
				.synchronizedMap(new HashMap<String, QosPolicy>());
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

	public QosPolicy getQosForVm(String vmUuid) {
		String sql = "SELECT * FROM `vmtoqos` WHERE `vmUuid` = \'" + vmUuid
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
		Map<String, QosQueue> queues = Collections
				.synchronizedMap(new HashMap<String, QosQueue>());
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

	public List<QosQueue> getQueuesForQos(String qosUuid) {
		String sql = "SELECT * FROM `qostoqueue` WHERE `qosUuid` = \'"
				+ qosUuid + "\'";
		List<QosQueue> queues = new ArrayList<QosQueue>();
		try {
			ResultSet result = statement.executeQuery(sql);
			while (result.next()) {
				QosQueue queue = getQueue(result.getString("queueUuid"));
				if (queue != null) {
					queues.add(queue);
				}
			}
			result.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return queues;
	}

	// return all
	public List<Device> getDevice() {
		String sql = "SELECT * FROM `vm` LIMIT 0, 30 ";
		List<Device> vms = new ArrayList<Device>();
		try {
			ResultSet result = statement.executeQuery(sql);
			while (result.next()) {
				Device vm = new Device();
				vm.setVmUuid(result.getString("vmUuid"));
				vm.setVifUuid(result.getString("vifUuid"));
				vm.setVifNumber(result.getString("vifNumber"));
				vm.setSwitchPort(result.getString("switchPort"));
				vm.setIpAddr(result.getString("ipAddr"));
				vm.setMacAddr(result.getString("macAddr"));
				vms.add(vm);
			}
			result.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return vms;
	}

	public Device getDevice(String vmUuid) {
		String sql = "SELECT * FROM `vm` WHERE `vmUuid` = \'" + vmUuid
				+ "\' LIMIT 0, 30 ";
		Device vm = new Device();
		try {
			ResultSet result = statement.executeQuery(sql);
			if (result.next()) {
				vm.setVmUuid(result.getString("vmUuid"));
				vm.setVifUuid(result.getString("vifUuid"));
				vm.setVifNumber(result.getString("vifNumber"));
				vm.setSwitchPort(result.getString("switchPort"));
				vm.setIpAddr(result.getString("ipAddr"));
				vm.setMacAddr(result.getString("macAddr"));
			}
			result.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return vm;
	}

	public void insertQos(QosPolicy qos) {
		String sql = "INSERT INTO `mydatabase`.`qos` (`id`, `qosUuid`, `minRate`, `maxRate`) "
				+ "VALUES (NULL, \'"
				+ qos.getUuid()
				+ "\', \'"
				+ qos.getMinRate() + "\', \'" + qos.getMaxRate() + "\');";
		try {
			if (statement.executeUpdate(sql) == 0) {
				throw new Exception("Insert return 0 changes");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void insertQosForVm(String vmUuid, String qosUuid) {
		// TODO Auto-generated method stub
		String sql = "INSERT INTO `mydatabase`.`vmtoqos` (`id`, `vmUuid`, `qosUuid`) VALUES (NULL, \'"
				+ vmUuid + "\', \'" + qosUuid + "\');";
		try {
			if (statement.executeUpdate(sql) == 0) {
				throw new Exception("Insert return 0 changes");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void insertQueue(QosQueue queue) {
		String sql = "INSERT INTO `mydatabase`.`queue` (`id`, `queueUuid`, `queueId`, `maxRate`, `minRate`) "
				+ "VALUES (NULL, \'"
				+ queue.getUuid()
				+ "\', \'"
				+ queue.getMaxRate() + "\', \'" + queue.getMinRate() + "\');";
		try {
			if (statement.executeUpdate(sql) == 0) {
				throw new Exception("Insert return 0 changes");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void insertQueueForQos(String qosUuid, String queueUuid, int queueId) {
		// TODO Auto-generated method stub
		String sql = "INSERT INTO `mydatabase`.`qostoqueue` (`id`, `qosUuid`, `queueUuid`, `queueId`) VALUES (NULL, \'"
				+ qosUuid + "\', \'" + queueId + "\', \'" + queueId + "\');";
		try {
			if (statement.executeUpdate(sql) == 0) {
				throw new Exception("Insert return 0 changes");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void insertDevice(Device vm) {
		String sql = "INSERT INTO `mydatabase`.`vm` "
				+ "(`id`, `vmUuid`, `vifUuid`, `vifNumber`, `switchPort`, `ipAddr`, `macAddr`)"
				+ " VALUES (NULL, \'" + vm.getVmUuid() + "\'," + " \'"
				+ vm.getVifUuid() + "\', " + "\'" + vm.getVifNumber() + "\', " + "\'"
				+ vm.getSwitchPort() + "\', " + "\'" + vm.getIpAddr()
				+ "\', " + "\'" + vm.getMacAddr() + "\');";
		try {
			if (statement.executeUpdate(sql) == 0) {
				throw new Exception("Insert return 0 changes");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean removeQos(String qosUuid) {
		String sql = "DELETE FROM `qos` WHERE `qosUuid`=\'" + qosUuid + "\'";
		try {
			if (statement.executeUpdate(sql) == 0) {
				throw new Exception("Insert return 0 changes");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void removeQosToVm(String vmUuid, String qosUuid) {
		// TODO Auto-generated method stub
		String sql = "DELETE FROM `qostovm` WHERE `qosUuid`=\'" + qosUuid
				+ "\' AND `vmUuid`=\'" + vmUuid + "\'";
		try {
			if (statement.executeUpdate(sql) == 0) {
				throw new Exception("Insert return 0 changes");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch blocks
			e.printStackTrace();
		}
	}

	public boolean removeQueue(String queueUuid) {
		try {
			String sql = "DELETE FROM `queue` WHERE `queueUuid`=\'" + queueUuid
					+ "\'";
			if (statement.executeUpdate(sql) == 0) {
				throw new Exception("Insert return 0 changes");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void removeQueueToQos(String qosUuid, String queueUuid) {
		// TODO Auto-generated method stub
		String sql = "DELETE FROM `queuetoqos` WHERE `qosUuid`=\'" + qosUuid
				+ "\' AND `queueUuid`=\'" + queueUuid + "\'";
		try {
			if (statement.executeUpdate(sql) == 0) {
				throw new Exception("Insert return 0 changes");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void removeVm(String vmUuid) {
		String sql = "DELETE FROM `vm` WHERE `vmUuid`=\'" + vmUuid + "\'";
		try {
			if (statement.executeUpdate(sql) == 0) {
				throw new Exception("Insert return 0 changes");
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
