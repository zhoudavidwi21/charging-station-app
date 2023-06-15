package at.technikum.service;

import at.technikum.dto.Station;
import at.technikum.repository.CustomerStationDataRepository;
import at.technikum.repository.Database;
import at.technikum.repository.Repository;
import at.technikum.util.JsonHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import at.technikum.util.DatabaseFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

public class StationDataCollectionApp implements MessageHandler {

    private static final String INPUT_QUEUE_NAME = "station_data_collector_queue";
    private static final String OUTPUT_QUEUE_NAME = "data_collection_receiver_queue";

    private final MessagingQueue messagingQueue;

    private Repository<Double> customerStationDataRepository;
    private ObjectMapper objectMapper;

    public StationDataCollectionApp() throws IOException, TimeoutException {
        this.messagingQueue = new RabbitMQService(this);
        this.customerStationDataRepository = null;
        this.objectMapper = createObjectMapper();
    }

    private ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    @Override
    public void handleMessage(String jsonMessage) throws Exception {
        // Extract the customerId
        int customerId = extractCustomerId(jsonMessage);
        // Extract the station information and instantiate the Station object
        Station station = extractStation(jsonMessage);

        // Use the db_url to instantiate the appropriate Database
        try (Database database = DatabaseFactory.createPostgresqlDatabase(station.getDb_url())) {
            setCustomerStationDataRepository(new CustomerStationDataRepository(database));
            // Get the kwh by customerId
            List<Double> customerStationData = customerStationDataRepository.getKwhByCustomerId(customerId);

            String jsonResponseString = generateJsonResponse(customerId, station.getId(), customerStationData);

            sendToDataCollectionReceiver(jsonResponseString);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private int extractCustomerId(String jsonString) {
        return Integer.parseInt(JsonHelper.extractField(jsonString, "customerId"));
    }

    private Station extractStation(String jsonMessage) throws JsonProcessingException {
        String stationJson = JsonHelper.extractObject(jsonMessage, "station");
        if (stationJson != null && !stationJson.isEmpty()) {
            return objectMapper.readValue(stationJson, Station.class);
        } else {
            throw new IllegalArgumentException("Invalid station JSON: " + stationJson);
        }
    }

    private void sendToDataCollectionReceiver(String jsonResponseString) throws Exception {
        System.out.println("Sending...");
        messagingQueue.publish(OUTPUT_QUEUE_NAME, jsonResponseString);
    }


    private String generateJsonResponse(int customerId, int stationId, List<Double> kwhData) throws JsonProcessingException {
        Map<String, Object> jsonResponse = new HashMap<>();
        jsonResponse.put("customerId", customerId);
        jsonResponse.put("stationId", stationId);
        jsonResponse.put("kwh", kwhData);
        return objectMapper.writeValueAsString(jsonResponse);
    }

    public void setCustomerStationDataRepository(Repository<Double> customerStationDataRepository) {
        this.customerStationDataRepository = customerStationDataRepository;
    }

    public void run() {
        try {
            System.out.println("Station Data Collection App started");
            System.out.println("Waiting for messages in: " + INPUT_QUEUE_NAME);
            messagingQueue.consume(INPUT_QUEUE_NAME);
        } catch (Exception e) {
            System.out.println("Error consuming in " + INPUT_QUEUE_NAME + ": " + e.getMessage());
            // Exception handling code
            e.printStackTrace();
            System.exit(1);
        }
    }
}
