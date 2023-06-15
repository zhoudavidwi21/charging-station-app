package at.technikum.repository;

import java.sql.*;

public class PostgreSQLDatabase implements Database {
    private Connection connection;

    private static final String DRIVER = "postgresql";
    private static final String HOST = "localhost";
    private String PORT;
    private static final String DATABASE = "stationdb";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgres";

    public PostgreSQLDatabase(String PORT) throws SQLException {
        this.PORT = PORT;
        connection = getConnection();
    }

    private Connection getConnection()  throws SQLException {
        return DriverManager.getConnection(getURL());
    }

    private String getURL() {
        return String.format("jdbc:%s://%s:%s/%s?user=%s&password=%s",
                DRIVER,
                HOST,
                PORT,
                DATABASE,
                USERNAME,
                PASSWORD);
    }

    @Override
    public ResultSet executeQuery(String query, int customerId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, customerId);
        return statement.executeQuery();
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
