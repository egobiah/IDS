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
    private ArrayList<ChatRoom> chatRooms;
    private ArrayList<Person> users;

    public TalkService(String path) throws RemoteException {
        super();
        this.path = path;
        this.users = new ArrayList<>();
        this.chatRooms = new ArrayList<>();
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
    public Person loggedIn(String pseudo, String pwd) {
        Person plog = new Person(pseudo,pwd);
        for (Person p : this.users) if(p.equals(plog)) return p;

        return null;
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

    private void generateUsers(){
        this.users.add(new Person("julien", "123"));
        this.users.add(new Person("olivier", "123"));
    }

    private void generateChatRoom(){
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
    void boot() throws InterruptedException, ClassNotFoundException, IOException {
        System.out.print("Booting ");


        ObjectInputStream inputStream;
        try {
            inputStream = new ObjectInputStream(new FileInputStream(path + "/chatRooms")); //load chatRoom
            this.chatRooms = (ArrayList<ChatRoom>) inputStream.readObject();
            inputStream.close();

        } catch (IOException e) { // catch files not found exceptions
            generateChatRoom();
            System.out.println("/!\\ chatRooms save files not found");
        }

        try {
            inputStream = new ObjectInputStream(new FileInputStream(path + "/users")); //load chatRoom
            this.users = (ArrayList<Person>) inputStream.readObject();
            inputStream.close();

        } catch (IOException e) { // catch files not found exceptions
            generateUsers();
            System.out.println("/!\\ users save files not found");
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
    void powerOff() throws InterruptedException, IOException {
        System.out.print("PowerOff ");

        ObjectOutputStream outputStream;
        outputStream = new ObjectOutputStream(new FileOutputStream(path + "/chatRooms"));
        outputStream.writeObject(this.chatRooms);

        outputStream = new ObjectOutputStream(new FileOutputStream(path + "/users"));
        outputStream.writeObject(this.users);

        outputStream.close();

        for (int i = 0; i < 10; i++) {
            System.out.print(".");
            Thread.sleep(100);
        }
        Thread.sleep(100);
        System.out.println("GoodBye");
    }
}