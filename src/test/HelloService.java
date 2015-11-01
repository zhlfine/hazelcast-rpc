package test;

import cn.zhl.hazalcastrpc.Service;

public interface HelloService extends Service {

	String sayHello(String who);
	
}
