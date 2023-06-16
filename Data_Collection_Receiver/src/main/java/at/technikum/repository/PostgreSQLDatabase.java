package at.technikum.repository;

import at.technikum.repository.Database;

import java.sql.*;

public class PostgreSQLDatabase implements Database {
    private Connection connection;

    private static final String DRIVER = "postgresql";
    private static final String HOST = "localhost";
    private static final String PORT = "30001";
    private static final String DATABASE = "customerdb";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgres";

    public PostgreSQLDatabase() throws SQLException {
        connection = getConnection();
    }

    private static Connection getConnection()  throws SQLException {
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

    @Override
    public ResultSet executeQuery(String query) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);
        return statement.executeQuery();
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
