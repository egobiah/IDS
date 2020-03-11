package Client;

import App._Runnable;
import BD.Person;
import RmiPackage.TalkWithClient;
import RmiPackage.TalkWithServer;
import Server.ConfigManagerServices.ConfigManager;
import javafx.application.Application;
import javafx.scene.control.Label;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class Client extends Application implements _Runnable {
    private static TalkWithClient talkWithClient;
    private static ConnectionScreen connectionScreen;
    private static Accueil accueil;
    private static Person user;
    private static TalkWithServer chat;
    private boolean boot;

    public Client() {
        this.boot = true;
    }

    public void start(Stage stage) {
        connectionScreen = new ConnectionScreen();
        setActionButtonConnectionScreen(stage);
        stage.setTitle("Chat Olivier - Julien");
        stage.setScene(connectionScreen.getScene());
        stage.show();
        this.boot = false;
    }


    private void setActionButtonConnectionScreen(Stage stage){
        //if(!this.boot)this.rmiConnect();
        connectionScreen.getButton().setOnAction(e -> {
            if(!this.boot)this.rmiConnect();
            try {
                user = talkWithClient.loggedIn(connectionScreen.getPseudo(), connectionScreen.getPassword());
            } catch (RemoteException r){
                r.printStackTrace();
            }
            if(user == null){
                Label label = new Label("Unknown account");
                Popup popup = new Popup();

                // set background
                label.setStyle(
                        "-fx-border-color: black;" +
                        "-fx-border-width: 2px;" +
                        "-fx-border-width: 2px;" +
                        "-fx-background-color: #f3edff;" +
                        "-fx-background-color: -fx-box-border, -fx-control-inner-background;"+
                        "-fx-background-insets: 0, 1;" +
                        "-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 8, 0.0 , 0 , 0);");


                // add the label
                popup.getContent().add(label);

                popup.setAutoHide(true);

                // set size of label
                label.setMinWidth(200);
                label.setMinHeight(100);

                popup.show(stage);
                return;
            }
            accueil = new Accueil(user, talkWithClient);
            stage.setScene(accueil.getScene());
            setToConnectionScreenButton(stage);
            setToChatRoom(stage);

        });
    }



    public void setToConnectionScreenButton(Stage stage){
        accueil.getToConnectionScreen().setOnAction(e -> stage.setScene(connectionScreen.getScene()));

    }

    public void setToChatRoom(Stage stage){
        accueil.getToChatRoom().setOnAction(e -> {
            System.out.println("Need to connect to chat room : "  + accueil.getTableView().getSelectionModel().getSelectedItem().getName());
            try {
                chat = new Chat(accueil.getTableView().getSelectionModel().getSelectedItem(), user, talkWithClient);
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
            stage.setScene(((Chat) (chat)).getScene());

            ((Chat) (chat)).getToAccueil().setOnAction(e1 -> {
                stage.setScene(accueil.getScene());
                try {
                    talkWithClient.diconnectToChatRoom(((Chat) chat).chatRoom, chat);
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }

            });


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
