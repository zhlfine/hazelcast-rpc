package cn.zhl.hazalcastrpc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ITopic;
import com.hazelcast.core.IdGenerator;
import com.hazelcast.core.Message;
import com.hazelcast.core.MessageListener;

public class ServiceStub implements InvocationHandler, MessageListener<ServiceReply> {
	private Logger logger = LoggerFactory.getLogger(ServiceStub.class);
	
	private long timeout;
	
	private ServiceRouting routing;
	private ServiceTarget myAddr;
	
	private ITopic<ServiceInvocation> invocationTopic;
	private ITopic<ServiceReply> replyTopic;
	private IdGenerator idGenerator;
	
	private ConcurrentHashMap<Long, ServiceReply> replyMap = new ConcurrentHashMap<Long, ServiceReply>();

	public ServiceStub(HazelcastInstance hz, ServiceRouting routing, long timeout){
		this.myAddr = new ServiceTarget(hz.getCluster().getLocalMember());
		this.routing = routing;
		this.timeout = timeout;
		
		this.invocationTopic = hz.getTopic(ServiceRegistry.HACELCASTRPC_INVOCATION_TOPIC);
		this.replyTopic = hz.getTopic(ServiceRegistry.HACELCASTRPC_REPLY_TOPIC);
		this.idGenerator = hz.getIdGenerator(ServiceRegistry.HACELCASTRPC_INVOCATION_ID);

		this.replyTopic.addMessageListener(this);
	}
	
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		ServiceTarget target = routing.getTarget();
		Long invocationId = idGenerator.newId();
		Class<?> svcClass = proxy.getClass().getInterfaces()[0];
		ServiceInvocation invocation = new ServiceInvocation(invocationId, myAddr, target, svcClass, method, args);
		
		ServiceReply dummy = new ServiceReply(invocationId, myAddr, new ServiceInvocationTimeoutException(timeout+"millis"));
		replyMap.put(invocationId, dummy);
		
		ServiceReply reply = null;
		try{
			logger.info("service request [{}]", invocation.toString());
			invocationTopic.publish(invocation);
			
			synchronized(dummy){
				dummy.wait(timeout);
			}
		}finally{
			reply = replyMap.remove(invocationId);
			logger.info("service reply [{}]", reply.toString());
		}
		
		Object result = reply.getResult();
		if(result != null && result instanceof Exception){
			throw (Exception)result;
		}
		return result;
	}

	public void onMessage(Message<ServiceReply> message) {
		ServiceReply reply = message.getMessageObject();
		if(!reply.getTarget().equals(myAddr)){
			return;
		}
		
		ServiceReply dummy = replyMap.get(reply.getInvocationId());
		if(dummy != null){
			logger.info("Received a reply [{}]", reply.toString());
			replyMap.replace(reply.getInvocationId(), reply);
			
			synchronized(dummy){
				dummy.notifyAll();
			}
		}else{
			logger.info("Received a reply [{}], but the request is not found", reply.toString());
		}
	}
	
}
