package test;

import cn.zhl.hazelcastrpc.Service;

public interface HelloService extends Service {

	String sayHello(String who);
	
}
