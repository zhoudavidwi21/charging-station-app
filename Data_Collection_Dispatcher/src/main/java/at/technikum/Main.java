package at.technikum;

import at.technikum.repository.PostgreSQLDatabase;
import at.technikum.repository.StationsRepository;
import at.technikum.service.DataCollectionDispatcherApp;


import java.util.Objects;

public class Main {
    public static void main(String[] args) throws Exception {
        DataCollectionDispatcherApp app = new DataCollectionDispatcherApp(
                new StationsRepository(new PostgreSQLDatabase()));
        System.out.println("Starting DataCollectionDispatcherApp...");
        app.run();
    }
}