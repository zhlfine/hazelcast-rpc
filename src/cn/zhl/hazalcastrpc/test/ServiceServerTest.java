package cn.zhl.hazalcastrpc.test;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import cn.zhl.hazalcastrpc.ServiceRegistry;

public class ServiceServerTest {

	public static void main(String[] args){
		HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
		ServiceRegistry.getInstance().setHazelcastInstance(hazelcastInstance);
		
		HelloService helloService = new HelloServiceImpl();
		ServiceRegistry.getInstance().bind(HelloService.class, helloService);
	}
	
}
