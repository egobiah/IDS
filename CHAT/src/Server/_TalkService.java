package Server;

import BD.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface _TalkService extends Remote{
    public String sendMessage(int chatRoomId, Message m) throws RemoteException;
}