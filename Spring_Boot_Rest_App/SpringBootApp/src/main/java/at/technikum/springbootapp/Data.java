package at.technikum.springbootapp;

public class Data {
    private String customerId;

    //station id wird nicht gebraucht
    private String stationId;

    public Data() {
        super();
    }

    public Data(String customerId) {
        super();
        this.customerId = customerId;
    }

    public Data(String customerId, String stationId) {
        super();
        this.customerId = customerId;
        this.stationId = stationId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }
}
