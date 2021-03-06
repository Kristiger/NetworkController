package com.main.app.qos;

public class QosQueue {
	
	private String uuid;
	private long minRate;
	private long maxRate;
	
	public QosQueue(){
	}
	
	public QosQueue(long maxRate, long minRate){
		this.minRate = minRate;
		this.maxRate = maxRate;
	}
	
	public QosQueue(String uuid, long maxRate, long minRate){
		this(maxRate,minRate);
		this.uuid = uuid;
	}
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public long getMinRate() {
		return minRate;
	}
	public void setMinRate(long minRate) {
		this.minRate = minRate;
	}
	public long getMaxRate() {
		return maxRate;
	}
	public void setMaxRate(long maxRate) {
		this.maxRate = maxRate;
	}

	/* ���� Javadoc��
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "QosQueue [uuid=" + uuid + ", minRate=" + minRate + ", maxRate="
				+ maxRate + "]";
	}
}
