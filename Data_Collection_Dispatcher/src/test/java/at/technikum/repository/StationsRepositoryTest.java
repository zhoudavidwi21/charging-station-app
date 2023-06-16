package at.technikum.repository;

import at.technikum.dto.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class StationsRepositoryTest {

    private StationsRepository stationsRepository;
    private Database mockDatabase;
    private ResultSet mockResultSet;

    @BeforeEach
    public void setUp() {

        mockDatabase = mock(Database.class);
        mockResultSet = mock(ResultSet.class);
        stationsRepository = new StationsRepository(mockDatabase);
    }

    @Test
    public void testGetAllStations() throws Exception {

        when(mockDatabase.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next())
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(false);

        when(mockResultSet.getInt("id")).thenReturn(1).thenReturn(2);
        when(mockResultSet.getString("db_url")).thenReturn("station1_db_url").thenReturn("station2_db_url");
        when(mockResultSet.getDouble("lat")).thenReturn(1.234).thenReturn(5.678);
        when(mockResultSet.getDouble("lng")).thenReturn(2.345).thenReturn(6.789);


        List<Station> expectedStations = new ArrayList<>();
        expectedStations.add(new Station(1, "station1_db_url", 1.234, 2.345));
        expectedStations.add(new Station(2, "station2_db_url", 5.678, 6.789));

        List<Station> actualStations = stationsRepository.getAllStations();
        assert(expectedStations.get(0).toString().equals(actualStations.get(0).toString()));
        assert(expectedStations.get(1).toString().equals(actualStations.get(1).toString()));
    }
    @Test
    public void testGetNumberOfStations() throws Exception {

        when(mockDatabase.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(5);

        int actualNumberOfStations = stationsRepository.getNumberOfStations();

        assertEquals(5, actualNumberOfStations);
    }
}