package at.technikum.repository;

import at.technikum.dto.Customer;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CustomersRepository implements Repository<Customer>{

    private Database database;

    public CustomersRepository(Database database) {
        this.database = database;
    }

    @Override
    public Customer getCustomerById(int customerId) throws Exception {
        String query = "SELECT * FROM customer WHERE id = ?";
        ResultSet rs = database.executeQuery(query, customerId);

        Customer customer = null;
        while (rs.next()) {
            int id = rs.getInt("id");
            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");
            customer = new Customer(id, firstName, lastName);
        }

        return customer;
    }
}
