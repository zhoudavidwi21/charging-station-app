package at.technikum.repository;

import at.technikum.dto.Station;

import java.util.List;

public interface Repository<T>{

    List<T> getAllStations() throws Exception;

    int getNumberOfStations() throws Exception;

}
