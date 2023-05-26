package at.technikum;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private static final String DRIVER = "postgresql";
    private static final String HOST = "localhost";
    private static final String PORT = "30002";
    private static final String DATABASE = "stationdb";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgres";

    public static Connection getConnection()  throws SQLException {
        return DriverManager.getConnection(getURL());
    }

    private static String getURL() {
        return String.format("jdbc:%s://%s:%s/%s?user=%s&password=%s",
                DRIVER,
                HOST,
                PORT,
                DATABASE,
                USERNAME,
                PASSWORD);
    }
}
