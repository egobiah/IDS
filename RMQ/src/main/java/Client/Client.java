package Client;

import App._Runnable;
import BD.Person;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


public class Client extends Application implements _Runnable {
    private static final String EXCHANGE_NAME = "CHAT_EXCHANGE";

    private static ConnectionScreen connectionScreen;
    private static Accueil accueil;
    private static Person user;
    private boolean boot;
    private ConnectionFactory factory;
    private Connection connection;
    public Channel channel;
    private String queueName;
    private Chat chat;

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
        connectionScreen.getButton().setOnAction(e -> {
            user =  new Person(connectionScreen.getPseudo(), connectionScreen.getPassword());
            accueil = new Accueil(user);
            stage.setScene(accueil.getScene());
            setToConnectionScreenButton(stage);
            setToChatRoom(stage);
            this.factory = new ConnectionFactory();
            this.factory.setHost(connectionScreen.getUrl());

            try {
                this.connection = factory.newConnection();
                this.channel = this.connection.createChannel();
                this.channel.exchangeDeclare(EXCHANGE_NAME, "topic");
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (TimeoutException ex) {
                ex.printStackTrace();
            }

        });
    }



    public void setToConnectionScreenButton(Stage stage)  {
        accueil.getToConnectionScreen().setOnAction(e -> stage.setScene(connectionScreen.getScene()));

    }

    public void setToChatRoom(Stage stage){
        accueil.getToChatRoom().setOnAction(e -> {
            System.out.println("Need to connect to chat room : "  + accueil.getTableView().getSelectionModel().getSelectedItem().getName());

            try {
                this.queueName = this.channel.queueDeclare().getQueue();
                this.channel.queueBind(queueName, EXCHANGE_NAME, "chat."+ accueil.getTableView().getSelectionModel().getSelectedItem().getName());
                chat = new Chat(accueil.getTableView().getSelectionModel().getSelectedItem(), user, this.channel, this.queueName);
            } catch (IOException | TimeoutException ex) {
                ex.printStackTrace();
            }

        stage.setScene(((chat)).getScene());

            ((chat)).getToAccueil().setOnAction(e1 -> {
                stage.setScene(accueil.getScene());
                try {
                    this.channel.queueUnbind(queueName, EXCHANGE_NAME, "");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
        });
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
