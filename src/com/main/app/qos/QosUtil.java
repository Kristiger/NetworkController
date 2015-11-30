package com.main.app.qos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.util.db.DBHelper;

public class QosUtil {
	enum UPDATE {
		ADD, REMOVE
	}

	private static Map<String, QosPolicy> qoses = new ConcurrentHashMap<String, QosPolicy>();

	private static DBHelper db;

	public static void addQos(QosPolicy qos) {
		if (!qoses.containsKey(qos.getUuid())) {
			qoses.put(qos.getUuid(), qos);

			db = new DBHelper();
			String sql = "INSERT INTO `mydatabase`.`qos` (`id`, `qosUuid`, `minRate`, `maxRate`) "
					+ "VALUES (NULL, \'"
					+ qos.getUuid()
					+ "\', \'"
					+ qos.getMinRate() + "\', \'" + qos.getMaxRate() + "\');";
			try {
				if (db.executeUpdate(sql) == 0) {
					throw new Exception("Insert qos, 0 returned");
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

	public static void addQueueForQos(String qosUuid, String queueUuid,
			int queueId) {
		// TODO Auto-generated method stub
		if (qoses.containsKey(qosUuid)) {
			Map<Integer, String> queues = qoses.get(qosUuid).getQueues();
			db = new DBHelper();
			try {
				queues.put(queueId, queueUuid);

				String sql = "INSERT INTO `mydatabase`.`qostoqueue` "
						+ "(`id`, `qosUuid`, `queueUuid`, `queueId`) VALUES (NULL, \'"
						+ qosUuid + "\', \'" + queueUuid + "\', \'" + queueId
						+ "\');";
				db.executeUpdate(sql);
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				db.close();
			}
		}
	}

	public static QosPolicy getQos(String qosUuid) {
		return qoses.get(qosUuid);
	}

	public static Map<String, QosPolicy> getQoses() {
		return qoses;
	}

	public static Map<String, QosPolicy> getQosFromDB() {
		// TODO Auto-generated method stub
		db = new DBHelper();
		String sql = "SELECT * FROM `qos` LIMIT 0, 30 ";
		QosPolicy qos = null;
		try {
			ResultSet result = db.executeQuery(sql);
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
		} finally {
			db.close();
		}
		return qoses;
	}

	public static Map<Integer, String> getQueuesForQos(String qosUuid) {

		db = new DBHelper();
		String sql = "SELECT * FROM `qostoqueue` WHERE `qosUuid` = \'"
				+ qosUuid + "\'";
		Map<Integer, String> queues = new ConcurrentHashMap<Integer, String>();
		try {
			ResultSet result = db.executeQuery(sql);
			while (result.next()) {
				String queueUuid = result.getString("queueUuid");
				int id = result.getInt("queueId");
				queues.put(id, queueUuid);
			}
			result.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			db.close();
		}
		return queues;
	}

	public static void removeQos(String qosUuid) {

		if (qoses.containsKey(qosUuid)) {
			qoses.remove(qosUuid);

			db = new DBHelper();
			String sql = "DELETE FROM `qos` WHERE `qosUuid`=\'" + qosUuid
					+ "\'";
			try {
				if (db.executeUpdate(sql) == 0) {
					throw new Exception("Remove qos, 0 returned");
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

	public static void removeQueueForQos(String qosUuid, String queueUuid,
			int queueId) {
		// TODO Auto-generated method stub
		if (qoses.containsKey(qosUuid)) {
			qoses.get(qosUuid).getQueues().remove(queueId);

			DBHelper db1 = new DBHelper();
			String sql = "DELETE FROM `qostoqueue` WHERE `qosUuid`=\'"
					+ qosUuid + "\' AND `queueUuid`=\'" + queueUuid + "\'";
			try {
				if (db1.executeUpdate(sql) == 0) {
					throw new Exception("Remove queue for qos, 0 returned");
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
}
