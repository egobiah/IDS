package Hello;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Info_itf extends Remote {

    public String getName(Client client) throws RemoteException;
}