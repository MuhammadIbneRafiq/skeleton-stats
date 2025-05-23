// Aurthor: Muhammad Rafiq, 1924214
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.util.List;

class StatisticianTest {
    // Test fixture
    private Statistician stats;
    
    @BeforeEach
    void setUp() {
        stats = new Statistician();
    }
    
    // addData tests
    @Test
    void testAddData() {
        assertEquals(0, stats.size());
        stats.addData(5.0);
        assertEquals(1, stats.size());
        stats.addData(10.0);
        assertEquals(2, stats.size());
    }
    
    @Test
    void testAddDataNegativeValue() {
        stats.addData(-7.5);
        assertEquals(1, stats.size());
    }
    
    @Test
    void testAddDataNaNValue() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            stats.addData(Double.NaN);
        });
        
        assertTrue(exception.getMessage().contains("NaN or Infinite"));
    }
    
    @Test
    void testAddDataInfiniteValue() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            stats.addData(Double.POSITIVE_INFINITY);
        });
        
        assertTrue(exception.getMessage().contains("NaN or Infinite"));
    }
    
    @Test
    void testAddDataPrecisionValues() {
        stats.addData(1.123456789);
        stats.addData(2.987654321);
        assertEquals(2, stats.size());
    }
    
    // removeData tests
    @Test
    void testRemoveDataExistingValue() {
        stats.addData(5.0);
        stats.addData(10.0);
        stats.addData(5.000001); // Should be considered equal to 5.0 (within epsilon)
        
        boolean result = stats.removeData(5.0);
        
        assertTrue(result);
        assertEquals(1, stats.size());
    }
    
    @Test
    void testRemoveDataNonExistentValue() {
        stats.addData(5.0);
        stats.addData(10.0);
        
        boolean result = stats.removeData(7.0);
        
        assertFalse(result);
        assertEquals(2, stats.size());
    }
    
    @Test
    void testRemoveDataNaNValue() {
        stats.addData(5.0);
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            stats.removeData(Double.NaN);
        });
        
        assertTrue(exception.getMessage().contains("NaN or Infinite"));
    }
    
    @Test
    void testRemoveDataInfiniteValue() {
        stats.addData(5.0);
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            stats.removeData(Double.NEGATIVE_INFINITY);
        });
        
        assertTrue(exception.getMessage().contains("NaN or Infinite"));
    }
    
    //mean tests
    @Test
    void testMeanWithSingleElement() {
        stats.addData(4.0);
        assertEquals(4.0, stats.mean(), 0.000001);
    }

    @Test
    void testMeanWithMultipleElements() {
        stats.addData(2.0);
        stats.addData(4.0);
        stats.addData(6.0);
        assertEquals(4.0, stats.mean(), 0.000001); 
        // (2+4+6)/3 = 4
    }

    @Test
    void testMeanWithNegativeAndPositiveValues() {
        stats.addData(-3.0);
        stats.addData(3.0);
        assertEquals(0.0, stats.mean(), 0.000001);
    }
    
    @Test
    void testMeanWithHighPrecisionDecimals() {
        stats.addData(1.123456789);
        stats.addData(2.987654321);
        double expectedMean = (1.123456789 + 2.987654321) / 2;
        assertEquals(expectedMean, stats.mean(), 1e-9);
    }

    @Test
    void testMeanWithLargeValues() {
        stats.addData(1_000_000_000.0);
        stats.addData(2_000_000_000.0);
        assertEquals(1_500_000_000.0, stats.mean(), 1.0);
        // Loose epsilon for large numbers
    }
    
    @Test
    void testMeanWithMixedSignsAndDecimals() {
        stats.addData(-3.5);
        stats.addData(2.0);
        stats.addData(1.5);
        assertEquals(0.0, stats.mean(), 0.000001);
    }
    
    // Median Tests
    @Test
    void testMedianWithSingleElement() {
        stats.addData(7.0);
        assertEquals(7.0, stats.median());
    }

    @Test
    void testMedianWithTwoElements() {
        stats.addData(2.0);
        stats.addData(8.0);
        assertEquals(5.0, stats.median());
    }

    @Test
    void testMedianWithDuplicates() {
        stats.addData(1.0);
        stats.addData(2.0);
        stats.addData(2.0);
        stats.addData(3.0);
        assertEquals(2.0, stats.median());
    }

    @Test
    void testMedianWithAllIdenticalValues() {
        stats.addData(5.0);
        stats.addData(5.0);
        stats.addData(5.0);
        stats.addData(5.0);
        assertEquals(5.0, stats.median());
    }

    // Mode Tests
    @Test
    void testModeWithSingleElementThrowsException() {
        stats.addData(9.0);
    
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            stats.mode();
        });
    
        assertTrue(exception.getMessage().contains("all elements are unique"));
    }
    
    @Test
    void testModeWithTwoIdenticalElements() {
        stats.addData(3.0);
        stats.addData(3.0);
        assertEquals(3.0, stats.mode());
    }

    @Test
    void testModeWithMultipleModesDifferentFrequencies() {
        stats.addData(1.0);
        stats.addData(1.0);
        stats.addData(1.0);
        stats.addData(2.0);
        stats.addData(2.0);
        stats.addData(3.0);
        stats.addData(3.0);
        stats.addData(3.0);
        assertEquals(1.0, stats.mode()); // Based on smallest value policy
    }

    @Test
    void testModeWithAllValuesTwice() {
        stats.addData(2.0);
        stats.addData(2.0);
        stats.addData(4.0);
        stats.addData(4.0);
        stats.addData(6.0);
        stats.addData(6.0);
        assertEquals(2.0, stats.mode());
    }

    @Test
    void testModeWithNegativeValues() {
        stats.addData(-1.0);
        stats.addData(-1.0);
        stats.addData(0.0);
        stats.addData(1.0);
        assertEquals(-1.0, stats.mode());
    }

    @Test
    void testModeWithZeroAsMode() {
        stats.addData(0.0);
        stats.addData(0.0);
        stats.addData(1.0);
        stats.addData(2.0);
        assertEquals(0.0, stats.mode());
    }

    // Variance Tests
    @Test
    void testVarianceWithTwoElements() {
        stats.addData(1.0);
        stats.addData(5.0);
        assertEquals(8.0, stats.variance(), 0.000001); // (1-3)^2 + (5-3)^2 = 4 + 4 = 8 / 1 = 8
    }

    @Test
    void testVarianceWithLargeDifferences() {
        stats.addData(1.0);
        stats.addData(1000.0);
        stats.addData(5000.0);
        assertEquals(6998000.333333334, stats.variance(), 1.0); 
        // Very loose epsilon due to large values
    }

    // Exception Handling for Statistical Methods
    @Test
    void testMeanWithPositiveInfinity() throws Exception {
        injectDirectlyToData(Double.POSITIVE_INFINITY);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> stats.mean());
        assertTrue(exception.getMessage().contains("Infinite"));
    }
    
    @Test
    void testMeanWithNegativeInfinity() throws Exception {
        injectDirectlyToData(Double.NEGATIVE_INFINITY);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> stats.mean());
        assertTrue(exception.getMessage().contains("Infinite"));
    }
    
    @Test
    void testMeanWithNaN() throws Exception {
        injectDirectlyToData(Double.NaN);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> stats.mean());
        assertTrue(exception.getMessage().contains("NaN"));
    }
    
    @Test
    void testMedianWithPositiveInfinity() throws Exception {
        injectDirectlyToData(Double.POSITIVE_INFINITY);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> stats.median());
        assertTrue(exception.getMessage().contains("Infinite"));
    }
    
    @Test
    void testMedianWithNegativeInfinity() throws Exception {
        injectDirectlyToData(Double.NEGATIVE_INFINITY);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> stats.median());
        assertTrue(exception.getMessage().contains("Infinite"));
    }
    
    @Test
    void testMedianWithNaN() throws Exception {
        injectDirectlyToData(Double.NaN);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> stats.median());
        assertTrue(exception.getMessage().contains("NaN"));
    }
    
    @Test
    void testModeWithPositiveInfinity() throws Exception {
        // Add a normal value first to avoid the "all elements are unique" exception
        stats.addData(5.0);
        injectDirectlyToData(Double.POSITIVE_INFINITY);
        injectDirectlyToData(Double.POSITIVE_INFINITY);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> stats.mode());
        assertTrue(exception.getMessage().contains("Infinite"));
    }
    
    @Test
    void testModeWithNegativeInfinity() throws Exception {
        stats.addData(5.0);
        injectDirectlyToData(Double.NEGATIVE_INFINITY);
        injectDirectlyToData(Double.NEGATIVE_INFINITY);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> stats.mode());
        assertTrue(exception.getMessage().contains("Infinite"));
    }
    
    @Test
    void testModeWithNaN() throws Exception {
        stats.addData(5.0);
        injectDirectlyToData(Double.NaN);
        injectDirectlyToData(Double.NaN);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> stats.mode());
        assertTrue(exception.getMessage().contains("NaN"));
    }
    
    @Test
    void testVarianceWithPositiveInfinity() throws Exception {
        injectDirectlyToData(Double.POSITIVE_INFINITY);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> stats.variance());
        assertTrue(exception.getMessage().contains("Infinite"));
    }
    
    @Test
    void testVarianceWithNegativeInfinity() throws Exception {
        injectDirectlyToData(Double.NEGATIVE_INFINITY);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> stats.variance());
        assertTrue(exception.getMessage().contains("Infinite"));
    }
    
    @Test
    void testVarianceWithNaN() throws Exception {
        injectDirectlyToData(Double.NaN);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> stats.variance());
        assertTrue(exception.getMessage().contains("NaN"));
    }
    
    // Mixed invalid data tests
    @Test
    void testMeanWithMixedValidAndInvalidData() throws Exception {
        stats.addData(1.0);
        stats.addData(2.0);
        injectDirectlyToData(Double.POSITIVE_INFINITY);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> stats.mean());
        assertTrue(exception.getMessage().contains("Infinite"));
    }
    
    @Test
    void testMedianWithMixedValidAndInvalidData() throws Exception {
        stats.addData(1.0);
        stats.addData(2.0);
        injectDirectlyToData(Double.NaN);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> stats.median());
        assertTrue(exception.getMessage().contains("NaN"));
    }
    
    // Helper method to bypass normal validation and add directly to the internal data structure
    @SuppressWarnings("unchecked")
    private void injectDirectlyToData(Double value) throws Exception {
        Field dataField = Statistician.class.getDeclaredField("data");
        dataField.setAccessible(true);
        List<Double> internalData = (List<Double>) dataField.get(stats);
        internalData.add(value);
    }
}