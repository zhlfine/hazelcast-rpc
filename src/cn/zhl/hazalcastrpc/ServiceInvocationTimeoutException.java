package cn.zhl.hazalcastrpc;

public class ServiceInvocationTimeoutException extends Exception {
	private static final long serialVersionUID = 1L;

	public ServiceInvocationTimeoutException(String message){
		super(message);
	}
}
