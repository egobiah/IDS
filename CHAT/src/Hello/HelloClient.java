package Hello;

import Hello.Info_itf;

import java.rmi.*;
import java.rmi.registry.*;

public class HelloClient {

	public static void main(String [] args) {
	
	try {
	  if (args.length < 1) {
	   System.out.println("Usage: java HelloClient <rmiregistry host>");
	   return;}

	String host = args[0];

	// Get remote object reference
	Registry registry = LocateRegistry.getRegistry(host); 
	Hello h = (Hello) registry.lookup("HelloService");
	Info_itf i = (Info_itf) registry.lookup("HelloService2") ;

	Client client = new Client("Main", "127.0.0.1");
	// Remote method invocation

	String res = h.sayHello();
	System.out.println(res);
	String res2 = i.getName(client)	;
	System.out.println(res2);
	String res3 = h.auth("motdepasse");
	System.out.println(res3);
	} catch (Exception e)  {
		System.err.println("Error on client: " + e);
	}


  }


}