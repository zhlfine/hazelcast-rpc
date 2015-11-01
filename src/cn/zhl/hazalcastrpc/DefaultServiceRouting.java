package cn.zhl.hazalcastrpc;

import java.util.Map;

public class DefaultServiceRouting implements ServiceRouting {

	private Class<?> svcClass;
	private ServiceSkeleton serviceSkeleton;
	
	public DefaultServiceRouting(Class<?> svcClass, ServiceSkeleton serviceSkeleton){
		this.svcClass = svcClass;
		this.serviceSkeleton = serviceSkeleton;
	}
	
	public ServiceTarget getTarget() throws NoRouteToServiceException {
		ServiceBindingInfo svcBinding = serviceSkeleton.getServiceBindingInfo(svcClass);
		if(svcBinding == null || svcBinding.getBindingService().size() == 0){
			throw new NoRouteToServiceException("Service "+svcClass.getName()+" is not provided");
		}
		if(svcBinding.getBindingService().size() > 1){
			throw new NoRouteToServiceException("Service "+svcClass.getName()+" has "+svcBinding.getBindingService().size()+" service providers. A ServiceInvocationRouting is needed");
		}
		
		Map<ServiceTarget, Service> bindingServices = svcBinding.getBindingService();
		return bindingServices.entrySet().iterator().next().getKey();
	}

}
