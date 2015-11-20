package com.basic.elements;

public class Device {
	private String vifNumber, ipAddr, macAddr, swtichPort, switchDpid, qosUuid,
			vmUuid;
	
	private boolean isActive = false;

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
