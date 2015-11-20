package com.util.device;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sound.sampled.Port;

import com.basic.elements.Device;

public class DeviceUtil {
	private static Map<String, Device> devices = new HashMap<String, Device>();
	
	public DeviceUtil(){
		startContinuingUpdate();
	}
	
	private void startContinuingUpdate() {
		// TODO Auto-generated method stub
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				List<Port> ports = 
			}
		});
	}

	public static Map<String, Device> getDeviceFromController(){
		return devices;
	}

	public static Map<String, Device> getDevices() {
		return devices;
	}
	
	public static void updateDevice(Device device, boolean active){
		if(!devices.containsKey(device.getMacAddr()))
			devices.put(device.getMacAddr(), device);
		device.setActive(active);
	}
}
