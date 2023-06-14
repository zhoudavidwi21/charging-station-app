package at.technikum.repository;

import at.technikum.dto.Station;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StationsRepository implements Repository<Station> {

    private Database database;

    public StationsRepository(Database database) {
        this.database = database;
    }

    @Override
    public List<Station> getAllStations() throws Exception {
        List<Station> stations = new ArrayList<>();

        String query = "SELECT * FROM station";
        ResultSet rs = database.executeQuery(query);

        while (rs.next()) {
            int id = rs.getInt("id");
            String dbUrl = rs.getString("db_url");
            double lat = rs.getDouble("lat");
            double lng = rs.getDouble("lng");

            Station station = new Station(id, dbUrl, lat, lng);
            stations.add(station);
        }

        return stations;
    }

    @Override
    public int getNumberOfStations() throws Exception {

        String query = "SELECT COUNT(*) FROM station";
        ResultSet rs = database.executeQuery(query);
        rs.next();
        return rs.getInt(1);

        // alternatively
        // return getAllStations().size();
    }
}
