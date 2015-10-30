package cn.zhl.hazalcastrpc.test;

public class HelloServiceImpl implements HelloService {
	private static final long serialVersionUID = 1L;

	public String sayHello(String hello) {
		return "Hello, "+hello;
	}

}
