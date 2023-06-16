package at.technikum.service;

import at.technikum.dto.Customer;
import at.technikum.repository.Repository;
import at.technikum.util.JsonHelper;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeoutException;

public class DataCollectionReceiverApp2 implements MessageHandler {
    private static final String INPUT_QUEUE_NAME_2 = "data_collection_receiver_queue_2";
    private static final String OUTPUT_QUEUE_NAME = "pdf_generator_queue";

    private final MessagingQueue messagingQueue;
    private final Repository<Customer> customerRepository;
    private List<Map<String, Object>> stationDataList = new ArrayList<>();;
    private int numOfMessages;
    private int customerId;

    public DataCollectionReceiverApp2(Repository<Customer> customerRepository) throws IOException, TimeoutException {
        this.messagingQueue = new RabbitMQService(this);
        this.customerRepository = customerRepository;
    }

    public void listenToStationDataMessages(int numOfMessages, int customerId) {
        System.out.println("Listening to Station data messages");
        this.numOfMessages = numOfMessages;
        this.customerId = customerId;
        try {
            messagingQueue.consume(INPUT_QUEUE_NAME_2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleMessage(String message) throws Exception {
        Map<String, Object> stationDataMap = JsonHelper.deserialize(message);
        stationDataList.add(stationDataMap);
        if (stationDataList.size() == numOfMessages) {
            sendToPdfGenerator(customerId, stationDataList);
            stationDataList.clear();
        }
    }

    private void sendToPdfGenerator(int customerId, List<Map<String, Object>> stationDataList) throws Exception {
        Customer customer = customerRepository.getCustomerById(customerId);
        if (customer != null) {
            String firstName = customer.getFirst_name();
            String lastName = customer.getLast_name();
            List<Map<String, Object>> stationsList = new ArrayList<>();
            double totalKwh = 0;
            for (Map<String, Object> stationData : stationDataList) {
                int stationId = (int) stationData.get("stationId");
                List<Double> kwhList = (List<Double>) stationData.get("kwh");
                double stationTotalKwh = kwhList.stream().mapToDouble(Double::doubleValue).sum();
                totalKwh += stationTotalKwh;

                Map<String, Object> stationMap = new HashMap<>();
                mapStationCharge(stationsList, stationId, kwhList, stationTotalKwh, stationMap);
            }

            Map<String, Object> pdfDataMap = new HashMap<>();
            pdfDataMap.put("customerId", customerId);
            pdfDataMap.put("first_name", firstName);
            pdfDataMap.put("last_name", lastName);
            pdfDataMap.put("stations", stationsList);
            pdfDataMap.put("total_kwh", totalKwh);
            pdfDataMap.put("currentTime", System.currentTimeMillis());

            messagingQueue.publish(OUTPUT_QUEUE_NAME, JsonHelper.serialize(pdfDataMap));
        } else {
            System.out.println("Customer with id " + customerId + " not found");
            System.out.println("Skipping pdf generation");
            System.out.println("-----------------------------------");
            System.out.println("Waiting for messages. To exit press CTRL+C");
        }
    }

    private static void mapStationCharge(List<Map<String, Object>> stationsList, int stationId, List<Double> kwhList, Double stationTotalKwh, Map<String, Object> stationMap) {
        stationMap.put("stationId", stationId);
        stationMap.put("kwhList", kwhList);
        stationMap.put("total_kwh", stationTotalKwh);
        stationsList.add(stationMap);
    }
}