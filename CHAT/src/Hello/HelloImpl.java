package Hello;

import java.rmi.*;

public  class HelloImpl implements Hello {

	private String message;
 
	public HelloImpl(String s) {
		message = s ;
	}


	public String sayHello() throws RemoteException {
		return message;
	}

	@Override
	public String auth(String mdp) throws RemoteException {
		if (mdp.equals("motdepasse")){
			return "GOOD AUTH";
		} else {
			return "BAD AUTH";
		}
	}
}

