package at.technikum.springbootapp;

public class Data {
    private String customerId;

    public Data() {
        super();
    }

    public Data(String customerId) {
        super();
        this.customerId = customerId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
