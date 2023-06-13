package at.technikum.service;

import at.technikum.dto.Station;
import at.technikum.repository.Repository;
import at.technikum.util.JsonHelper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataCollectionDispatcherApp implements MessageHandler {
    private static final String INPUT_QUEUE_NAME = "red_queue";
    private static final String OUTPUT_QUEUE_NAME_1 = "green_queue";
    private static final String OUTPUT_QUEUE_NAME_2 = "purple_queue";

    private MessagingQueue messagingQueue;

    private Repository<Station> stationRepository;

    public DataCollectionDispatcherApp(Repository<Station> repository) throws Exception {
        this.messagingQueue = new RabbitMQService(this);
        this.stationRepository = repository;
    }

    @Override
    public void handleMessage(String message) throws Exception {
        // Custom logic to handle the received message
        // TODO: dependent on format
        // if message is a number, send to data collection receiver
        startDataCollectionJob();
    }

    private void startDataCollectionJob() throws Exception {
        sendToStationDataCollector();
        sendToDataCollectionReceiver();
    }

    private void sendToStationDataCollector() throws Exception {
        // Sends data to station data collector
        List<Station> stations = stationRepository.getAllStations();
        for (Station station : stations) {
            String message = JsonHelper.toJson(station);
            messagingQueue.publish(OUTPUT_QUEUE_NAME_1, message);
        }
    }

    private void sendToDataCollectionReceiver() throws Exception {
        // Create a JSON object for numMessages
        int numberOfMessages = stationRepository.getNumberOfStations();
        Map<String, Object> numberOfMessagesObject = new HashMap<>();
        numberOfMessagesObject.put("numMessages", numberOfMessages);
        String numberOfMessagesJson = JsonHelper.toJson(numberOfMessagesObject);

        // Sends data to station data collector
        messagingQueue.publish(OUTPUT_QUEUE_NAME_2, numberOfMessagesJson);
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
