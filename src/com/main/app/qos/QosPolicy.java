package com.main.app.qos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class QosPolicy {
	private String uuid;
	private String switchdpid;
	private String vifport;

	private long maxRate;
	private long minRate;

	private Map<String, QosQueue> queues;

	public QosPolicy() {
		queues = new HashMap<String, QosQueue>();
	}

	public QosPolicy(String uuid) {
		this();
		this.uuid = uuid;
	}

	public QosPolicy(long maxRate, long minRate) {
		this();

		this.maxRate = maxRate;
		this.minRate = minRate;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getSwitchdpid() {
		return switchdpid;
	}

	public void setSwitchdpid(String switchdpid) {
		this.switchdpid = switchdpid;
	}

	public String getVifport() {
		return vifport;
	}

	public void setVifport(String vifport) {
		this.vifport = vifport;
	}

	public long getMaxRate() {
		return maxRate;
	}

	public void setMaxRate(long maxRate) {
		this.maxRate = maxRate;
	}

	public long getMinRate() {
		return minRate;
	}

	public void setMinRate(long minRate) {
		this.minRate = minRate;
	}

	public void addQueue(QosQueue queue) {
		queues.add(queue);
	}

	public QosQueue getQueue(String queueid) {
		Iterator<QosQueue> it = queues.iterator();
		QosQueue queue = null;
		while (it.hasNext()) {
			queue = it.next();
			if (queue.getQueueID() == Integer.valueOf(queueid))
				return queue;
		}
		return null;
	}

	public List<QosQueue> getQueues() {
		return queues;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		QosPolicy other = (QosPolicy) obj;
		if (this.toString().equals(other.toString())) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "QosPolicy [uuid=" + uuid + ", maxRate=" + maxRate
				+ ", minRate=" + minRate + ", queues=" + queues.toString() + "]";
	}
}
