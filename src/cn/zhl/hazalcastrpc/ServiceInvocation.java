package cn.zhl.hazalcastrpc;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ServiceInvocation implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long invocationId;
	private ServiceTarget src;
	private ServiceTarget target;
	private Class<?> serviceClass;
	private String methodName;
	private Object[] args;
	
	public ServiceInvocation(Long id, ServiceTarget src, ServiceTarget target, Class<?> svcClass, Method method, Object[] args){
		this.invocationId = id;
		this.src = src;
		this.target = target;
		this.serviceClass = svcClass;
		this.methodName = method.getName();
		this.args = args;
	}
	
	public Object invoke(Service svcImpl) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Class<?>[] paramClasses = null;
		if(args != null && args.length > 0){
			paramClasses = new Class<?>[args.length];
			for(int i = 0; i < args.length; i++){
				paramClasses[i] = args[i].getClass();
			}
		}
		Method method = serviceClass.getMethod(methodName, paramClasses);
		return method.invoke(svcImpl, args);
	}
	
	public ServiceReply createReply(Object reply){
		return new ServiceReply(invocationId, src, reply);
	}

	public Long getInvocationId() {
		return invocationId;
	}

	public ServiceTarget getTarget() {
		return target;
	}

	public Class<?> getServiceClass() {
		return serviceClass;
	}
	
	public String toString(){
		return "InvocationID "+invocationId+", target "+target.toString()+", "+serviceClass.getName()+"."+methodName+"("+Arrays.toString(args)+")";
	}

}
