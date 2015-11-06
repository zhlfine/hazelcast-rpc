package cn.zhl.hazelcastrpc;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ServiceBindingInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Map<ServiceTarget, Service> bindingServices = new HashMap<ServiceTarget, Service>();

	public void bindService(ServiceTarget bindAddr, Service svcImpl){
		if(bindingServices.containsKey(bindAddr)){
			throw new RuntimeException("Cannot bind a service twice");
		}
		bindingServices.put(bindAddr, svcImpl);
	}
	
	public Service getServiceImpl(ServiceTarget target){
		return bindingServices.get(target);
	}
	
	public Map<ServiceTarget, Service> getBindingService(){
		return bindingServices;
	}

}
