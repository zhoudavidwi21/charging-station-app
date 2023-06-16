package at.technikum.service;

import com.rabbitmq.client.DeliverCallback;

public interface MessagingQueue {

    void publish(String queueName, String message) throws Exception;
    void consume(String queueName) throws Exception;

}
