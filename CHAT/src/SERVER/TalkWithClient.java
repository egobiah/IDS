package SERVER;
import java.rmi.*;
import BD.*;

public interface TalkWithClient extends Remote {
    public String sendMessage(Message m)  throws RemoteException;
    public void test() throws RemoteException;
    public String sayHi(Person p) throws RemoteException;
}





