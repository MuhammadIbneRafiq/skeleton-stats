// Aurthor: Muhammad Rafiq, 1924214
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    
    // mean tests
    @Test
    void testMeanNormalCase() {
        stats.addData(2.0);
        stats.addData(4.0);
        stats.addData(6.0);
        
        assertEquals(4.0, stats.mean());
    }
    
    @Test
    void testMeanWithSingleElement() {
        stats.addData(5.0);
        assertEquals(5.0, stats.mean());
    }
    
    @Test
    void testMeanWithEmptyDataset() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            stats.mean();
        });
        
        assertTrue(exception.getMessage().contains("empty dataset"));
    }
    
    @Test
    void testMeanWithDecimalValues() {
        stats.addData(1.5);
        stats.addData(2.5);
        stats.addData(3.5);
        
        assertEquals(2.5, stats.mean());
    }
    
    @Test
    void testMeanWithPreciseValues() {
        stats.addData(10.123);
        stats.addData(20.456);
        stats.addData(30.789);
        
        // Average is 20.456
        assertEquals(20.456, stats.mean(), 0.000001);
    }
    
    // median tests
    @Test
    void testMedianWithOddNumberOfElements() {
        stats.addData(3.0);
        stats.addData(1.0);
        stats.addData(5.0);
        
        assertEquals(3.0, stats.median());
    }
    
    @Test
    void testMedianWithEvenNumberOfElements() {
        stats.addData(3.0);
        stats.addData(1.0);
        stats.addData(5.0);
        stats.addData(7.0);
        
        assertEquals(4.0, stats.median());
    }
    
    @Test
    void testMedianWithEmptyDataset() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            stats.median();
        });
        
        assertTrue(exception.getMessage().contains("empty dataset"));
    }
    
    @Test
    void testMedianWithDecimalValues() {
        stats.addData(1.1);
        stats.addData(2.2);
        stats.addData(3.3);
        stats.addData(4.4);
        
        // Median of [1.1, 2.2, 3.3, 4.4] should be (2.2 + 3.3) / 2 = 2.75
        assertEquals(2.75, stats.median());
    }
    
    @Test
    void testMedianWithPreciseValues() {
        stats.addData(10.123);
        stats.addData(20.456);
        stats.addData(30.789);
        
        // Median is 20.456
        assertEquals(20.456, stats.median(), 0.000001);
    }
    
    // mode tests
    @Test
    void testModeNormalCase() {
        stats.addData(1.0);
        stats.addData(2.0);
        stats.addData(2.0);
        stats.addData(3.0);
        
        assertEquals(2.0, stats.mode());
    }
    
    @Test
    void testModeWithAllUniqueElements() {
        stats.addData(1.0);
        stats.addData(2.0);
        stats.addData(3.0);
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            stats.mode();
        });
        
        assertTrue(exception.getMessage().contains("all elements are unique"));
    }
    
    @Test
    void testModeWithTwoModes() {
        // When there are multiple values with the same frequency, 
        // the smallest one should be returned
        stats.addData(3.0);
        stats.addData(3.0);
        stats.addData(1.0);
        stats.addData(1.0);
        
        assertEquals(1.0, stats.mode());
    }
    
    @Test
    void testModeWithNearEqualValues() {
        stats.addData(5.0);
        stats.addData(5.0000001); // Should be considered the same due to epsilon
        stats.addData(10.0);
        
        assertEquals(5.0, stats.mode(), 0.0000001);
    }
    
    // variance tests
    @Test
    void testVarianceNormalCase() {
        stats.addData(2.0);
        stats.addData(4.0);
        stats.addData(6.0);
        stats.addData(8.0);
        
        // Expected variance: ((2-5)^2 + (4-5)^2 + (6-5)^2 + (8-5)^2) / 3 = 20/3 = 6.666...
        assertEquals(20.0 / 3.0, stats.variance(), 0.000001);
    }
    
    @Test
    void testVarianceWithEmptyDataset() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            stats.variance();
        });
        
        assertTrue(exception.getMessage().contains("empty dataset"));
    }
    
    @Test
    void testVarianceWithSingleElementReturnsZero() {
        stats.addData(5.0);

        // Variance of a single element should be 0
        assertEquals(0.0, stats.variance(), 0.000001);
    }
    
    @Test
    void testVarianceWithIdenticalValues() {
        stats.addData(3.0);
        stats.addData(3.0);
        stats.addData(3.0);
        
        // Variance of identical values should be 0
        assertEquals(0.0, stats.variance(), 0.000001);
    }
    
    @Test
    void testVarianceWithDecimalValues() {
        stats.addData(1.5);
        stats.addData(2.5);
        stats.addData(3.5);
        stats.addData(4.5);
        
        // Expected variance: ((1.5-3)^2 + (2.5-3)^2 + (3.5-3)^2 + (4.5-3)^2) / 3 = 5/3 = 1.666...
        assertEquals(5.0 / 3.0, stats.variance(), 0.000001);
    }
    
    // Combined operation tests
    @Test
    void testCombinedOperations() {
        stats.addData(2.0);
        stats.addData(4.0);
        stats.addData(6.0);
        stats.addData(6.0);
        stats.addData(8.0);
        
        // Mean = (2+4+6+6+8)/5 = 26/5 = 5.2
        assertEquals(5.2, stats.mean(), 0.000001);
        assertEquals(6.0, stats.median());
        assertEquals(6.0, stats.mode());
        
        // Variance = ((2-5.2)^2 + (4-5.2)^2 + (6-5.2)^2 + (6-5.2)^2 + (8-5.2)^2) / 4 = 5.2
        assertEquals(5.2, stats.variance(), 0.000001);
        
        stats.removeData(6.0); // Removes both 6.0 values
        
        // Mean = (2+4+8)/3 = 14/3 = 4.666...
        assertEquals(14.0 / 3.0, stats.mean(), 0.000001);
        assertEquals(4.0, stats.median());
        
        // Now [2.0, 4.0, 8.0] - all elements are unique
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            stats.mode();
        });
        assertTrue(exception.getMessage().contains("all elements are unique"));
        
        // Variance = ((2-4.667)^2 + (4-4.667)^2 + (8-4.667)^2) / 2 = 28/3 = 9.333...
        assertEquals(28.0 / 3.0, stats.variance(), 0.000001);
    }
}