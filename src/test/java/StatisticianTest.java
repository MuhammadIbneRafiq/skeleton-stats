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
    void addDataIncreasesSizeBy1() {
        int before = stats.size();
        stats.addData(42.0);
        assertEquals(before + 1, stats.size());
    }

    @Test
    void removeData_exact() {
        stats.addData(1.0);
        stats.addData(2.0);
        stats.addData(3.0);
        assertTrue(stats.removeData(2.0));
        assertEquals(2, stats.size());
        assertFalse(stats.removeData(2.0));
    }
    
    @Test
    void removeData_tolerance() {
        stats.addData(1.0000005);
        stats.addData(2.0);
        assertTrue(stats.removeData(1.0));
        assertEquals(1, stats.size());
    }

    // mean test
    @Test
    void mean_basic() {
        stats.addData(1.0);
        stats.addData(2.0);
        stats.addData(3.0);
        assertEquals(2.0, stats.mean(), EPS);
    }

    @Test
    void mean_emptyThrows() {
        assertThrows(IllegalArgumentException.class, () -> stats.mean());
    }

    // /median test
    @Test
    void median_odd() {
        stats.addData(3.0);
        stats.addData(1.0);
        stats.addData(4.0);
        assertEquals(3.0, stats.median(), EPS);
    }

    @Test
    void median_even() {
        stats.addData(10.0);
        stats.addData(2.0);
        stats.addData(4.0);
        stats.addData(6.0);
        assertEquals(5.0, stats.median(), EPS);
    }

    @Test
    void median_emptyThrows() {
        assertThrows(IllegalArgumentException.class, () -> stats.median());
    }

    //mode
    @Test
    void mode_basic() {
        stats.addData(2.0);
        stats.addData(2.0);
        stats.addData(3.0);
        stats.addData(3.0);
        stats.addData(3.0);
        stats.addData(4.0);
        stats.addData(4.0);
        stats.addData(4.0);
        stats.addData(2.0);
        assertEquals(2.0, stats.mode(), EPS);
    }

    @Test
    void mode_allUniqueThrows() {
        stats.addData(1.0);
        stats.addData(2.0);
        stats.addData(3.0);
        assertThrows(IllegalArgumentException.class, () -> stats.mode());
    }

    // variance (sample)
    @Test
    void variance_basic() {
        stats.addData(2.0);
        stats.addData(4.0);
        stats.addData(4.0);
        stats.addData(4.0);
        stats.addData(5.0);
        stats.addData(5.0);
        stats.addData(7.0);
        stats.addData(9.0);
        assertEquals(4.57142857, stats.variance(), 1e-5);
    }

    @Test
    void variance_singleThrows() {
        assertThrows(IllegalArgumentException.class, () -> stats.variance());
    }
    
    @Test
    void variance_sameNumbers() {
        stats.addData(4.0);
        stats.addData(4.0);
        stats.addData(4.0);
        stats.addData(4.0);
        assertEquals(0.0, stats.variance(), 1e-5);
    }    
    // # END TODO
}