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
    private Channel information;
    private Channel broadcast;
    private Channel newClient;
    private Channel work;

    String queueCom;


    InformationsServeur informationsServeur;
    Object monitor;

    Object soloClient;

    final String TASK_QUEUE_NAME = "task_queue";
    final String NEW_CLIENT_QUEUE = "new_client";

    boolean initOk = false;



    public Server(String RPC_INI_QUEUE_NAME, String SERVER_NAME) throws IOException, TimeoutException {
        this.SERVER_NAME = SERVER_NAME;
        this.RPC_INI_QUEUE_NAME = RPC_INI_QUEUE_NAME;
        this.initConnection();

       // connection.close();

    }

    private void initWork() throws IOException, TimeoutException{
        work = connection.createChannel();
        queueCom = work.queueDeclare().getQueue();
        System.out.println("queue declare" + queueCom);
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");

            System.out.println(" [x] Client talk to me '" + message + "'");
            try {
                Thread.sleep(1000);
                //doWork(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                work.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };
        work.basicConsume(queueCom, false, deliverCallback, consumerTag -> { });


    }
    private void initCommunication() throws IOException, TimeoutException{
        information = connection.createChannel();
        information.queueDeclare(NEW_CLIENT_QUEUE, false, false, false, null);
        information.queuePurge(NEW_CLIENT_QUEUE);

        information.basicQos(1);
        System.out.println(" [x] Awaiting Client request");

        soloClient = new Object();
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                    .Builder()
                    .correlationId(delivery.getProperties().getCorrelationId())
                    .build();

            String response = "";

            try {

                response = queueCom;
            } catch (RuntimeException e) {
                System.out.println(" [.] " + e.toString());
            } finally {
                System.out.println("New client there");
                information.basicPublish("", delivery.getProperties().getReplyTo(), replyProps, response.getBytes("UTF-8"));
                information.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                // RabbitMq consumer worker thread notifies the RPC server owner thread
                synchronized (soloClient) {
                    soloClient.notify();
                }

            }
        };

        information.basicConsume(NEW_CLIENT_QUEUE, false, deliverCallback, (consumerTag -> { }));
    }
    private void initNewClient() throws IOException, TimeoutException{
        this.newClient = connection.createChannel();

        newClient.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
        newClient.basicQos(1);
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");

            System.out.println(" [x] Received '" + message + "'");
            try {
                Thread.sleep(1000);
                //doWork(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println(" [x] Done");
                newClient.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };
        newClient.basicConsume(TASK_QUEUE_NAME, false, deliverCallback, consumerTag -> { });
    }
    private void initConnection() throws IOException, TimeoutException {
        this.factory = new ConnectionFactory();
        this.factory.setHost("localhost");
        this.connection = factory.newConnection();
        initWork();
        this.initCommunication();
        this.initConnectionFANOUT();
        this.initConnectionRPC();
        this.initNewClient();
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



}
