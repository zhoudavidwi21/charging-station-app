package at.technikum.service;

import at.technikum.dto.Customer;
import at.technikum.repository.Repository;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class DataCollectionReceiverApp implements MessageHandler {

    private static final String INPUT_QUEUE_NAME = "data_collection_receiver_queue";
    private static final String OUTPUT_QUEUE_NAME = "pdf_generator_queue

    private final MessagingQueue messagingQueue;

    private final Repository<Customer> stationsRepository;

    public DataCollectionReceiverApp(Repository<Customer> stationsRepository) throws IOException, TimeoutException {
        this.messagingQueue = new RabbitMQService(this);;
        this.stationsRepository = stationsRepository;
    }

    @Override
    public void handleMessage(String message) throws Exception {

    }
}
