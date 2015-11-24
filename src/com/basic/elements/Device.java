package com.basic.elements;

import java.util.Date;

public class Device {
	private boolean isActive = false;
	
	private Date lastSeen;
	private String uploadRate;
	private String vifNumber, ipAddr, macAddr, swtichPort, switchDpid, qosUuid,
			vmUuid, vifUuid;

	public Device(String mac) {
		// TODO Auto-generated constructor stub
		this.setMacAddr(mac);
	}

	public String getIpAddr() {
		return ipAddr;
	}

	public Date getLastSeen() {
		return lastSeen;
	}

	public String getMacAddr() {
		return macAddr;
	}

	public String getQosUuid() {
		return qosUuid;
	}

	public String getSwitchDpid() {
		return switchDpid;
	}

	public String getSwtichPort() {
		return swtichPort;
	}

	public String getUploadRate() {
		return uploadRate;
	}

	public String getVifNumber() {
		return vifNumber;
	}

	public String getVifUuid() {
		return vifUuid;
	}

	public String getVmUuid() {
		return vmUuid;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	public void setLastSeen(Date lastSeen) {
		this.lastSeen = lastSeen;
	}

	public void setMacAddr(String macAddr) {
		this.macAddr = macAddr;
	}

	public void setQosUuid(String qosUuid) {
		this.qosUuid = qosUuid;
	}

	public void setSwitchDpid(String switchDpid) {
		this.switchDpid = switchDpid;
	}

	public void setSwtichPort(String swtichPort) {
		this.swtichPort = swtichPort;
	}

	public void setUploadRate(String uploadRate) {
		this.uploadRate = uploadRate;
	}

	public void setVifNumber(String vifNumber) {
		this.vifNumber = vifNumber;
	}

	public void setVifUuid(String vifUuid) {
		this.vifUuid = vifUuid;
	}

	public void setVmUuid(String vmUuid) {
		this.vmUuid = vmUuid;
	}
}
