package at.technikum.repository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CustomerStationDataRepository implements Repository<Double> {

    private Database database;

    public CustomerStationDataRepository(Database database) {
        this.database = database;
    }

    @Override
    public List<Double> getKwhByCustomerId(int customerId) throws Exception {
        List<Double> customerStationData = new ArrayList<>();

        String query = "SELECT * FROM charge WHERE customer_id = ?";
        ResultSet rs = database.executeQuery(query, customerId);

        while (rs.next()) {
            Double kwh = rs.getDouble("kwh");

            customerStationData.add(kwh);
        }

        return customerStationData;
    }
}
