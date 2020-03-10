package Client;
import BD.*;
import RmiPackage.TalkWithClient;
import RmiPackage.TalkWithServer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Chat extends UnicastRemoteObject implements TalkWithServer {
    ChatRoom chatRoom;
    Person user;
    Scene scene;
    BorderPane root;
    ScrollPane scrollPane;
    Button toAccueil;
    Button send;
    TextField message;
    TalkWithClient t;
    ArrayList<Message> msgs;

    public Chat(ChatRoom chatRoom, Person user, TalkWithClient t) throws RemoteException{
        this.t = t;
        this.chatRoom = chatRoom;
        this.user = user;
        root = new BorderPane();
        VBox topBox = new VBox();
        toAccueil = new Button("Retour");
        Text roomDef = new Text("Salle : " + chatRoom.toString());
        Text userDef = new Text("Utilisateur : " + user.toString());

        topBox.getChildren().addAll(toAccueil, roomDef, userDef);

        scrollPane = new ScrollPane();

        BorderPane bottomBox = new BorderPane();
        message = new TextField();
        send = new Button("Envoyer");
        bottomBox.setCenter(message);
        bottomBox.setRight(send);

        root.setTop(topBox);
        root.setCenter(scrollPane);
        root.setBottom(bottomBox);
        scene = new Scene(root, 400,400);

        getAllMessage();
        send.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
               sendMSG();
            }
        });

        message.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent k)
            {
                if (k.getCode().equals(KeyCode.ENTER)){
                    sendMSG();
                }
            }
        });

        try {
            t.connectToChatRoom(chatRoom, (TalkWithServer) this);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                refreshMSG();
            }
        });

    }

    private void sendMSG()  {
        try {
            //t.test();
            //System.out.println(t.sayHi(user));
            t.sendMessage(chatRoom.getID(),new Message(message.getText(), user));
        } catch (RemoteException r){

            System.out.println("Problème lors de l'envoi du message\n" + r);
            r.printStackTrace();
        }

       // System.out.println(message.getText());
        chatRoom.getMsg().add(new Message(message.getText(), user));
        message.clear();

       // refreshMSG();
    };

    public Button getToAccueil() {
        return toAccueil;
    }

    public Scene getScene() {
        return scene;
    }

    public void refreshMSG(){
        BorderPane mainB = new BorderPane();
        VBox v = new VBox();
        for(Message m : msgs){
           Text t =  new Text(m.toString());
                   if(m.getOwner().getPseudo().equals(user.getPseudo())){
                       t.setFill(Color.BLUE);


                   }
            v.getChildren().add(t);
        }

        scrollPane.setContent(v);
        scrollPane.setVvalue(1.00);


    }

    void getAllMessage(){
        try {
            msgs = t.getMessageofChatRoom(chatRoom.getID());
        } catch (RemoteException r){
            r.printStackTrace();
            System.out.println("Impossible de récupérer les messages de cette salle de chat");
            return;
        }
    }

    @Override
    public void connect_to_chat_client(ChatRoom c) {
        System.out.println("Connect to " + c.toString());
    }

    @Override
    public void newMessage(Message message) {
        System.out.println("Serveur m'as dis que  " + message.toString());
        msgs.add(msgs.size(), message);
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                refreshMSG();
            }
          });
    }
}
