package at.technikum;

public class Main {
    public static void main(String[] args) {
        MessageConsumer messageConsumer = new MessageConsumer();
        messageConsumer.consumeAndPublish();
    }
}