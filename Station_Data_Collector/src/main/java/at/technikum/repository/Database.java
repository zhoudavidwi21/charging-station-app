package at.technikum.repository;

import java.sql.*;

public abstract class Database implements AutoCloseable, ExecuteQuery {

    private Connection connection;

    public Database() throws SQLException {
        connection = getConnection();
    }

    private Connection getConnection()  throws SQLException {
        return DriverManager.getConnection(getURL());
    }

    public abstract String getURL();

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
