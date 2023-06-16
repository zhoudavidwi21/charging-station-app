package com.example.javafx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
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
    private ProgressIndicator progressIndicator;

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
            // Schließen der Anwendung
            closeButton.getScene().getWindow().hide();
        });
    }

    public void startDataGathering(String customerId) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String url = BASE_URL + customerId;
            HttpPost httpPost = new HttpPost(url);

            // Setzen des Content-Type-Headers auf application/json
            httpPost.setHeader("Content-Type", "application/json");

            // JSON-Payload für die Anfrage erstellen
            String payload = "{\"customerId\": \"" + customerId + "\"}";

            // JSON-Payload als Request-Body setzen
            HttpEntity requestEntity = new StringEntity(payload, ContentType.APPLICATION_JSON);
            httpPost.setEntity(requestEntity);

            // Anfrage senden und Antwort empfangen
            HttpResponse response = httpClient.execute(httpPost);

            // Statuscode überprüfen
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 202) {
                // Erfolgreich gestartet
                HttpEntity entity = response.getEntity();
                String responseString = EntityUtils.toString(entity, "UTF-8");
                System.out.println(responseString);
                updateStatusLabel(responseString);
            } else {
                // Fehler aufgetreten
//                updateStatusLabel("Failed to start data gathering for customer ID: " + customerId);
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

            // Anfrage senden und Antwort empfangen
            HttpResponse response = httpClient.execute(httpGet);

            // Statuscode überprüfen
            int statusCode = response.getStatusLine().getStatusCode();
            HttpEntity responseEntity = response.getEntity();
            String responseString = EntityUtils.toString(responseEntity, "UTF-8");
            System.out.println(responseString);

            if (statusCode == 200) {
                // Erfolgreich heruntergeladen
                if (responseEntity != null) {
                    updateStatusLabel(responseString);
                }
            } else if (statusCode == 404) {
                updateStatusLabel(responseString);
            }   else
            {
                // Fehler aufgetreten
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
