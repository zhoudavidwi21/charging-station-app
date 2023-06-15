package at.technikum.repository;

import java.util.List;

public interface Repository<T>{

    List<T> getKwhByCustomerId(int customerId) throws Exception;


}
