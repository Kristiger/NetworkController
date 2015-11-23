package com.basic.elements;

import java.util.Date;

public class Device {
	private String vifNumber, ipAddr, macAddr, swtichPort, switchDpid, qosUuid,
			vmUuid, vifUuid;
	
	private boolean isActive = false;

	private Date lastSeen;

	public Device(String mac) {
		// TODO Auto-generated constructor stub
		this.setMacAddr(mac);
	}

	public String getVifNumber() {
		return vifNumber;
	}

	public void setVifNumber(String vifNumber) {
		this.vifNumber = vifNumber;
	}

	public String getIpAddr() {
		return ipAddr;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	public String getMacAddr() {
		return macAddr;
	}

	public void setMacAddr(String macAddr) {
		this.macAddr = macAddr;
	}

	public String getSwtichPort() {
		return swtichPort;
	}

	public void setSwtichPort(String swtichPort) {
		this.swtichPort = swtichPort;
	}

	public String getSwitchDpid() {
		return switchDpid;
	}

	public void setSwitchDpid(String switchDpid) {
		this.switchDpid = switchDpid;
	}

	public String getQosUuid() {
		return qosUuid;
	}

	public void setQosUuid(String qosUuid) {
		this.qosUuid = qosUuid;
	}

	public String getVmUuid() {
		return vmUuid;
	}

	public void setVmUuid(String vmUuid) {
		this.vmUuid = vmUuid;
	}

	public String getVifUuid() {
		return vifUuid;
	}

	public void setVifUuid(String vifUuid) {
		this.vifUuid = vifUuid;
	}

	public Date getLastSeen() {
		return lastSeen;
	}

	public void setLastSeen(Date lastSeen) {
		this.lastSeen = lastSeen;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public String toString() {
		return "Device [vifNumber=" + vifNumber + ", ipAddr=" + ipAddr
				+ ", macAddr=" + macAddr + ", swtichPort=" + swtichPort
				+ ", switchDpid=" + switchDpid + ", qosUuid=" + qosUuid
				+ ", vmUuid=" + vmUuid + "]";
	}
}
