package at.technikum.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface Database extends AutoCloseable {
    ResultSet executeQuery(String query) throws Exception;
}
