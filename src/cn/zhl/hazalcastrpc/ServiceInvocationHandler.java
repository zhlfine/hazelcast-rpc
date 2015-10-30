package cn.zhl.hazalcastrpc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

import com.hazelcast.core.IMap;

import cn.zhl.hazalcastrpc.test.HelloService;
import cn.zhl.hazalcastrpc.test.HelloServiceImpl;

public class ServiceInvocationHandler<T> implements InvocationHandler {
	private Map<Class<? extends Service>, ServiceBindingInfo> bindedServices;
	private Class<T> svcClass;
	
	HelloService s = new HelloServiceImpl();
	
	public ServiceInvocationHandler(IMap<Class<? extends Service>, ServiceBindingInfo> bindedServices2, Class<T> svcClass){
		this.bindedServices = bindedServices2;
		this.svcClass = svcClass;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object result = null;
		try{
			result = method.invoke(s, args);
		}catch(Throwable e){
			throw e;
		}
		return result;
	}

}
