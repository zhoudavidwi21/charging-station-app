package at.technikum.repository;

import java.util.List;

public interface Repository<T>{

    List<T> getAllStations() throws Exception;

    int getNumberOfStations() throws Exception;

}
