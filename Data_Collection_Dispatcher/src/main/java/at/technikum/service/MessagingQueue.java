package at.technikum.service;

public interface MessagingQueue {

    void consumeAndPublish(String inputQueue, String outputQueue1, String outputQueue2) throws Exception;
}
