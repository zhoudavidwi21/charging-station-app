package at.technikum.springbootapp;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataGatheringService {
    private final AmqpTemplate amqpTemplate;

    @Autowired
    public DataGatheringService(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    public void startDataGathering(String customerId) {
        amqpTemplate.convertAndSend("data-collection", customerId);
        System.out.println("Data gathering started for customer ID: " + customerId);
    }
}
