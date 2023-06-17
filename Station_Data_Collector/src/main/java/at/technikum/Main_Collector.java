package at.technikum;

import at.technikum.service.StationDataCollectionApp;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Main_Collector {
    public static void main(String[] args) throws IOException, TimeoutException {
        StationDataCollectionApp app = new StationDataCollectionApp();
        System.out.println("Starting StationDataCollectionApp...");
        app.run();
    }
}