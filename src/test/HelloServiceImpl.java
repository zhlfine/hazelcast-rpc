package test;

public class HelloServiceImpl implements HelloService {
	private static final long serialVersionUID = 1L;

	public String sayHello(String who) {
		return "Hello, "+who;
	}

}
