package Server;
import Class.InformationsServeur;

import Configuration.RmqConfig;
import FX.Case;
import com.rabbitmq.client.*;
import javafx.beans.binding.ObjectExpression;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class Server {
    private String SERVER_NAME;
    private String RPC_INI_QUEUE_NAME;
    private ConnectionFactory factory;
    private Connection connection;
    private Channel initChannel;
    private Channel broadcast;
    private List<String> clients;
    private Channel channel;
    InformationsServeur informationsServeur;
    Object monitor;

    boolean initOk = false;



    public Server(String RPC_INI_QUEUE_NAME, String SERVER_NAME) throws IOException, TimeoutException {
        this.SERVER_NAME = SERVER_NAME;
        this.RPC_INI_QUEUE_NAME = RPC_INI_QUEUE_NAME;
        this.clients = new ArrayList<>();
        this.initConnection();

        connection.close();

    }


    private void initConnection() throws IOException, TimeoutException {
        this.factory = new ConnectionFactory();
        this.factory.setHost("localhost");
        this.connection = factory.newConnection();
        this.initConnectionFANOUT();
        this.initConnectionRPC();
        this.waitForAllServersReady();

    }

    private void initConnectionFANOUT() throws IOException, TimeoutException {

        this.broadcast = connection.createChannel();
        broadcast.exchangeDeclare("BROADCAST", "fanout");
        String queueName = broadcast.queueDeclare().getQueue();
        broadcast.queueBind(queueName, "BROADCAST", "");

        monitor = new Object();

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
            synchronized (monitor) {
                monitor.notify();
            }
            initOk = true;
        };
        broadcast.basicConsume(queueName, true, deliverCallback, consumerTag -> { });



    }

    private void initConnectionRPC() throws IOException, TimeoutException {




            try (RPC_INIT rpcInit = new RPC_INIT(this.connection, this.RPC_INI_QUEUE_NAME)) {
                System.out.println(" [x] Requesting Initialisation from manager");
                this.informationsServeur = rpcInit.call();
                System.out.println(" [.] Got IT");
            } catch (IOException | TimeoutException | InterruptedException e) {
                e.printStackTrace();
            }




    }

    private void waitForAllServersReady(){
        if (initOk == false) {

            synchronized (monitor) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // Do only two task
            }
        }
    }


    private void configureWorkingQueue() throws IOException {
        this.channel = connection.createChannel();
        this.channel.queueDeclare(this.SERVER_NAME, false, false, false, null);
        this.channel.queuePurge(this.SERVER_NAME);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                    .Builder()
                    .correlationId(delivery.getProperties().getCorrelationId())
                    .build();

            channel.basicPublish("", delivery.getProperties().getReplyTo(), replyProps, this.SERVER_NAME.getBytes());
            this.addClient(delivery.getProperties().getReplyTo());
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };
        channel.basicConsume(this.RPC_INI_QUEUE_NAME, false, deliverCallback, (consumerTag -> { }));

    }


    private void addClient(String clientQueu){
        if(!this.clients.contains(clientQueu))this.clients.add(clientQueu);
    }

    private String getServer(Case clientCase){
        return null;
    }

    public void contactServer(){
    }
}
