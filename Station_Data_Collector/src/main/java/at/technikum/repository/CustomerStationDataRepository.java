package at.technikum.repository;

import at.technikum.dto.CustomerStationData;
import at.technikum.dto.Station;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CustomerStationDataRepository implements Repository<CustomerStationData> {

    private Database database;

    public CustomerStationDataRepository(Database database) {
        this.database = database;
    }

    @Override
    public List<CustomerStationData> getKwhByCustomerId(int customerId) throws Exception {
        List<CustomerStationData> customerStationData = new ArrayList<>();

        String query = "SELECT * FROM charge WHERE customer_id = ?";
        ResultSet rs = database.executeQuery(query, customerId);

        while (rs.next()) {
            Double kwh = rs.getDouble("kwh");

            CustomerStationData customerStationData1 = new CustomerStationData(kwh);
            customerStationData.add(customerStationData1);
        }

        return customerStationData;
    }
}
