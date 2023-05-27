package at.technikum.springbootapp;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/invoices")
public class DataGatheringController {

    private final DataGatheringService service;
    private final RabbitTemplate rabbitTemplate;
    private final Connection connection;
    private final Channel channel;

    private static final String QUEUE_NAME = "data_collection_dispatcher_queue";

    @Autowired
    public DataGatheringController(DataGatheringService service, RabbitTemplate rabbitTemplate) throws IOException, TimeoutException {
        this.service = service;
        this.rabbitTemplate = rabbitTemplate;
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(30003);
        this.connection = factory.newConnection();
        this.channel = connection.createChannel();
    }

    @PostMapping("/{customerId}")
    public ResponseEntity<String> startDataGathering(@PathVariable("customerId") String customerId) throws IOException {
        Data data = new Data(customerId);
        service.addData(data);
        rabbitTemplate.convertAndSend("data_collection_dispatcher_queue", data);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Data gathering started for customer ID: " + customerId);
    }

    @GetMapping("/{customerId}")
    public String getInvoice(@PathVariable("customerId") String customerId) {
        Data data = service.findData(customerId);
        if (data != null) {
            return "Invoice PDF for customer ID: " + customerId;
        } else {
            return "Invoice not available for customer ID: " + customerId;
        }
    }
}



    /*
    @PostMapping("/{customerId}")
    public String startDataGathering(@PathVariable String customerId) {
        service.startDataGathering(customerId);
        return "Data gathering started for customer ID: " + customerId;
    }

    @GetMapping("/{customerId}")
    public String getInvoice(@PathVariable String customerId) {
        return "Invoice PDF for customer ID: " + customerId;
    } */


