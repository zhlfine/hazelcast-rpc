package cn.zhl.hazalcastrpc;

public class ServiceNotBindException extends Exception {
	private static final long serialVersionUID = 1L;

	public ServiceNotBindException(String message){
		super(message);
	}
}
