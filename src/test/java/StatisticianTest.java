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
    
    // variance tests
    @Test
    void testVarianceNormalCase() {
        stats.addData(2.0);
        stats.addData(4.0);
        stats.addData(6.0);
        stats.addData(8.0);
        
        assertEquals(6.0, stats.variance(), 0.000001);
    }
    
    @Test
    void testVarianceWithEmptyDataset() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            stats.variance();
        });
        
        assertTrue(exception.getMessage().contains("empty dataset"));
    }
    
    @Test
    void testVarianceWithSingleElement() {
        stats.addData(5.0);
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            stats.variance();
        });
        
        assertTrue(exception.getMessage().contains("only one data point"));
    }
}