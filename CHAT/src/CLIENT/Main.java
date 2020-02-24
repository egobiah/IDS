package CLIENT;

import BD.Person;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

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

        launch();
    }

    private void setActionButtonConnectionScreen(Stage stage){
            connectionScreen.getButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                user = new Person(connectionScreen.getPseudo(), "");
                accueil = new Accueil(user);
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
                chat = new Chat(accueil.getTableView().getSelectionModel().getSelectedItem(), user);
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