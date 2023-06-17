package at.technikum.repository;


import at.technikum.service.DataCollectionDispatcherApp;
import at.technikum.service.MessagingQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class DataCollectionDispatcherAppTest {

    private static DataCollectionDispatcherApp dataCollectionDispatcherApp;
    private StationsRepository mockStationsRepository;

    @BeforeEach
    public void setUp() throws Exception {
        // Arrange
        mockStationsRepository = mock(StationsRepository.class);
        dataCollectionDispatcherApp = new DataCollectionDispatcherApp(
                mockStationsRepository);
    }

    @Test
    public void testIsNumericShouldReturnTrueForNumericString() {
        // Arrange
        String numericString = "123";

        // Act
        boolean isNumeric = dataCollectionDispatcherApp.isNumeric(numericString);

        // Assert
        assertTrue(isNumeric);
    }

    @Test
    public void testIsNumericShouldReturnFalseForNonNumericString() {
        // Arrange
        String nonNumericString = "abc";

        // Act
        boolean isNumeric = dataCollectionDispatcherApp.isNumeric(nonNumericString);

        // Assert
        assertFalse(isNumeric);
    }

    @Test
    public void testIsNumericShouldReturnFalseForMixedString() {
        // Arrange
        String mixedString = "123abc";

        // Act
        boolean isNumeric = dataCollectionDispatcherApp.isNumeric(mixedString);

        // Assert
        assertFalse(isNumeric);
    }

}
