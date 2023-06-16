package at.technikum.repository;

import at.technikum.dto.Customer;

public class CustomersRepository implements Repository<Customer>{

    private Database database;

    public CustomersRepository(Database database) {
        this.database = database;
    }

    @Override
    public Customer getCustomerById(int id) throws Exception {
        return null;
    }
}
