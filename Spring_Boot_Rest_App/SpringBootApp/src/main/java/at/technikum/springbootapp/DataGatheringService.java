package at.technikum.springbootapp;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DataGatheringService implements DataGatheringServiceInterface {

    private final Map<String, Data> dataMap = new HashMap<>();

    @Override
    public Map<String, Data> findAllData() {
        return dataMap;
    }

    @Override
    public Data findData(String customerId) {
        return dataMap.get(customerId);
    }

    @Override
    public void addData(Data data) {
        dataMap.put(data.getCustomerId(), data);
    }
}

