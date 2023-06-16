package com.example.javafx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HelloController {

    @FXML
    private TextField customerIdField;

    @FXML
    private Button createInvoiceButton;

    @FXML
    private Button statusInvoiceButton;

    @FXML
    private Button closeButton;

    @FXML
    private Label statusLabel;

    private static final String BASE_URL = "http://localhost:8080/invoices/";

    @FXML
    void initialize() {
        createInvoiceButton.setOnAction(event -> {
            String customerId = customerIdField.getText();
            startDataGathering(customerId);
        });

        statusInvoiceButton.setOnAction(event -> {
            String customerId = customerIdField.getText();
            downloadInvoice(customerId);
        });

        closeButton.setOnAction(event -> {
            closeButton.getScene().getWindow().hide();
        });
    }

    public void startDataGathering(String customerId) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String url = BASE_URL + customerId;
            HttpPost httpPost = new HttpPost(url);

            // Set the content type header to application/json
            httpPost.setHeader("Content-Type", "application/json");

            // Set JSON payload as request body
            String payload = "{\"customerId\": \"" + customerId + "\"}";

            // JSON-Payload als Request-Body setzen
            HttpEntity requestEntity = new StringEntity(payload, ContentType.APPLICATION_JSON);
            httpPost.setEntity(requestEntity);

            // Send request and receive response
            HttpResponse response = httpClient.execute(httpPost);

            // Check status code
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 202) {
                // Successfully launched
                HttpEntity entity = response.getEntity();
                String responseString = EntityUtils.toString(entity, "UTF-8");
                System.out.println(responseString);
                updateStatusLabel(responseString);
            } else {
                // Error occurred
                updateStatusLabel("Please enter a customer-ID!");

            }
        } catch (IOException e) {
            e.printStackTrace();
            updateStatusLabel("Error occurred: " + e.getMessage());
        }
    }

    public void downloadInvoice(String customerId) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String url = BASE_URL + customerId;
            HttpGet httpGet = new HttpGet(url);

            // Send request and receive response
            HttpResponse response = httpClient.execute(httpGet);

            // Check status code
            int statusCode = response.getStatusLine().getStatusCode();
            HttpEntity responseEntity = response.getEntity();
            String responseString = EntityUtils.toString(responseEntity, "UTF-8");
            System.out.println(responseString);

            if (statusCode == 200) {
                // Successfully downloaded
                if (responseEntity != null) {
                    updateStatusLabel(responseString);
                }
            } else if (statusCode == 404) {
                updateStatusLabel(responseString);
            }   else
            {
                // Error occurred
                updateStatusLabel(responseString);
            }
        } catch (IOException e) {
            e.printStackTrace();
            updateStatusLabel("Error occurred: " + e.getMessage());
        }
    }

    public void updateStatusLabel(String message) {
        statusLabel.setText(message);
    }

}
