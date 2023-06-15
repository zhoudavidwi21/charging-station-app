package at.technikum.service;

import at.technikum.dto.CustomerStationData;
import at.technikum.dto.Station;
import at.technikum.repository.CustomerStationDataRepository;
import at.technikum.repository.Database;
import at.technikum.repository.Repository;
import at.technikum.util.JsonHelper;
import com.fasterxml.jackson.core.type.TypeReference;
import at.technikum.util.DatabaseFactory;


import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class StationDataCollectionApp implements MessageHandler {

    private static final String INPUT_QUEUE_NAME = "station_data_collector_queue";
    private static final String OUTPUT_QUEUE_NAME = "data_collection_receiver_queue";

    private final MessagingQueue messagingQueue;

    private Repository<CustomerStationData> customerStationDataRepository;

    public StationDataCollectionApp() throws IOException, TimeoutException {
        this.messagingQueue = new RabbitMQService(this);
    }

    @Override
    public void handleMessage(String message) throws IOException {
        // Deserialize the message
        Map<String, Object> jsonMessage = JsonHelper.fromJson(message, new TypeReference<>() {});

        // Extract the customerId
        int customerId = extractCustomerId(jsonMessage);

        // Extract the station information and instantiate the Station object
        Station station = extractStation(jsonMessage);


        // Use the db_url to instantiate the appropriate Database
        try (Database database = DatabaseFactory.createPostgresqlDatabase(station.getDb_url())) {
            setCustomerStationDataRepository(new CustomerStationDataRepository(database));
            // Get the kwh by customerId
            List<CustomerStationData> customerStationData = customerStationDataRepository.getKwhByCustomerId(customerId);

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private int extractCustomerId(Map<String, Object> jsonMessage) {
        return (int) jsonMessage.get("customerId");
    }

    private Station extractStation(Map<String, Object> jsonMessage) throws IOException {
        Map<String, Object> stationJson = (Map<String, Object>) jsonMessage.get("station");
        return JsonHelper.fromJson(JsonHelper.toJson(stationJson), Station.class);
    }

    public void setCustomerStationDataRepository(Repository<CustomerStationData> customerStationDataRepository) {
        this.customerStationDataRepository = customerStationDataRepository;
    }
}
