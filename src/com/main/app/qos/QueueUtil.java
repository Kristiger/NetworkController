package com.main.app.qos;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.util.db.DBHelper;

public class QueueUtil {
	private static Map<String, QosQueue> queues = new ConcurrentHashMap<String, QosQueue>();
	private static DBHelper db = null;

	enum UPDATE {
		ADD, REMOVE
	}
	
	public QueueUtil() {
		updateFromDB();
	}
	
	private void updateFromDB() {
		db = new DBHelper();
		queues = db.getQueue();
	}

	public static Map<String, QosQueue> getQueues() {
		return queues;
	}

	public void addQueue(QosQueue queue){
		queues.put(queue.getUuid(), queue);
		updateDB(queue, UPDATE.ADD);
	}


	public QosQueue getQueue(String queueUuid) {
		if(queues.containsKey(queueUuid)){
			return queues.get(queueUuid);
		}
		return null;
	}
	
	public boolean removeQueue(String queueUuid){
		if(queues.containsKey(queueUuid)){
			QosQueue queue = queues.get(queueUuid);
			queues.remove(queueUuid);
			updateDB(queue, UPDATE.REMOVE);
			return true;
		}
		return false;
	}
	
	private void updateDB(QosQueue queue, UPDATE type) {
		// TODO Auto-generated method stub
		db = new DBHelper();
		if(type == UPDATE.ADD){
			db.insertQueue(queue);
		}else if(type == UPDATE.REMOVE){
			db.removeQueue(queue.getUuid());
		}
	}
}
