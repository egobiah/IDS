package CLIENT;

import BD.Person;

import SERVER.TalkWithClient;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main extends Application {

    private static TalkWithClient t;
    ConnectionScreen connectionScreen;
    Accueil accueil;
    Person user;
    Chat chat;

    public void start(Stage stage) {

        connectionScreen = new ConnectionScreen();

        setActionButtonConnectionScreen(stage);




        stage.setTitle("Chat Olivier - Julien");
        stage.setScene(connectionScreen.getScene());
        stage.show();
    }

    public static void main(String[] args) {

        try {
            if (args.length < 1) {
                System.out.println("Usage: java HelloClient <rmiregistry host>");
                return;
            }

            String host = args[0];

            // Get remote object reference
            Registry registry = LocateRegistry.getRegistry(host);
            t = (TalkWithClient) registry.lookup("Talk");

        }  catch (Exception e)  {
        System.err.println("Error on client: " + e);
        }
            launch();
    }

    private void setActionButtonConnectionScreen(Stage stage){
            connectionScreen.getButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
             //   user = t.loggedIn(connectionScreen.getPseudo());
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
                chat = new Chat(accueil.getTableView().getSelectionModel().getSelectedItem(), user, t);
                stage.setScene(chat.getScene());

                chat.getToAccueil().setOnAction(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent e) {
                        stage.setScene(accueil.getScene());

                    }
                });


            }
        });


    }


}