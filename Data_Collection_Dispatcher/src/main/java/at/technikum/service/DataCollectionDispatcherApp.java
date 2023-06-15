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
    private static final String OUTPUT_QUEUE_NAME_2 = "data_collection_receiver_queue";

    private final MessagingQueue messagingQueue;

    private final Repository<Station> stationsRepository;

    public DataCollectionDispatcherApp(Repository<Station> repository) throws Exception {
        this.messagingQueue = new RabbitMQService(this);
        this.stationsRepository = repository;
    }

    @Override
    public void handleMessage(String message) throws Exception {
        Map<String, Object> messageObject = JsonHelper.fromJson(message, new TypeReference<>() {});
        Object customerIdObj = messageObject.get("customerId");

        if (customerIdObj instanceof String customerIdStr) {

            if (isNumeric(customerIdStr)) {
                int customerId = Integer.parseInt(customerIdStr);
                if (customerId > 0) {
                    startDataCollectionJob(customerId);
                    return; // Exit the method since data collection job is started
                }
            }
        }

        // If the customerId is not set or not a valid number, log a message
        System.out.println("Invalid message: " + message);
    }

    private boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }
    private void startDataCollectionJob(int customerId) throws Exception {
        sendToStationDataCollector(customerId);
        sendToDataCollectionReceiver(customerId);
    }

    private void sendToStationDataCollector(int customerId) throws Exception {
        // Sends data to station data collector
        List<Station> stations = stationsRepository.getAllStations();

        for (Station station : stations) {
            Map<String, Object> messageObject = new HashMap<>();
            messageObject.put("customerId", customerId);
            messageObject.put("station", station);
            String message = JsonHelper.toJson(messageObject);
            messagingQueue.publish(OUTPUT_QUEUE_NAME_1, message);
        }
    }

    private void sendToDataCollectionReceiver(int customerId) throws Exception {
        // Create a JSON object for numMessages
        int numberOfMessages = stationsRepository.getNumberOfStations();
        Map<String, Object> messageObject = new HashMap<>();
        messageObject.put("customerId", customerId);
        messageObject.put("numMessages", numberOfMessages);
        String messageJson = JsonHelper.toJson(messageObject);

        // Sends data to station data collector
        messagingQueue.publish(OUTPUT_QUEUE_NAME_2, messageJson);
    }

    public void run() {
        try {
            System.out.println("App started");
            System.out.println("Waiting for messages in: " + INPUT_QUEUE_NAME);
            messagingQueue.consume(INPUT_QUEUE_NAME);
        } catch (Exception e) {
            System.out.println("Error consuming data collection queue: " + e.getMessage());
        }
    }

}
