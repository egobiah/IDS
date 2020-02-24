package Hello;
import java.rmi.*;

public interface Hello extends Remote {
	public String sayHello()  throws RemoteException;
	public String auth(String mdp) throws RemoteException;
}
