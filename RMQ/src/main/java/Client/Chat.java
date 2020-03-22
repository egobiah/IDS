package Client;
import BD.*;
import com.rabbitmq.client.*;
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

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class Chat extends UnicastRemoteObject {
    ChatRoom chatRoom;
    Person user;
    Scene scene;
    BorderPane root;
    ScrollPane scrollPane;
    Button toAccueil;
    Button send;
    TextField message;
    ArrayList<Message> msgs;
    private Channel channel;
    private String queueName;

    private static final String EXCHANGE_NAME = "CLIENT";

    public Chat(ChatRoom chatRoom, Person user, Channel channel, String queueName) throws IOException, TimeoutException {

        this.channel = channel;
        this.chatRoom = chatRoom;
        this.user = user;
        this.queueName = queueName;
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

        //getAllMessage();
        send.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                try {
                    sendMSG();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        message.setOnKeyPressed(k -> {
            if (k.getCode().equals(KeyCode.ENTER)){
                try {
                    sendMSG();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");

            try {
                this.newMessage((Message)Chat.deserialize(delivery.getBody()));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


        };

        channel.basicConsume(this.queueName, true, deliverCallback, consumerTag -> { });


        /*try {
            t.connectToChatRoom(chatRoom, (TalkWithServer) this);
        } catch (RemoteException e) {
            e.printStackTrace();
        }*/

        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                refreshMSG();
            }
        });

    }

    private void sendMSG() throws IOException {
        try {

            channel.basicPublish(
                    EXCHANGE_NAME,
                    "chat." + chatRoom.getName(),
                    null,
                    Chat.serialize(new Message(this.message.getText(),this.user)));
        } catch (RemoteException r){

            System.out.println("Problème lors de l'envoi du message\n" + r);
            r.printStackTrace();
        }
        message.clear();
    }

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

   /* void getAllMessage(){
        try {
            msgs = t.getMessageofChéatRoom(chatRoom.getID());
        } catch (RemoteException r){
            r.printStackTrace();
            System.out.println("Impossible de récupérer les messages de cette salle de chat");
            return;
        }
    }*/

    public void connect_to_chat_client(ChatRoom c) {
        System.out.println("Connect to " + c.toString());
    }

    public void newMessage(Message message) {
        System.out.println("Serveur m'as dis que  " + message.toString());
        msgs.add(msgs.size(), message);
        Platform.runLater(() -> refreshMSG());
    }

    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }
    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }
}
