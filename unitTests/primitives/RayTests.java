package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the {@link primitives.Ray} class.
 * This class contains unit tests to verify the correct functionality of the {@link primitives.Ray#getPoint(double)}
 * method, which calculates a point along the ray given a parameter t.
 */
class RayTests {

    /**
     * Test method for {@link primitives.Ray#getPoint(double)}.
     * Verifies that the correct point along the ray is calculated for different values of t.
     */
    @Test
    void testGetPoint() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: positive t
        Ray ray = new Ray(new Point(1.0, 0.0, 0.0), new Vector(0.0, 1.0, 0.0));
        assertEquals(ray.getPoint(5), new Point(1.0, 5.0, 0.0), "wrong result for positive t");

        // TC02: negative t
        assertEquals(ray.getPoint(-2), new Point(1.0, -2.0, 0.0), "wrong result for negative t");

        // =============== Boundary Values Tests ==================
        // TC03: t=0
        assertEquals(ray.getPoint(0), new Point(1.0, 0.0, 0.0), "wrong result for t=0");
    }
}
