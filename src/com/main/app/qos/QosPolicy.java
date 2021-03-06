package com.main.app.qos;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class QosPolicy {
	private String uuid;
	private String switchdpid;
	private String vifport;

	private long maxRate;
	private long minRate;

	// <id, uuid>; max is 7
	private Map<Integer, String> queues;

	public QosPolicy() {
		queues = new ConcurrentHashMap<Integer, String>();
	}

	public QosPolicy(String uuid) {
		this();
		this.uuid = uuid;
	}

	public QosPolicy(String qosUuid, long maxRate, long minRate) {
		// TODO Auto-generated constructor stub
		this(qosUuid);
		this.maxRate = maxRate;
		this.minRate = minRate;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public void setQueues(Map<Integer, String> queues) {
		this.queues = queues;
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

	/**
	 * @param queueUuid
	 *            to add
	 * @return queue id, 0-6, -1 if all 7 queues are full
	 */
	public int addQueue(String queueUuid) {
		for (Integer id : queues.keySet()) {
			if (queues.get(id) == null) {
				queues.remove(id);
				queues.put(id, queueUuid);
				return id;
			}
		}
		return -1;
	}

	public Map<Integer, String> getQueues() {
		return queues;
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "QosPolicy [uuid=" + uuid + ", switchdpid=" + switchdpid
				+ ", vifport=" + vifport + ", maxRate=" + maxRate
				+ ", minRate=" + minRate + ", queues=" + queues + "]";
	}
}
