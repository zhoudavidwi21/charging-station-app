package at.technikum;

import at.technikum.dto.Station;
import at.technikum.repository.PostgreSQLDatabase;
import at.technikum.repository.Repository;
import at.technikum.repository.StationsRepository;
import at.technikum.util.JsonHelper;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class test {


    public static void main(String[] args) throws Exception {
//        stationDataCollector();
        dataCollectionReceiver();
    }

    private static void dataCollectionReceiver() throws Exception {
        StationsRepository stationsRepository = new StationsRepository(new PostgreSQLDatabase());
        // Create a JSON object for numMessages
        int numberOfMessages = stationsRepository.getNumberOfStations();
        Map<String, Object> messageObject = new HashMap<>();
        messageObject.put("customerId", 1);
        messageObject.put("numMessages", numberOfMessages);
        String messageJson = JsonHelper.toJson(messageObject);

        // Sends data to station data collector
        System.out.println(messageJson);
    }

    private static void stationDataCollector() throws Exception {
        // Sends data to station data collector
        StationsRepository stationsRepository = new StationsRepository(new PostgreSQLDatabase());
        List<Station> stations = stationsRepository.getAllStations();

        for (Station station : stations) {
            Map<String, Object> messageObject = new HashMap<>();
            messageObject.put("customerId", 2);
            messageObject.put("station", station);
            String message = JsonHelper.toJson(messageObject);
            System.out.println(message);
        }
    }
}
