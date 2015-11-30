package com.main;

import com.basic.elements.UPDATETYPE;
import com.main.app.qos.QosPolicy;
import com.main.app.qos.QosQueue;
import com.main.provider.DataProvider;
import com.main.view.MainFrame;
import com.main.view.StartUp;
import com.util.xen.XenTools;

public class NetworkController {

	public static void main(String[] args) {
		// new StartUp();
		DataProvider.setIP("localhost");
		DataProvider.setPORT("8080");
		new MainFrame();
		//addDefaultQosAndQueue();
	}

	public static void addDefaultQosAndQueue() {
		String qosUuid = XenTools.createRow("qos", 50000, 50000);
		if (qosUuid != null) {
			QosPolicy qos = new QosPolicy(qosUuid, 50000, 50000);
			DataProvider.updateQosStore(qos, null, -1, UPDATETYPE.INSERT);

			String queueUuid = XenTools.createRow("queue", 50000, 50000);
			if (queueUuid != null) {
				QosQueue queue = new QosQueue(queueUuid, 50000, 50000);
				XenTools.addQosQueue(qosUuid, 0, queueUuid);
				DataProvider.updateQueueStore(queue, UPDATETYPE.INSERT);
				DataProvider.updateQosStore(qos, queueUuid, 0, UPDATETYPE.BAND);
			}
			
			queueUuid = XenTools.createRow("queue", 20000, 20000);
			if (queueUuid != null) {
				QosQueue queue = new QosQueue(queueUuid, 20000, 20000);
				XenTools.addQosQueue(qosUuid, 1, queueUuid);
				DataProvider.updateQueueStore(queue, UPDATETYPE.INSERT);
				DataProvider.updateQosStore(qos, queueUuid, 1, UPDATETYPE.BAND);
			}
			
			queueUuid = XenTools.createRow("queue", 10000, 10000);
			if (queueUuid != null) {
				QosQueue queue = new QosQueue(queueUuid, 10000, 10000);
				XenTools.addQosQueue(qosUuid, 2, queueUuid);
				DataProvider.updateQueueStore(queue, UPDATETYPE.INSERT);
				DataProvider.updateQosStore(qos, queueUuid, 2, UPDATETYPE.BAND);
			}
			
			queueUuid = XenTools.createRow("queue", 5000, 5000);
			if (queueUuid != null) {
				QosQueue queue = new QosQueue(queueUuid, 5000, 5000);
				XenTools.addQosQueue(qosUuid, 3, queueUuid);
				DataProvider.updateQueueStore(queue, UPDATETYPE.INSERT);
				DataProvider.updateQosStore(qos, queueUuid, 3, UPDATETYPE.BAND);
			}
			
			queueUuid = XenTools.createRow("queue", 2000, 2000);
			if (queueUuid != null) {
				QosQueue queue = new QosQueue(queueUuid, 2000, 2000);
				XenTools.addQosQueue(qosUuid, 4, queueUuid);
				DataProvider.updateQueueStore(queue, UPDATETYPE.INSERT);
				DataProvider.updateQosStore(qos, queueUuid, 4, UPDATETYPE.BAND);
			}
			
			queueUuid = XenTools.createRow("queue", 1000, 1000);
			if (queueUuid != null) {
				QosQueue queue = new QosQueue(queueUuid, 1000, 1000);
				XenTools.addQosQueue(qosUuid, 5, queueUuid);
				DataProvider.updateQueueStore(queue, UPDATETYPE.INSERT);
				DataProvider.updateQosStore(qos, queueUuid, 5, UPDATETYPE.BAND);
			}

			queueUuid = XenTools.createRow("queue", 500, 500);
			if (queueUuid != null) {
				QosQueue queue = new QosQueue(queueUuid, 500, 500);
				XenTools.addQosQueue(qosUuid, 6, queueUuid);
				DataProvider.updateQueueStore(queue, UPDATETYPE.INSERT);
				DataProvider.updateQosStore(qos, queueUuid, 6, UPDATETYPE.BAND);
			}
		}
	}
	
	
	/*
	 * 1. 3389
	 * 2. TCP
	 * 3. UDP
	 * 4. BT
	 * 5. OTHER
	 * */
	public static void defaultFlow(){
		
		String flow1 = "";
		String flow2 = "";
		String flow3 = "";
		String flow4 = "";
		String flow5 = "";
	}
}