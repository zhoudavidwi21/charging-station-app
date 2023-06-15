package at.technikum.repository;

import java.sql.*;

public class PostgreSQLDatabase extends Database implements AutoCloseable, ExecuteQuery{
    private static final String DRIVER = "postgresql";
    private static final String DATABASE = "stationdb";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgres";

    public PostgreSQLDatabase(String hostport) throws SQLException {
        super.setConnection(getConnection(hostport));
    }

    @Override
    public Connection getConnection(String hostport) throws SQLException {
        return DriverManager.getConnection(getURL(hostport));
    }
    @Override
    public String getURL(String hostport) {
        return String.format("jdbc:%s://%s/%s?user=%s&password=%s",
                DRIVER,
                hostport,
                DATABASE,
                USERNAME,
                PASSWORD);
    }
}
