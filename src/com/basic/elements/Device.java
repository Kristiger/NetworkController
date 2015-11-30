package com.basic.elements;

import java.util.Date;

public class Device {
	
	private String vmUuid;
	private String vifUuid;
	private String vifNumber;
	private String ipAddr;
	private String macAddr;
	private String switchPort;
	private String switchDpid;
	private String qosUuid;
	private long uploadRate = -1;
	
	private Date lastSeen;
	
	// if it is newly added from sw
	private boolean isActive = false;
	
	public Device(){
	}
	
	public Device(String mac) {
		// TODO Auto-generated constructor stub
		this.macAddr = mac;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public String getVmUuid() {
		return vmUuid;
	}
	public void setVmUuid(String vmuuid) {
		this.vmUuid = vmuuid;
	}
	public String getVifUuid() {
		return vifUuid;
	}
	public void setVifUuid(String vifUuid) {
		this.vifUuid = vifUuid;
	}
	public String getVifNumber() {
		return vifNumber;
	}
	public void setVifNumber(String VifNumber) {
		this.vifNumber = VifNumber;
	}
	public String getSwitchDpid() {
		return switchDpid;
	}
	public void setSwitchDpid(String Switch) {
		this.switchDpid = Switch;
	}
	public String getSwitchPort() {
		return switchPort;
	}
	public void setSwitchPort(String SwitchPort) {
		this.switchPort = SwitchPort;
	}
	public String getIpAddr() {
		return ipAddr;
	}
	public void setIpAddr(String IpAddr) {
		this.ipAddr = IpAddr;
	}
	public String getMacAddr() {
		return macAddr;
	}
	public void setMacAddr(String MacAddr) {
		this.macAddr = MacAddr;
	}
	public Date getLastSeen() {
		return lastSeen;
	}
	public void setLastSeen(Date lastSeen) {
		this.lastSeen = lastSeen;
	}
	public String getQosUuid() {
		return qosUuid;
	}
	public void setQosUuid(String qosUuid) {
		this.qosUuid = qosUuid;
	}
	public long getUploadRate() {
		return uploadRate;
	}
	public void setUploadRate(long uploadRate) {
		this.uploadRate = uploadRate;
	}

	@Override
	public String toString() {
		return "Device [vmUuid=" + vmUuid + ", vifUuid=" + vifUuid
				+ ", vifNumber=" + vifNumber + ", ipAddr=" + ipAddr
				+ ", macAddr=" + macAddr + ", switchPort=" + switchPort
				+ ", switchDpid=" + switchDpid + ", qosUuid=" + qosUuid
				+ ", uploadRate=" + uploadRate + ", lastSeen=" + lastSeen
				+ ", isActive=" + isActive + "]";
	}	
}
