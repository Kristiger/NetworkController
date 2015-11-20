package com.main.app.qos;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class QosUtil {
	private static Map<String, QosPolicy> qoses = Collections
			.synchronizedMap(new HashMap<String, QosPolicy>());

	public static Map<String, QosPolicy> getQoses() {
		return qoses;
	}
}
