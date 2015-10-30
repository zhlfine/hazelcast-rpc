package cn.zhl.hazalcastrpc.test;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import cn.zhl.hazalcastrpc.ServiceNotBindException;
import cn.zhl.hazalcastrpc.ServiceRegistry;

public class ServiceClientTest {

	public static void main(String[] args) throws ServiceNotBindException{
		HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
		ServiceRegistry.getInstance().setHazelcastInstance(hazelcastInstance);
		
		ServiceRegistry.getInstance().bind(HelloService.class,  new HelloServiceImpl());
		
		HelloService helloService = ServiceRegistry.getInstance().getService(HelloService.class);
		String hello = helloService.sayHello("hzhou");
		System.out.println(hello);
	}
	
}
