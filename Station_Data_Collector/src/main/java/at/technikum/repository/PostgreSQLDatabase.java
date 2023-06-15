package at.technikum.repository;

import java.sql.*;

public class PostgreSQLDatabase extends Database {
    private static final String DRIVER = "postgresql";
    private String HOSTPORT;
    private static final String DATABASE = "stationdb";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgres";

    public PostgreSQLDatabase(String HOSTPORT) throws SQLException {
        this.HOSTPORT = HOSTPORT;
    }

    @Override
    public String getURL() {
        return String.format("jdbc:%s://%s/%s?user=%s&password=%s",
                DRIVER,
                HOSTPORT,
                DATABASE,
                USERNAME,
                PASSWORD);
    }
}
