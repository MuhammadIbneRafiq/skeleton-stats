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
    void testAddDataNaNValue() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            stats.addData(Double.NaN);
        });
        
        assertTrue(exception.getMessage().contains("NaN or Infinite"));
    }
    
    @Test
    void testAddDataPrecisionValues() {
        stats.addData(1.123456789);
        stats.addData(2.987654321);
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
    void testRemoveAllOccurrences() {
        stats.addData(4.0);
        stats.addData(4.0);
        stats.addData(4.0);
        
        boolean result = stats.removeData(4.0);
        
        assertTrue(result);
        assertEquals(0, stats.size()); // All occurrences should be removed
    }
    
    //mean tests  
    @Test
    void testMeanWithEmptyData() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> stats.mean());
        assertTrue(exception.getMessage().contains("empty"));
    }

    @Test
    void testMeanWithSingleValue() {
        stats.addData(5.0);
        assertEquals(5.0, stats.mean(), 0.000001);
    }
               
    @Test
    void testMeanWithAllIdenticalValues() {
        stats.addData(5.0);
        stats.addData(5.0);
        stats.addData(5.0);
        stats.addData(5.0);
        assertEquals(5.0, stats.mean());
    }
        
    // Median Tests
    @Test
    void testMedianWithEmptyData() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> stats.median());
        assertTrue(exception.getMessage().contains("empty"));
    }

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

    @Test
    void testMedianWithOddNumberOfElements() {
        stats.addData(1.0);
        stats.addData(2.0);
        stats.addData(3.0);
        stats.addData(4.0);     
        stats.addData(5.0);
        stats.addData(6.0);
        stats.addData(7.0);
        assertEquals(4.0, stats.median());
    }

    @Test
    void testMedianWithEvenNumberOfElements() {
        stats.addData(1.0);
        stats.addData(2.0);
        stats.addData(3.0);
        stats.addData(4.0);     
        stats.addData(5.0);
        stats.addData(6.0);
        assertEquals(3.5, stats.median());
    }

    @Test
    void testMedianWithEvenNotSortedValues() {
        stats.addData(6.0);
        stats.addData(3.0);
        stats.addData(2.0);
        stats.addData(1.0);
        stats.addData(3.0);
        stats.addData(5.0); 
        assertEquals(3.0, stats.median());
    }

    @Test
    void testMedianWithOddNotSortedValues() {
        stats.addData(6.0);
        stats.addData(3.0);
        stats.addData(2.0);
        stats.addData(1.0);
        stats.addData(5.0);
        assertEquals(3.0, stats.median());
    }
    
    // Mode Tests
    @Test
    void testModeWithEmptyData() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> stats.mode());
        assertTrue(exception.getMessage().contains("empty"));
    }

    @Test
    void testModeWithAllIdenticalValues() {
        stats.addData(5.0);
        stats.addData(5.0);
        stats.addData(5.0);
        stats.addData(5.0);
        assertEquals(5.0, stats.mode());
    }

    @Test
    void testModeWithSingleElement() {
        stats.addData(9.0);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> stats.mode());
        assertTrue(exception.getMessage().contains("all elements are unique"));
    }

    @Test
    void testModeWithAllUniqueValues() {
        stats.addData(1.0);
        stats.addData(2.0);
        stats.addData(3.0);
        stats.addData(4.0);
        stats.addData(5.0);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> stats.mode());
        assertTrue(exception.getMessage().contains("all elements are unique"));
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
   
    // Variance Tests
    @Test
    void testVarianceWithOneElements() {
        stats.addData(1.0);
        assertEquals(0.0, stats.variance(), 0.000001);
    }

    @Test
    void testVarianceWithEmptyData() {
        Exception exception = assertThrows(
            IllegalArgumentException.class, () -> stats.variance());
        assertTrue(exception.getMessage().contains("empty"));
    }

    @Test
    void testVarianceWithLargeDifferences() {
        stats.addData(1.0);
        stats.addData(1000.0);
        stats.addData(5000.0);
        assertEquals(6998000.333333334, stats.variance(), 1.0); 
        // Very loose epsilon due to large values
    }
    
    @Test
    void testVarianceWithAllIdenticalValues() {
        stats.addData(5.0);
        stats.addData(5.0);
        stats.addData(5.0);
        assertEquals(0.0, stats.variance(), 0.000001);
    }
}