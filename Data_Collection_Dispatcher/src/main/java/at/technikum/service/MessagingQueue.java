package at.technikum.service;

public interface MessagingQueue {

    void publish(String queueName, String message) throws Exception;
    void consume(String queueName) throws Exception;
}
