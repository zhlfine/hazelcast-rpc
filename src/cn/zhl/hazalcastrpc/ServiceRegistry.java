package cn.zhl.hazalcastrpc;

import java.lang.reflect.Proxy;

import com.hazelcast.core.HazelcastInstance;

public class ServiceRegistry {
	private static ServiceRegistry instance = null;
	
	public static final String HACELCASTRPC_SERVICE_MAP      = "HazelcastRPC-ServiceMap";
	public static final String HACELCASTRPC_INVOCATION_TOPIC = "HazelcastRPC-InvocationTopic";
	public static final String HACELCASTRPC_REPLY_TOPIC      = "HazelcastRPC-ReplyTopic";
	public static final String HACELCASTRPC_INVOCATION_ID    = "HazelcastRPC-InvocationID";
	
	public static final long DEFAULT_SERVICE_TIMEOUT = 5000;
	
	private HazelcastInstance hz;
	private ServiceSkeleton serviceSkeleton;
	
	private ServiceRegistry(){
	}
	
	public static synchronized ServiceRegistry getInstance(){
		if(instance == null){
			instance = new ServiceRegistry();
		}
		return instance;
	}
	
	public void setHazelcastInstance(HazelcastInstance hz){
		this.hz = hz;
		this.serviceSkeleton = new ServiceSkeleton(hz);
	}
	
	private void checkInitialization(){
		if(hz == null){
			throw new RuntimeException("The ServiceRegistry is not initialized");
		}
	}
	
	public <T extends Service> T getService(Class<T> svcClass) throws ServiceNotBindException, NoRouteToServiceException{
		return getService(svcClass, new DefaultServiceRouting(svcClass, serviceSkeleton), DEFAULT_SERVICE_TIMEOUT);
	}
	
	public <T extends Service> T getService(Class<T> svcClass, long serviceTimeout) throws ServiceNotBindException, NoRouteToServiceException{
		return getService(svcClass, new DefaultServiceRouting(svcClass, serviceSkeleton), serviceTimeout);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Service> T getService(Class<T> svcClass, ServiceRouting routing, long serviceTimeout) throws ServiceNotBindException, NoRouteToServiceException{
		checkInitialization();

		ServiceTarget target = routing.getTarget();
		Service svcImpl = serviceSkeleton.getServiceImpl(svcClass, target);
		if(svcImpl == null){
			throw new ServiceNotBindException("Service "+svcClass.getName()+" is not provided by target "+target.toString());
		}
		
		ServiceStub serviceStub = new ServiceStub(hz, routing, serviceTimeout);
		return (T)Proxy.newProxyInstance(svcClass.getClassLoader(), new Class<?>[]{svcClass}, serviceStub);
	}
	
	public <T extends Service> void bind(Class<T> svcClass, Service svcImpl) {
		checkInitialization();
		if(svcImpl.getClass().isAssignableFrom(svcClass)){
			throw new RuntimeException(svcImpl.getClass().getName()+" is not the implementation of "+svcClass.getName());
		}
		serviceSkeleton.bind(svcClass, svcImpl);
	}
	
}
        