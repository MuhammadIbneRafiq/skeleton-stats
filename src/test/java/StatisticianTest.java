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

    // Boundary and Special Cases
    @Test
    void testWithZeroAndNegativeValues() {
        stats.addData(0.0);
        stats.addData(-10.0);
        stats.addData(-20.0);
        assertDoesNotThrow(() -> {
            stats.mean();
            stats.median();
            stats.variance();
        });
    }

    @Test
    void testWithVeryLargeAndVerySmallValues() {
        stats.addData(Double.MAX_VALUE);
        stats.addData(Double.MIN_VALUE);
        assertDoesNotThrow(() -> {
            stats.mean();
            stats.variance();
        });
    }

    @Test
    void testWithRepeatingPattern() {
        stats.addData(1.0);
        stats.addData(2.0);
        stats.addData(1.0);
        stats.addData(2.0);
        stats.addData(1.0);
        stats.addData(2.0);
        assertEquals(1.0, stats.mode());
    }

    @Test
    void testWithIncreasingSequence() {
        stats.addData(1.0);
        stats.addData(2.0);
        stats.addData(3.0);
        stats.addData(4.0);
        stats.addData(5.0);
        assertEquals(3.0, stats.median());
    }

    @Test
    void testWithDecreasingSequence() {
        stats.addData(5.0);
        stats.addData(4.0);
        stats.addData(3.0);
        stats.addData(2.0);
        stats.addData(1.0);
        assertEquals(3.0, stats.median());
    }
}