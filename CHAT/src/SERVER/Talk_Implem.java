package SERVER;

import BD.Message;
import BD.Person;

import java.io.Serializable;
import java.rmi.RemoteException;

public class Talk_Implem implements TalkWithClient {
    public Talk_Implem(){

    }

    @Override
    public String sendMessage(Message m) throws RemoteException {
        System.out.println("Recu le message " + m.toString());
        return m.toString();
    }

    @Override
    public void test() throws RemoteException {
        System.out.println("C'est bien passé");
    }

    @Override

    public String sayHi(Person p) throws RemoteException {
        System.out.println(p.toString() + "is now on");
        return "Hello : " + p.toString();
    }
}
