package RmiPackage;
import java.rmi.*;
import java.util.ArrayList;

import BD.*;

public interface TalkWithClient extends Remote {
    public String sendMessage(int chatRoomId,Message m)  throws RemoteException;
    public void test() throws RemoteException;
    public String sayHi(Person p) throws RemoteException;
    public Person loggedIn(String pseudo, String pwd) throws RemoteException;
    public ArrayList<ChatRoom> getChatList(Person p) throws RemoteException;
    public ArrayList<Message> getMessageofChatRoom(int chatRoomId) throws RemoteException;
    public Boolean connectToChatRoom(ChatRoom chatRoom, TalkWithServer talkWithServer)  throws RemoteException;
    public Boolean diconnectToChatRoom(ChatRoom chatRoom, TalkWithServer talkWithServer)  throws RemoteException;

}





