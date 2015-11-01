package cn.zhl.hazalcastrpc;

public interface ServiceRouting {

	ServiceTarget getTarget() throws NoRouteToServiceException;
	
}
