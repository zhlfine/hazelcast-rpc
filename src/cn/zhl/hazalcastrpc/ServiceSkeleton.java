package cn.zhl.hazalcastrpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.ITopic;
import com.hazelcast.core.Message;
import com.hazelcast.core.MessageListener;

public class ServiceSkeleton implements MessageListener<ServiceInvocation> {
	private Logger logger = LoggerFactory.getLogger(ServiceSkeleton.class);
	
	private IMap<Class<?>, ServiceBindingInfo> serviceMap;
	private ITopic<ServiceInvocation> invocationTopic;
	private ITopic<ServiceReply> replyTopic;
	private ServiceTarget myAddr;
	
	public ServiceSkeleton(HazelcastInstance hz){
		this.serviceMap = hz.getMap(ServiceRegistry.HACELCASTRPC_SERVICE_MAP);
		this.invocationTopic = hz.getTopic(ServiceRegistry.HACELCASTRPC_INVOCATION_TOPIC);
		this.replyTopic = hz.getTopic(ServiceRegistry.HACELCASTRPC_REPLY_TOPIC);
		this.myAddr = new ServiceTarget(hz.getCluster().getLocalMember());
		
		this.invocationTopic.addMessageListener(this);
	}
	
	public ServiceBindingInfo getServiceBindingInfo(Class<?> serviceClass){
		return serviceMap.get(serviceClass);
	}
	
	public Service getServiceImpl(Class<?> serviceClass, ServiceTarget target){
		ServiceBindingInfo bindingInfo = serviceMap.get(serviceClass);
		if(bindingInfo != null){
			return bindingInfo.getServiceImpl(target);
		}else{
			return null;
		}
	}
	
	public <T extends Service> void bind(Class<T> svcClass, Service svcImpl) {
		serviceMap.lock(svcClass);
		try{
			ServiceBindingInfo svcInfo = serviceMap.get(svcClass);
			if(svcInfo == null){
				svcInfo = new ServiceBindingInfo(); 
			}
			svcInfo.bindService(myAddr, svcImpl);	
			serviceMap.put(svcClass, svcInfo);
		}finally{
			serviceMap.unlock(svcClass);
		}
	}

	public void onMessage(Message<ServiceInvocation> message) {
		ServiceInvocation invocation = message.getMessageObject();
		if(!invocation.getTarget().equals(myAddr)){
			return;
		}
		
		logger.info("remote service invocation [{}]", invocation.toString());
		Object result = null;
		try {
			Service svcImpl = getServiceImpl(invocation.getServiceClass(), myAddr);
			if(svcImpl == null){	
				throw new ServiceNotBindException("Service "+invocation.getServiceClass().getName()+" is not provided by target "+myAddr.toString());
			}
			result = invocation.invoke(svcImpl);
		} catch (Exception e) {
			logger.warn("remote service invocation error", e);
			result = new ServiceRemoteException(e);
		}
		
		ServiceReply reply = invocation.createReply(result);
		logger.info("remote service reply [{}]", reply.toString());
		replyTopic.publish(reply);
	}
	
}
