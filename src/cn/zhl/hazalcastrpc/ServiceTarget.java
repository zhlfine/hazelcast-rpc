package cn.zhl.hazalcastrpc;

import java.io.Serializable;
import java.net.SocketAddress;

import com.hazelcast.core.Member;

public class ServiceTarget implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private SocketAddress socketAddr;
	
	public ServiceTarget(Member member){
		socketAddr = member.getSocketAddress();
	}
	
	public String toString(){
		return socketAddr.toString();
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ServiceTarget other = (ServiceTarget) obj;
		if (socketAddr == null) {
			if (other.socketAddr != null)
				return false;
		} else if (!socketAddr.equals(other.socketAddr))
			return false;
		return true;
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((socketAddr == null) ? 0 : socketAddr.hashCode());
		return result;
	}
	
}
