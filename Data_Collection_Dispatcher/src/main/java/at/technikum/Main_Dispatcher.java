package at.technikum;

import at.technikum.repository.PostgreSQLDatabase;
import at.technikum.repository.StationsRepository;
import at.technikum.service.DataCollectionDispatcherApp;

public class Main_Dispatcher {
    public static void main(String[] args) throws Exception {
        DataCollectionDispatcherApp app = new DataCollectionDispatcherApp(
                new StationsRepository(new PostgreSQLDatabase()));
        System.out.println("Starting DataCollectionDispatcherApp...");
        app.run();
    }


}