package primitives;

import org.junit.jupiter.api.Test;

import java.util.List;

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

    @Test
    void testFindClosestPoint() {
        Ray ray = new Ray(new Point(1.0, 0.0, 0.0), new Vector(2.0, 2.0, 2.0));

        // Shared points for all test cases
        Point closest = new Point(1.2, 0.1, 0.1);   // Distance^2 = 0.04 + 0.01 + 0.01 = 0.06
        Point midFar = new Point(4.0, 5.0, 6.0);    // Distance^2 = 9 + 25 + 36 = 70
        Point farthest = new Point(6.0, 6.0, 6.0);  // Distance^2 = 25 + 36 + 36 = 97

        // ============ Equivalence Partition Test ==============
        // TC01: Closest point is in the middle
        List<Point> listMiddle = List.of(midFar, closest, farthest);
        assertEquals(closest, ray.findClosestPoint(listMiddle), "wrong closest point (middle)");

        // =============== Boundary Value Tests ==================

        // TC02: null list returns null
        assertNull(ray.findClosestPoint(null), "expected null for null list");

        // TC03: Closest point is first
        List<Point> listFirst = List.of(closest, midFar, farthest);
        assertEquals(closest, ray.findClosestPoint(listFirst), "wrong closest point (first)");

        // TC04: Closest point is last
        List<Point> listLast = List.of(farthest, midFar, closest);
        assertEquals(closest, ray.findClosestPoint(listLast), "wrong closest point (last)");
    }
}
