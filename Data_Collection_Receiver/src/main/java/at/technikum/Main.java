package at.technikum;

import at.technikum.repository.CustomersRepository;
import at.technikum.repository.PostgreSQLDatabase;
import at.technikum.service.DataCollectionReceiverApp;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

public class Main {
    public static void main(String[] args) throws SQLException, IOException, TimeoutException {
        DataCollectionReceiverApp app = new DataCollectionReceiverApp(
                new CustomersRepository(new PostgreSQLDatabase()));
        System.out.println("Starting DataCollectionDispatcherApp...");
        app.run();
    }
}