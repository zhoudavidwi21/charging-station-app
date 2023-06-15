package at.technikum.service;

import at.technikum.dto.CustomerStationData;
import at.technikum.dto.Station;
import at.technikum.repository.Repository;

public class StationDataCollectionApp {

    private static final String INPUT_QUEUE_NAME = "station_data_collector_queue";
    private static final String OUTPUT_QUEUE_NAME = "data_collection_receiver_queue";

    private final MessagingQueue messagingQueue;

    private final Repository<CustomerStationData> customer;

}
