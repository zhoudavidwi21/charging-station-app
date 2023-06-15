package at.technikum;

import at.technikum.service.StationDataCollectionApp;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Main {
    public static void main(String[] args) throws IOException, TimeoutException {
        StationDataCollectionApp app = new StationDataCollectionApp();
        System.out.println("Starting StationDataCollectionApp...");
        app.run();
    }
}