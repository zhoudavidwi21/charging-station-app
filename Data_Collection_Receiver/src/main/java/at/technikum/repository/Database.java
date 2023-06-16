package at.technikum.repository;

import java.sql.ResultSet;

public interface Database extends AutoCloseable {
    ResultSet executeQuery(String query) throws Exception;
}
