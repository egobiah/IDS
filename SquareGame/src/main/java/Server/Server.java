package Server;
import Class.InformationsServeur;

import Configuration.RmqConfig;
import FX.Case;
import com.rabbitmq.client.*;

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
    private List<String> clients;
    private Channel channel;
    InformationsServeur informationsServeur;



    public Server(String RPC_INI_QUEUE_NAME, String SERVER_NAME) throws IOException, TimeoutException {
        this.SERVER_NAME = SERVER_NAME;
        this.RPC_INI_QUEUE_NAME = RPC_INI_QUEUE_NAME;
        this.clients = new ArrayList<>();
        this.initConnection();
    }
/*
    private void initConnection() throws IOException, TimeoutException {
        this.factory = new ConnectionFactory();
        this.factory.setHost(RmqConfig.RMQ_SERVER_IP);

        this.connection = factory.newConnection();
        connection.openChannel();
        this.initChannel = connection.createChannel();
        this.initChannel.queueDeclare(this.RPC_INI_QUEUE_NAME, false, false, false, null);
        this.initChannel.queuePurge(this.RPC_INI_QUEUE_NAME);

        DeliverCallback initDeliverCallback = (consumerTag, delivery) -> {
            AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                    .Builder()
                    .correlationId(delivery.getProperties().getCorrelationId())
                    .build();

            initChannel.basicPublish("", delivery.getProperties().getReplyTo(), replyProps, this.SERVER_NAME.getBytes());
            this.addClient(delivery.getProperties().getReplyTo());
            initChannel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };
        initChannel.basicConsume(this.RPC_INI_QUEUE_NAME, false, initDeliverCallback, (consumerTag -> { }));
    }
*/
    private void initConnection() throws IOException, TimeoutException {
        try (RPC_INIT rpcInit = new RPC_INIT()) {
            System.out.println(" [x] Requesting Initialisation from manager");
            informationsServeur = rpcInit.call();
            System.out.println(" [.] Got IT");

        } catch (IOException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
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
