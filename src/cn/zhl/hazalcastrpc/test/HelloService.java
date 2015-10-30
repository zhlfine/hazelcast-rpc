package cn.zhl.hazalcastrpc.test;

import cn.zhl.hazalcastrpc.Service;

public interface HelloService extends Service {

	String sayHello(String hello);
	
}
