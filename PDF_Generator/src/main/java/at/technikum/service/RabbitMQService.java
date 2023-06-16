package at.technikum.service;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

// basic publish/consume source: https://www.rabbitmq.com/tutorials/tutorial-one-java.html
public class RabbitMQService implements MessagingQueue, AutoCloseable {
    private static final String HOST = "localhost";
    private static final int PORT = 30003;
    private Connection connection;
    private Channel channel;
    private static final String CONTENT_TYPE_JSON = "application/json";
    private MessageHandler messageHandler;

    public RabbitMQService(MessageHandler messageHandler) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        factory.setPort(PORT);

        connection = factory.newConnection();
        channel = connection.createChannel();
        this.messageHandler = messageHandler;
    }

    @Override
    public void publish(String queueName, String message) throws IOException {
        channel.queueDeclare(queueName, false, false, false, null);
        channel.basicPublish("", queueName, new AMQP.BasicProperties.Builder().contentType(CONTENT_TYPE_JSON).build(),
                message.getBytes());
        System.out.println("Sent \""+ message + "\" to queue: " + queueName);
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("Waiting for messages. To exit press CTRL+C");
    }

    @Override
    public void consume(String queueName) throws IOException {
        channel.queueDeclare(queueName, false, false, false, null);

        // Create a consumer to handle incoming messages
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Received message from queue " + queueName + ": " + message);
                // Delegate message handling to the message handler
                try {
                    messageHandler.handleMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        // Start consuming messages from the queue
        channel.basicConsume(queueName, true, consumer);
    }

    @Override
    public void close() throws IOException, TimeoutException {
        channel.close();
        connection.close();
    }
}
