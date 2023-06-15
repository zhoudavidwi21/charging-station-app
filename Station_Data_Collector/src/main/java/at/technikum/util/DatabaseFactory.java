package at.technikum.util;

import at.technikum.repository.Database;
import at.technikum.repository.PostgreSQLDatabase;

import java.sql.SQLException;

public class DatabaseFactory {
    private static final DatabaseFactory instance = new DatabaseFactory();

    private DatabaseFactory() {
        // Private constructor to prevent direct instantiation
    }

    public static DatabaseFactory getInstance() {
        return instance;
    }

    public static Database createPostgresqlDatabase(String hostPort) throws SQLException {
        return new PostgreSQLDatabase(hostPort);
    }
}


