package at.technikum;

public class Station {

    private int id;
    private String db_url;
    private Double lat;
    private Double lng;

    public Station(int id, String db_url, Double lat, Double lng) {
        this.id = id;
        this.db_url = db_url;
        this.lat = lat;
        this.lng = lng;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDb_url() {
        return db_url;
    }

    public void setDb_url(String db_url) {
        this.db_url = db_url;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "Station{" +
                "id=" + id +
                ", db_url='" + db_url + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}
