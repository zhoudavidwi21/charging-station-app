package at.technikum.service;

import at.technikum.dto.Customer;
import at.technikum.repository.Repository;
import at.technikum.util.JsonHelper;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeoutException;

public class DataCollectionReceiverApp implements MessageHandler {

    private static final String INPUT_QUEUE_NAME_1 = "data_collection_receiver_queue_1";

    private final MessagingQueue messagingQueue;
    private final DataCollectionReceiverApp2 dataCollectionReceiverApp2;

    public DataCollectionReceiverApp(Repository<Customer> customerRepository) throws IOException, TimeoutException {
        this.messagingQueue = new RabbitMQService(this);
        this.dataCollectionReceiverApp2 = new DataCollectionReceiverApp2(customerRepository);
    }

    @Override
    public void handleMessage(String message) throws Exception {
        Map<String, Object> messageMap = JsonHelper.deserialize(message);
        int numOfMessages = (int) messageMap.get("numMessages");
        int customerId = (int) messageMap.get("customerId");

        dataCollectionReceiverApp2.listenToStationDataMessages(numOfMessages, customerId);
    }

    public void run() {
        try {
            System.out.println("Data Collection Receiver App 1 started");
            System.out.println("Waiting for messages in: " + INPUT_QUEUE_NAME_1);
            messagingQueue.consume(INPUT_QUEUE_NAME_1);
        } catch (Exception e) {
            System.out.println("Error consuming in "+ INPUT_QUEUE_NAME_1 + ": " + e.getMessage());
        }
    }
}
