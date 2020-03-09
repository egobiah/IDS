package SERVER;

import BD.ChatRoom;
import BD.Message;
import BD.Person;
import CLIENT.Chat;
import RMI_PACKAGE.TalkWithClient;
import RMI_PACKAGE.TalkWithServer;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class Talk_Implem implements TalkWithClient {
    ArrayList<ChatRoom> chatRooms;

    public Talk_Implem(){
        generateChatRoom();
    }

    @Override
    public String sendMessage(int chatRoomId, Message m) throws RemoteException {
        System.out.println("Recu le message " + m.toString());
        for(ChatRoom chatRoom : chatRooms){
            if(chatRoom.getID() == chatRoomId){
                chatRoom.getMsg().add(m);
                for(TalkWithServer t : chatRoom.getConnected()){
                    System.out.println("J'envoie au client");
                    t.newMessage(m);
                }
                return m.toString();
            }
        }
        return "Salle de chat ID #" + chatRoomId + "introuvable";
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

    @Override
    public Person loggedIn(String pseudo) {
        return new Person(pseudo, "null");
    }

    @Override
    public ArrayList<ChatRoom> getChatList(Person p) throws RemoteException {
        return chatRooms;
    }

    public ArrayList<Message> getMessageofChatRoom(int chatRoomId){
        for(ChatRoom chatRoom : chatRooms){
            if(chatRoom.getID() == chatRoomId){
                return chatRoom.getMsg();
            }
        }
        return null;
    }

    @Override
    public Boolean connectToChatRoom(ChatRoom chatRoom, TalkWithServer talkWithServer) {
        for(ChatRoom c : chatRooms){
            if(c.getID() == chatRoom.getID()){
                return c.addConnected(talkWithServer);
            }
        }
       return false;
    }

    @Override
    public Boolean diconnectToChatRoom(ChatRoom chatRoom, TalkWithServer talkWithServer) throws RemoteException {
        for(ChatRoom c : chatRooms){
            if(c.getID() == chatRoom.getID()){
                return c.rmConnected(talkWithServer);
            }
        }
        return false;
    }

    public void generateChatRoom(){
        Person user = new Person("Admin", "Password");
        chatRooms = new ArrayList<>();
      //  chatRooms.add( new ChatRoom("Room 1","JAVA",user));
       // chatRooms.add( new ChatRoom("Room 2","C",user));
       // chatRooms.add( new ChatRoom("Room 3","PHP",user));
       // chatRooms.add( new ChatRoom("Room 4","Python",user));
        chatRooms.add( new ChatRoom("Room 5","Rust",user));
    }
}
