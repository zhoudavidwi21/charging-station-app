package at.technikum.service;

import at.technikum.repository.PostgreSQLDatabase;
import at.technikum.repository.StationsRepository;
import com.rabbitmq.client.*;

public class MessageConsumer {
    private static final String INPUT_QUEUE_NAME = "red_queue";
    private static final String OUTPUT_QUEUE_NAME_1 = "green_queue";
    private static final String OUTPUT_QUEUE_NAME_2 = "purple_queue";
    private static final String HOST = "localhost";
    private static final int PORT = 30003;

    ConnectionFactory factory = new ConnectionFactory();

    StationsRepository stationsRepository = new StationsRepository();


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
