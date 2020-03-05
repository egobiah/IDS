package SERVER;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import BD.*;

public class Server {

    public static void  main(String [] args) {
        try {
           Talk_Implem t = new Talk_Implem();
           TalkWithClient t_stub = (TalkWithClient)  UnicastRemoteObject.exportObject(t, 0);

            // Register the remote object in RMI registry with a given identifier
            Registry registry= LocateRegistry.getRegistry();
            registry.bind("Talk", t_stub);


            System.out.println ("Server ready");

        } catch (Exception e) {
            System.err.println("Error on server :" + e) ;
            e.printStackTrace();
        }
    }
}
