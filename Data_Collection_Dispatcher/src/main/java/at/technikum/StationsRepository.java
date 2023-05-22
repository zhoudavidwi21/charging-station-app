package at.technikum;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StationsRepository {

    public List<Station> getAllStations() {

        String query = "SELECT * FROM station";
        List<Station> stations = new ArrayList<>();

        try (
                Connection connection = Database.getConnection();
                PreparedStatement ps = connection.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                )
        {
            while (rs.next()) {
                int id = rs.getInt("id");
                String db_url = rs.getString("db_url");
                Double lat = rs.getDouble("lat");
                Double lng = rs.getDouble("lng");

                Station station = new Station(id, db_url, lat, lng);
                stations.add(station);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return stations;
    }

}
