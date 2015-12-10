package com.main;

import com.basic.elements.UPDATETYPE;
import com.main.app.qos.QosPolicy;
import com.main.app.qos.QosQueue;
import com.main.provider.DataProvider;
import com.main.view.MainFrame;
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
		String qosUuid = XenTools.createRow("qos", 50000000, 50000000);// 50M
		if (qosUuid != null) {
			QosPolicy qos = new QosPolicy(qosUuid, 5000000, 5000000);
			DataProvider.updateQosStore(qos, null, -1, UPDATETYPE.INSERT);

			String queueUuid = XenTools.createRow("queue", 50000000, 50000000);//50M
			if (queueUuid != null) {
				QosQueue queue = new QosQueue(queueUuid, 50000000, 50000000);
				XenTools.addQosQueue(qosUuid, 0, queueUuid);
				DataProvider.updateQueueStore(queue, UPDATETYPE.INSERT);
				DataProvider.updateQosStore(qos, queueUuid, 0, UPDATETYPE.BAND);
			}
			
			queueUuid = XenTools.createRow("queue", 20000000, 20000000);//20M
			if (queueUuid != null) {
				QosQueue queue = new QosQueue(queueUuid, 20000000, 20000000);
				XenTools.addQosQueue(qosUuid, 1, queueUuid);
				DataProvider.updateQueueStore(queue, UPDATETYPE.INSERT);
				DataProvider.updateQosStore(qos, queueUuid, 1, UPDATETYPE.BAND);
			}
			
			queueUuid = XenTools.createRow("queue", 10000000, 10000000);//10M
			if (queueUuid != null) {
				QosQueue queue = new QosQueue(queueUuid, 10000000, 10000000);
				XenTools.addQosQueue(qosUuid, 2, queueUuid);
				DataProvider.updateQueueStore(queue, UPDATETYPE.INSERT);
				DataProvider.updateQosStore(qos, queueUuid, 2, UPDATETYPE.BAND);
			}
			
			queueUuid = XenTools.createRow("queue", 5000000, 5000000);//5M
			if (queueUuid != null) {
				QosQueue queue = new QosQueue(queueUuid, 5000000, 5000000);
				XenTools.addQosQueue(qosUuid, 3, queueUuid);
				DataProvider.updateQueueStore(queue, UPDATETYPE.INSERT);
				DataProvider.updateQosStore(qos, queueUuid, 3, UPDATETYPE.BAND);
			}
			
			queueUuid = XenTools.createRow("queue", 2000000, 2000000);//2M
			if (queueUuid != null) {
				QosQueue queue = new QosQueue(queueUuid, 2000000, 2000000);
				XenTools.addQosQueue(qosUuid, 4, queueUuid);
				DataProvider.updateQueueStore(queue, UPDATETYPE.INSERT);
				DataProvider.updateQosStore(qos, queueUuid, 4, UPDATETYPE.BAND);
			}
			
			queueUuid = XenTools.createRow("queue", 1000000, 1000000);//1M
			if (queueUuid != null) {
				QosQueue queue = new QosQueue(queueUuid, 1000000, 1000000);
				XenTools.addQosQueue(qosUuid, 5, queueUuid);
				DataProvider.updateQueueStore(queue, UPDATETYPE.INSERT);
				DataProvider.updateQosStore(qos, queueUuid, 5, UPDATETYPE.BAND);
			}

			queueUuid = XenTools.createRow("queue", 500000, 500000);//0.5M
			if (queueUuid != null) {
				QosQueue queue = new QosQueue(queueUuid, 500000, 500000);
				XenTools.addQosQueue(qosUuid, 6, queueUuid);
				DataProvider.updateQueueStore(queue, UPDATETYPE.INSERT);
				DataProvider.updateQosStore(qos, queueUuid, 6, UPDATETYPE.BAND);
			}
		}
	}
}