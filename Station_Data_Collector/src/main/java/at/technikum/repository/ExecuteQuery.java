package at.technikum.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ExecuteQuery {

    ResultSet executeQuery(String query, int customerId) throws SQLException;
}
