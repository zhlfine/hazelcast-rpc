package cn.zhl.hazalcastrpc;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ServiceBindingInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Map<String, Service> bindingSources = new HashMap<String, Service>();

	public void bindSource(String sourceAddr, Service svcImpl){
		if(bindingSources.containsKey(sourceAddr)){
			throw new RuntimeException("Cannot bind a service twice");
		}
		bindingSources.put(sourceAddr, svcImpl);
	}

}
