package Client;

import App._Runnable;
import BD.Person;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import javafx.application.Application;
import javafx.scene.control.Label;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


public class Client extends Application implements _Runnable {
    private static final String EXCHANGE_NAME = "CLIENT";

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

    public void start(Stage stage) throws IOException, TimeoutException {
        connectionScreen = new ConnectionScreen();
        setActionButtonConnectionScreen(stage);
        stage.setTitle("Chat Olivier - Julien");
        stage.setScene(connectionScreen.getScene());
        stage.show();
        this.boot = false;
    }


    private void setActionButtonConnectionScreen(Stage stage){
        connectionScreen.getButton().setOnAction(e -> {
            /*if(!this.boot)this.rmiConnect();
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
            }*/
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
