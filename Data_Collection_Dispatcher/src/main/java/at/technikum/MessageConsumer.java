package at.technikum;

import com.rabbitmq.client.*;

import java.io.IOException;

public class MessageConsumer {
    private static final String INPUT_QUEUE_NAME = "red_queue";
    private static final String OUTPUT_QUEUE_NAME_1 = "green_queue";
    private static final String OUTPUT_QUEUE_NAME_2 = "purple_queue";
    private static final String HOST = "localhost";
    private static final int PORT = 30003;

    ConnectionFactory factory = new ConnectionFactory();

    StationsRepository stationsRepository = new StationsRepository();

    public void consumeAndPublish()  {
        factory.setHost(HOST);
        factory.setPort(PORT);

        try (
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel()
                )
        {
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body, "UTF-8");
                    System.out.println("Received job to collect station data");
                    System.out.println("Message: " + message);
                    System.out.println("----------------------------------------------");

                    // get station data from repository
                    String responseMessage = stationsRepository.getAllStations().toString();
                    System.out.println("Sending response: " + responseMessage);
                    System.out.println("----------------------------------------------");

                    channel.basicPublish("", OUTPUT_QUEUE_NAME_1, null, responseMessage.getBytes("UTF-8"));
                    channel.basicPublish("", OUTPUT_QUEUE_NAME_2, null, responseMessage.getBytes("UTF-8"));
                    System.out.println("Sent to: " + OUTPUT_QUEUE_NAME_1 + " and " + OUTPUT_QUEUE_NAME_2);
                }
            };

            channel.basicConsume(INPUT_QUEUE_NAME, true, consumer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //TODO: implement extract customer id
    // check message format
    // if message format is correct, extract customer id
    // if message format is incorrect, return null
    private String extractMessage(String message) {
        String prefix = message.substring(0, message.indexOf(":"));

        String messageWithoutPrefix = message.substring(message.indexOf(":") + 1);

        return prefix + ": " + messageWithoutPrefix;
    }

    private String customerIdToJson(String customerId) {
        return "{\"customerId\": \"" + customerId + "\"}";
    }
}
