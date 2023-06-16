package at.technikum.repository;

import at.technikum.dto.Customer;

import java.util.List;

public interface Repository<T>{

    Customer getCustomerById(int id) throws Exception;

}
