package cn.zhl.hazalcastrpc;

import java.io.Serializable;

public class ServiceReply implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long invocationId;
	private ServiceTarget target;
	private Object result;
	
	public ServiceReply(Long id, ServiceTarget target, Object result) {
		this.invocationId = id;
		this.target = target;
		this.result = result;
	}

	public Long getInvocationId() {
		return invocationId; 
	}

	public ServiceTarget getTarget() {
		return target;
	}

	public Object getResult() {
		return result;
	}

	public String toString(){
		return "InvocationID "+invocationId+", target "+(target==null?"null":target.toString())+", result "+(result==null?"null":result.toString());
	}

}
