package Client;

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

import static App._Runnable.ChatComponent;

public class Main extends Application {

    private static TalkWithClient t;
    ConnectionScreen connectionScreen;
    Accueil accueil;
    Person user;
    TalkWithServer chat;


    public void start(Stage stage) {
        connectionScreen = new ConnectionScreen();
        setActionButtonConnectionScreen(stage);
        stage.setTitle("Chat Olivier - Julien");
        stage.setScene(connectionScreen.getScene());
        stage.show();
    }

    public static void main(String[] args) {
            launch();
    }

    private void setActionButtonConnectionScreen(Stage stage){
            connectionScreen.getButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {

                //try {

                    // Get remote object reference
                    System.setProperty("java.security.policy", ConfigManager.getConfig("securityManagerProp"));
                    if (System.getSecurityManager() == null) System.setSecurityManager ( new RMISecurityManager() );
                Registry registry = null;
                try {
                    registry = LocateRegistry.getRegistry(connectionScreen.getUrl());
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }

                Remote remoteUserManager = null;
                try {
                    remoteUserManager = registry.lookup("rmi://" + connectionScreen.getUrl() + "/"+ ChatComponent);
                } catch (RemoteException | NotBoundException ex) {
                    ex.printStackTrace();
                }

                t = (TalkWithClient) remoteUserManager;

                try {
                    user = t.loggedIn(connectionScreen.getPseudo());
                } catch (RemoteException r){
                    r.printStackTrace();
                }
                accueil = new Accueil(user,t);
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
                    chat = new Chat(accueil.getTableView().getSelectionModel().getSelectedItem(), user, t);
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
                stage.setScene(((Chat) (chat)).getScene());

                ((Chat) (chat)).getToAccueil().setOnAction(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent e) {
                        stage.setScene(accueil.getScene());
                        try {
                            t.diconnectToChatRoom(((Chat) chat).chatRoom, chat);
                        } catch (RemoteException ex) {
                            ex.printStackTrace();
                        }

                    }
                });


            }
        });


    }


}