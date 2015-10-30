package cn.zhl.hazalcastrpc;

import java.lang.reflect.Proxy;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

public class ServiceRegistry {
	private static ServiceRegistry instance = null;
	
	private HazelcastInstance hazelcastInstance;
	private String myAddr;
	
	private IMap<Class<? extends Service>, ServiceBindingInfo> bindedServices;
	
	private ServiceRegistry(){
	}
	
	public static synchronized ServiceRegistry getInstance(){
		if(instance == null){
			instance = new ServiceRegistry();
		}
		return instance;
	}
	
	public void setHazelcastInstance(HazelcastInstance hazelcastInstance){
		this.hazelcastInstance = hazelcastInstance;
		this.myAddr = hazelcastInstance.getCluster().getLocalMember().getSocketAddress().toString();
		
		this.bindedServices = hazelcastInstance.getMap("HazelcastRPC-BindedServiceMap");
	}
	
	private void checkInitialization(){
		if(hazelcastInstance == null){
			throw new RuntimeException("The ServiceRegistry is not initialized");
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Service> T getService(Class<T> svcClass) throws ServiceNotBindException{
		checkInitialization();
		bindedServices.lock(svcClass);
			try{
			ServiceBindingInfo svcInfo = bindedServices.get(svcClass);
			if(svcInfo == null){
				throw new ServiceNotBindException("Service "+svcClass.getName()+" is not binded");
			}
			
			ServiceInvocationHandler<T> invocationHandler = new ServiceInvocationHandler<T>(bindedServices, svcClass);
			return (T)Proxy.newProxyInstance(svcClass.getClassLoader(), new Class<?>[]{svcClass}, invocationHandler);
		}finally{
			bindedServices.unlock(svcClass);
		}
	}
	
	public <T extends Service> void bind(Class<T> svcClass, T svcImpl){
		checkInitialization();
		bindedServices.lock(svcClass);
		try{
			ServiceBindingInfo svcInfo = bindedServices.get(svcClass);
			if(svcInfo == null){
				svcInfo = new ServiceBindingInfo();
			}
			svcInfo.bindSource(myAddr, svcImpl);
			
			bindedServices.put(svcClass, svcInfo);
		}finally{
			bindedServices.unlock(svcClass);
		}
	}
	
}
        