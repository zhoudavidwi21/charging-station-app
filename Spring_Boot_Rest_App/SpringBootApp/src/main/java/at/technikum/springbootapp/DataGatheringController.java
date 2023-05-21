package at.technikum.springbootapp;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invoices")
public class DataGatheringController {

    private final AmqpTemplate amqpTemplate;
    private final DataGatheringService service;

    @Autowired
    public DataGatheringController(AmqpTemplate amqpTemplate, DataGatheringService service) {
        this.amqpTemplate = amqpTemplate;
        this.service = service;
    }

    @PostMapping("/{customerId}")
    public String startDataGathering(@PathVariable String customerId) {
        service.startDataGathering(customerId);
        return "Data gathering started for customer ID: " + customerId;
    }

    @GetMapping("/{customerId}")
    public String getInvoice(@PathVariable String customerId) {
        return "Invoice PDF for customer ID: " + customerId;
    }
}

