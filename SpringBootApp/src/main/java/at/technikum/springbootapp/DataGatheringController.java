package at.technikum.springbootapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private ResourceLoader resourceLoader;

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
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(data);
            rabbitTemplate.convertAndSend(QUEUE_NAME, json);
        } catch (JsonProcessingException e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Fehler beim Konvertieren des Datenobjekts in JSON.");
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Data gathering started for customer ID: " + customerId);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<byte[]> getInvoice(@PathVariable("customerId") String customerId) throws IOException {
        String fileName = "customer_" + customerId + ".pdf";
        final String OUTPUT_DIRECTORY = Paths.get(System.getProperty("user.home"), "..", "Public").toString();
        String filePath = OUTPUT_DIRECTORY + "\\" + fileName;

        File file = new File(filePath);
        if (file.exists()) {
            Date creationDate = new Date(file.lastModified());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedCreationDate = dateFormat.format(creationDate);
            String response = "Invoice PDF for customer ID: " + customerId
                    + "\nDownload Link: " + filePath
                    + "\nCreation Date: " + formattedCreationDate;
            return ResponseEntity.ok(response.getBytes());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(("Invoice not available for customer ID: " + customerId).getBytes());
        }
    }
}
