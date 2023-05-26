package at.technikum.springbootapp;

import java.util.Map;

public interface DataGatheringServiceInterface {
    Map<String, Data> findAllData();

    Data findData(String customerId);

    void addData(Data data);
}
