package Server;

import BD.ChatRoom;
import BD.Message;
import BD.Person;
import Client.Chat;
import RmiPackage.TalkWithClient;
import RmiPackage.TalkWithServer;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class TalkService extends UnicastRemoteObject implements TalkWithClient, Serializable, _TalkService {
    private String path;
    ArrayList<ChatRoom> chatRooms;

    public TalkService(String path) throws RemoteException {
        super();
        this.path = path;
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

    /**
     * This method allow Chat server to boot
     * <p>
     * Load database from Saving File
     *
     * @throws InterruptedException
     * @throws ClassNotFoundException if doesn't find class in classpath
     * @throws IOException            if file not found, print : "/!\ save files not found"
     */
    public void boot() throws InterruptedException, ClassNotFoundException, IOException {
        System.out.print("Booting ");

        ObjectInputStream inputStream;
        try {
            inputStream = new ObjectInputStream(new FileInputStream(path)); //load chatRoom
            this.chatRooms = (ArrayList<ChatRoom>) inputStream.readObject();
            inputStream.close();

        } catch (IOException e) { // catch files not found exception
            System.out.println("/!\\ save files not found");
        }

        for (int i = 0; i < 10; i++) {
            System.out.print(".");
            Thread.sleep(100);
        }

        Thread.sleep(100);
        System.out.println("TADA");
        System.out.println("Welcome to Chat Server (More efficiency than Skype) !");
    }

    /**
     * Power off Chat server
     * <p>
     * Save all data in file
     *
     * @throws InterruptedException
     * @throws IOException          if file not found, print : "/!\ save files not found"
     */
    public void powerOff() throws InterruptedException, IOException {
        System.out.print("PowerOff ");

        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(path)); //save films
        outputStream.writeObject(this.chatRooms);
        outputStream.close();

        for (int i = 0; i < 10; i++) {
            System.out.print(".");
            Thread.sleep(100);
        }
        Thread.sleep(100);
        System.out.println("GoodBye");
    }
}