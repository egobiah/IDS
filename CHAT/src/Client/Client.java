package Client;

import App._Runnable;
import BD.Person;
import RmiPackage.TalkWithClient;
import RmiPackage.TalkWithServer;
import Server.ConfigManagerServices.ConfigManager;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;

import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import static javafx.application.Application.launch;

public class Client extends Application implements _Runnable {
    private static TalkWithClient talkWithClient;
    private static ConnectionScreen connectionScreen;
    private static Accueil accueil;
    private static Person user;
    private static TalkWithServer chat;

    /*public Client() {
        this.run();
    }*/

    public void start(Stage stage) {
        connectionScreen = new ConnectionScreen();
        setActionButtonConnectionScreen(stage);
        stage.setTitle("Chat Olivier - Julien");
        stage.setScene(connectionScreen.getScene());
        stage.show();
    }


    private void setActionButtonConnectionScreen(Stage stage){
        this.rmiConnect();
        connectionScreen.getButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                try {
                    user = talkWithClient.loggedIn(connectionScreen.getPseudo());
                } catch (RemoteException r){
                    r.printStackTrace();
                }
                accueil = new Accueil(user, talkWithClient);
                stage.setScene(accueil.getScene());
                setToConnectionScreenButton(stage);
                setToChatRoom(stage);

            }
        });
    }



    public void setToConnectionScreenButton(Stage stage){
        accueil.getToConnectionScreen().setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                stage.setScene(connectionScreen.getScene());

            }
        });

    }

    public void setToChatRoom(Stage stage){
        accueil.getToChatRoom().setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                System.out.println("Need to connect to chat room : "  + accueil.getTableView().getSelectionModel().getSelectedItem().getName());
                try {
                    chat = new Chat(accueil.getTableView().getSelectionModel().getSelectedItem(), user, talkWithClient);
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
                stage.setScene(((Chat) (chat)).getScene());

                ((Chat) (chat)).getToAccueil().setOnAction(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent e) {
                        stage.setScene(accueil.getScene());
                        try {
                            talkWithClient.diconnectToChatRoom(((Chat) chat).chatRoom, chat);
                        } catch (RemoteException ex) {
                            ex.printStackTrace();
                        }

                    }
                });


            }
        });


    }


    private void rmiConnect(){
        System.setProperty("java.security.policy", ConfigManager.getConfig("securityManagerProp"));
        if (System.getSecurityManager() == null) System.setSecurityManager ( new RMISecurityManager() );
        Registry registry = null;
        try {
            registry = LocateRegistry.getRegistry(connectionScreen.getUrl());
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }

        Remote remoteChat = null;
        try {
            remoteChat = registry.lookup(this.buildRmiAddr(ChatComponent, connectionScreen.getUrl()));
        } catch (RemoteException | NotBoundException ex) {
            ex.printStackTrace();
        }

        talkWithClient = (TalkWithClient) remoteChat;
        if(talkWithClient == null) System.exit(1);
    }

    @Override
    public int run() {
        launch();
        return 0;
    }

    @Override
    public void stop() {
        System.gc();
        System.runFinalization();
        System.exit(0);
    }
}
