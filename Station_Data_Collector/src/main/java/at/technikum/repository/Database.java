package at.technikum.repository;

import java.sql.*;

public abstract class Database implements AutoCloseable, ExecuteQuery {

    private Connection connection;

    public Database() throws SQLException {
    }

    public abstract Connection getConnection(String hostport)  throws SQLException;

    public abstract String getURL(String hostport);

    public void setConnection(Connection connection) {
        this.connection = connection;
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
