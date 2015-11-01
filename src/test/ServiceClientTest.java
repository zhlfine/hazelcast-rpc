package test;

import cn.zhl.hazalcastrpc.NoRouteToServiceException;
import cn.zhl.hazalcastrpc.ServiceNotBindException;
import cn.zhl.hazalcastrpc.ServiceRegistry;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

public class ServiceClientTest {

	public static void main(String[] args) throws ServiceNotBindException, NoRouteToServiceException{
		HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
		ServiceRegistry.getInstance().setHazelcastInstance(hazelcastInstance);

		HelloService helloService = ServiceRegistry.getInstance().getService(HelloService.class);
		String hello = helloService.sayHello("hzhou");
		System.out.println(hello);
	}
	
}
