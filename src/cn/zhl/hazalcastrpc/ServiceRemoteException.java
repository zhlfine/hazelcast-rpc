package cn.zhl.hazalcastrpc;

public class ServiceRemoteException extends Exception {
	private static final long serialVersionUID = 1L;

	public ServiceRemoteException(Exception cause){
		super(cause);
	}
}
