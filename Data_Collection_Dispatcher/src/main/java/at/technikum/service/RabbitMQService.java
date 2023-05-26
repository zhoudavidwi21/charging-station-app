package at.technikum.service;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeoutException;

public class RabbitMQService implements MessagingQueue {

    ConnectionFactory factory = new ConnectionFactory();

    public RabbitMQService(String host, int port) {
        factory.setHost(host);
        factory.setPort(port);
    }


    @Override
    public void consumeAndPublish(String inputQueue, String outputQueue1, String outputQueue2) throws IOException, TimeoutException {
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel();){

            channel.queueDeclare(inputQueue, false, false, false, null);
            System.out.println(" [*] Waiting to start data collection. To exit press CTRL+C");

            // callback for received messages
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                System.out.println(" [x] Received '" + new String(delivery.getBody(), "UTF-8") + "' at " + LocalDateTime.now() + " ms");
                System.out.println(" [x] Starting data collection...");
                String message; // data collection output
                System.out.println(" [x] Data collection finished. Sending data to queue: " + outputQueue1);
                publish(message, outputQueue1);


            };


            // start listening for messages
            channel.basicConsume(inputQueue, true, deliverCallback, consumerTag -> { });
        }


    }

    private void publish(String message, String queueName) {
        try (
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel()
                ){
            channel.queueDeclare(queueName, false, false, false, null);
            System.out.println(" [x] Sending message: " + message + " to queue: " + queueName);
            channel.basicPublish("", queueName, null, message.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
