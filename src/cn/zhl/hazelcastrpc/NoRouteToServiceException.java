package cn.zhl.hazelcastrpc;

public class NoRouteToServiceException extends Exception {
	private static final long serialVersionUID = 1L;

	public NoRouteToServiceException(String message){
		super(message);
	}
}
