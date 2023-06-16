package at.technikum.service;

import at.technikum.dto.Station;
import at.technikum.repository.Repository;
import at.technikum.util.JsonHelper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.core.io.NumberInput.parseInt;

public class DataCollectionDispatcherApp implements MessageHandler {
    private static final String INPUT_QUEUE_NAME = "data_collection_dispatcher_queue";
    private static final String OUTPUT_QUEUE_NAME_1 = "station_data_collector_queue";
    private static final String OUTPUT_QUEUE_NAME_2 = "data_collection_receiver_queue_1";

    private final MessagingQueue messagingQueue;

    private final Repository<Station> stationsRepository;

    public DataCollectionDispatcherApp(Repository<Station> repository) throws Exception {
        this.messagingQueue = new RabbitMQService(this);
        this.stationsRepository = repository;
    }

    @Override
    public void handleMessage(String message) throws Exception {
        String customerIdStr = JsonHelper.extractField(message, "customerId");

        if (isNumeric(customerIdStr)) {
            int customerId = Integer.parseInt(customerIdStr);
            if (customerId > 0) {
                startDataCollectionJob(customerId);
                return;
            }
        }

        // If the customerId is not set or not a valid number, log a message
        System.out.println("Invalid message: " + message);
    }

    public static boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        // https://stackoverflow.com/questions/15111420/how-to-check-if-a-string-contains-only-digits-in-java
        return str.matches("\\d+");
    }
    private void startDataCollectionJob(int customerId) throws Exception {
        sendToDataCollectionReceiver(customerId);
        Thread.sleep(1000);
        sendToStationDataCollector(customerId);
    }

    private void sendToStationDataCollector(int customerId) throws Exception {
        // Sends data to station data collector
        List<Station> stations = stationsRepository.getAllStations();

        for (Station station : stations) {
            Map<String, Object> messageObject = new HashMap<>();
            messageObject.put("customerId", customerId);
            messageObject.put("station", station);
            String message = JsonHelper.serialize(messageObject);
            System.out.println("Sending...");
            messagingQueue.publish(OUTPUT_QUEUE_NAME_1, message);
            Thread.sleep(1000);
        }
    }

    private void sendToDataCollectionReceiver(int customerId) throws Exception {
        // Create a JSON object for numMessages
        int numberOfMessages = stationsRepository.getNumberOfStations();
        Map<String, Object> messageObject = new HashMap<>();
        messageObject.put("customerId", customerId);
        messageObject.put("numMessages", numberOfMessages);
        String messageJson = JsonHelper.serialize(messageObject);
        System.out.println("Sending...");
        // Sends data to station data collector
        messagingQueue.publish(OUTPUT_QUEUE_NAME_2, messageJson);
    }

    public void run() {
        try {
            System.out.println("Data Collection Dispatcher App started");
            System.out.println("Waiting for messages in: " + INPUT_QUEUE_NAME);
            messagingQueue.consume(INPUT_QUEUE_NAME);
        } catch (Exception e) {
            System.out.println("Error consuming in "+ INPUT_QUEUE_NAME + ": " + e.getMessage());
        }
    }

}
