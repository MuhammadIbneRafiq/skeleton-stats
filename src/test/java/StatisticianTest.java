// Aurthor: Muhammad Rafiq, 1924214
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StatisticianTest {
    //# BEGIN TODO: Implement test cases
    private Statistician stats;
    private static final double EPS = 1e-6;

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
        
    // removeData tests    
    @Test
    void testRemoveDataWithinEpsilon() {
        stats.addData(3.0);
        stats.addData(3.0000005); // Within epsilon (10^-6)
        
        boolean result = stats.removeData(3.0);
        
        assertTrue(result);
        assertEquals(0, stats.size()); // Both values should be removed
    }

    @Test
    void testRemoveDataExistingValue() {
        stats.addData(5.0);
        stats.addData(10.0);
        
        boolean result = stats.removeData(5.0);
        
        assertTrue(result);
        assertEquals(1, stats.size());
    }
    
    //mean tests  
    @Test
    void testMeanWithEmptyData() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> stats.mean());
        assertTrue(exception.getMessage().contains("empty"));
    }
               
    @Test
    void testMeanWithAllIdenticalValues() {
        stats.addData(5.0);
        stats.addData(4.0);
        stats.addData(3.0);
        stats.addData(2.0);
        assertEquals(3.5, stats.mean(), EPS);
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
    
    // Mode Tests
    @Test
    void testModeWithEmptyData() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> stats.mode());
        assertTrue(exception.getMessage().contains("empty"));
    }

    @Test
    void testModeWithSingleElement() {
        stats.addData(9.0);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> stats.mode());
        assertTrue(exception.getMessage().contains("unique"));
    }

    @Test
    void testModeWithAllUniqueValues() {
        stats.addData(1.0);
        stats.addData(2.0);
        stats.addData(3.0);
        stats.addData(4.0);
        stats.addData(5.0);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> stats.mode());
        assertTrue(exception.getMessage().contains("unique"));
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
   
    // Variance Tests
    @Test
    void testVarianceWithEmptyData() {
        Exception exception = assertThrows(
            IllegalArgumentException.class, () -> stats.variance());
        assertTrue(exception.getMessage().contains("empty"));
    }

    @Test
    void testVarianceWithLargeDifferences() {
        stats.addData(2.0);
        stats.addData(4.0);
        stats.addData(4.0);
        stats.addData(4.0);
        stats.addData(5.0);
        stats.addData(5.0);
        stats.addData(7.0);
        stats.addData(9.0);
        assertEquals(4.57142857, stats.variance(), 1e-5); 
        // Very loose epsilon due to large values
    }
    
    @Test
    void testVarianceWithAllIdenticalValues() {
        stats.addData(5.0);
        stats.addData(5.0);
        stats.addData(5.0);
        assertEquals(0.0, stats.variance(), EPS);
    }
    // # END TODO
}