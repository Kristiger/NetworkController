package com.main.app.qos;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.util.db.DBHelper;

public class QosUtil {
	private static Map<String, QosPolicy> qoses = Collections
			.synchronizedMap(new HashMap<String, QosPolicy>());
	private static DBHelper db;

	enum UPDATE {
		ADD, REMOVE
	}
	
	public QosUtil(){
		updateFromDB();
	}

	private void updateFromDB() {
		// TODO Auto-generated method stub
		db = new DBHelper();
		qoses = db.getQos();
	}

	public static Map<String, QosPolicy> getQoses() {
		return qoses;
	}

	public static void addQos(QosPolicy qos) {
		qoses.put(qos.getUuid(), qos);
		updataDB(qos, UPDATE.ADD);
	}

	public static QosPolicy getQos(String qosUuid) {
		if (qoses.containsKey(qosUuid)) {
			return qoses.get(qosUuid);
		}
		return null;
	}

	public static boolean removeQos(String qosUuid) {
		if (qoses.containsKey(qosUuid)) {
			QosPolicy qos = qoses.get(qosUuid);
			qoses.remove(qosUuid);
			updataDB(qos, UPDATE.REMOVE);
			return true;
		}
		return false;
	}

	public static boolean addQosQueue(String qosUuid, String queueUuid) {
		if (qoses.containsKey(qosUuid)) {
			QosPolicy qos = qoses.get(qosUuid);
			if (qos.addQueue(queueUuid) != -1) {
				return true;
			}
		}
		return false;
	}
	
	private static void updataDB(QosPolicy qos, UPDATE type) {
		db = new DBHelper();
		if (type == UPDATE.ADD) {
			db.insertQos(qos);
		} else if (type == UPDATE.REMOVE) {
			db.removeQos(qos.getUuid());
		}
		db.closeDBConnection();
	}
}
