package com.basic.elements;

import java.util.Date;

public class VmData {
	
	private String vmUuid, vifUuid;
	private String NameLabel;
	private String VifNumber;
	private String IpAddr;
	private String MacAddr;
	private String SwitchPort;
	private String SwitchDpid;
	
	private String qosUuid;
	private Date LastSeen;
	
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
	public String getVmNameLabel() {
		return NameLabel;
	}
	public void setVmNameLabel(String vmNameLabel) {
		this.NameLabel = vmNameLabel;
	}
	public String getVmVifNumber() {
		return VifNumber;
	}
	public void setVmVifNumber(String vmVifNumber) {
		this.VifNumber = vmVifNumber;
	}
	public String getVmSwitch() {
		return SwitchDpid;
	}
	public void setVmSwitch(String vmSwitch) {
		this.SwitchDpid = vmSwitch;
	}
	public String getVmSwitchPort() {
		return SwitchPort;
	}
	public void setVmSwitchPort(String vmSwitchPort) {
		this.SwitchPort = vmSwitchPort;
	}
	public String getVmIpAddr() {
		return IpAddr;
	}
	public void setVmIpAddr(String vmIpAddr) {
		this.IpAddr = vmIpAddr;
	}
	public String getVmMacAddr() {
		return MacAddr;
	}
	public void setVmMacAddr(String vmMacAddr) {
		this.MacAddr = vmMacAddr;
	}
	
	public Date getLastSeen() {
		return LastSeen;
	}
	public void setLastSeen(Date lastSeen) {
		LastSeen = lastSeen;
	}
	public String getQosUuid() {
		return qosUuid;
	}
	public void setQosUuid(String qosUuid) {
		this.qosUuid = qosUuid;
	}
	@Override
	public String toString() {
		return "VmData [vmUuid=" + vmUuid + ", vifUuid=" + vifUuid
				+ ", NameLabel=" + NameLabel + ", VifNumber=" + VifNumber
				+ ", IpAddr=" + IpAddr + ", MacAddr=" + MacAddr
				+ ", SwitchPort=" + SwitchPort + ", SwitchDpid=" + SwitchDpid
				+ ", qosUuid=" + qosUuid + ", LastSeen=" + LastSeen + "]";
	}
	
}
