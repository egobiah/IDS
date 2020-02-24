package Hello;

import java.rmi.RemoteException;

public class Info_itf_impl implements Info_itf {
    @Override
    public String getName(Client client) throws RemoteException {
        System.out.println("Je suis la ");
        return client.toString();
    }
}
