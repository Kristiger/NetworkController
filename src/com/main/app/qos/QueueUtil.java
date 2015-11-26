package com.main.app.qos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.util.db.DBHelper;

public class QueueUtil {
	enum UPDATE {
		ADD, REMOVE
	}
	private static Map<String, QosQueue> queues = new ConcurrentHashMap<String, QosQueue>();

	private static DBHelper db = null;

	public static void addQueue(QosQueue queue) {
		if (!queues.containsKey(queue.getUuid())) {
			try {
				queues.put(queue.getUuid(), queue);
				db = new DBHelper();
				String sql = "INSERT INTO `mydatabase`.`queue` (`id`, `queueUuid`, `maxRate`, `minRate`) "
						+ "VALUES (NULL, \'"
						+ queue.getUuid()
						+ "\', \'"
						+ queue.getMaxRate()
						+ "\', \'"
						+ queue.getMinRate()
						+ "\');";
				if (db.executeUpdate(sql) == 0) {
					throw new Exception("Insert queue, 0 returned");
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

	public static QosQueue getQueue(String queueUuid) {
		return queues.get(queueUuid);
	}

	public static Map<String, QosQueue> getQueues() {
		return queues;
	}

	public static Map<String, QosQueue> getQueuesFromDB() {
		// TODO Auto-generated method stub
		db = new DBHelper();
		String sql = "SELECT * FROM `queue` LIMIT 0, 30 ";
		QosQueue queue = null;
		try {
			ResultSet result = db.executeQuery(sql);
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
		} finally {
			db.close();
		}
		return queues;
	}

	public static void removeQueue(String queueUuid) {
		if (queues.containsKey(queueUuid)) {
			try {
				queues.remove(queueUuid);

				db = new DBHelper();
				String sql = "DELETE FROM `queue` WHERE `queueUuid`=\'"
						+ queueUuid + "\'";
				if (db.executeUpdate(sql) == 0) {
					throw new Exception("Remove queue, 0 returned");
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
}
