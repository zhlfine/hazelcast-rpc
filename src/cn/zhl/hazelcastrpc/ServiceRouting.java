package cn.zhl.hazelcastrpc;

public interface ServiceRouting {

	ServiceTarget getTarget() throws NoRouteToServiceException;
	
}
