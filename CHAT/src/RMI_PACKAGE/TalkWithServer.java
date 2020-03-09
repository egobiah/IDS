package RMI_PACKAGE;
import java.lang.reflect.Array;
import java.rmi.*;
import java.util.ArrayList;

import BD.*;

public interface TalkWithServer extends Remote {
    public void connect_to_chat_client(ChatRoom c)   throws RemoteException;
    public void newMessage(Message m)   throws RemoteException;

}

