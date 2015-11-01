package test;

import cn.zhl.hazalcastrpc.Service;
import cn.zhl.hazalcastrpc.ServiceRegistry;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

public class ServiceServerTest {

	public static void main(String[] args){
		HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
		ServiceRegistry.getInstance().setHazelcastInstance(hazelcastInstance);
		
		Service helloService = new HelloServiceImpl();
		ServiceRegistry.getInstance().bind(HelloService.class, helloService);
	}
	
}
