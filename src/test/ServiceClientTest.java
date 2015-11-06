package test;

import cn.zhl.hazelcastrpc.NoRouteToServiceException;
import cn.zhl.hazelcastrpc.ServiceNotBindException;
import cn.zhl.hazelcastrpc.ServiceRegistry;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

public class ServiceClientTest {

	public static void main(String[] args) throws ServiceNotBindException, NoRouteToServiceException{
		HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
		ServiceRegistry.getInstance().setHazelcastInstance(hazelcastInstance);

		HelloService helloService = ServiceRegistry.getInstance().getService(HelloService.class);
		String hello = helloService.sayHello("Godson");
		System.out.println(hello);
	}
	
}
