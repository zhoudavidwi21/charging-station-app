package at.technikum.dto;

public class CustomerStationData {

    private Double kwh;

    public CustomerStationData(Double kwh) {
        this.kwh = kwh;
    }

    public Double getKwh() {
        return kwh;
    }

    public void setKwh(Double kwh) {
        this.kwh = kwh;
    }

    @Override
    public String toString() {
        return "CustomerStationData{" +
                "kwh=" + kwh +
                '}';
    }
}
