package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Point class.
 */
class PointTests {
    private static final double DELTA = 0.000001;

    /**
     * Test method for {@link primitives.Point#subtract(primitives.Point)}.
     */
    @Test
    void testSubtract() {
        // ============ Equivalence Partitions Tests ==============
        Point p1 = new Point(1.0, 2.0, 3.0);
        Point p2 = new Point(4.0, 5.0, 6.0);
        Vector result = p1.subtract(p2);
        assertEquals(new Vector(-3.0, -3.0, -3.0), result, DELTA, "Incorrect result for subtraction of points");

        // =============== Boundary Values Tests ==================
        // TC01: Subtraction of a point with itself should result in the zero vector
        Point p3 = new Point(0.0, 0.0, 0.0);
        result = p3.subtract(p3);
        assertEquals(Vector.ZERO, result, DELTA, "Subtraction of the same points should result in zero vector");
    }

    /**
     * Test method for {@link primitives.Point#add(primitives.Vector)}.
     */
    @Test
    void testAdd() {
        // ============ Equivalence Partitions Tests ==============
        Point p1 = new Point(1.0, 2.0, 3.0);
        Vector v1 = new Vector(4.0, 5.0, 6.0);
        Point result = p1.add(v1);
        assertEquals(new Point(5.0, 7.0, 9.0), result, "Incorrect result for adding vector to point");

        // =============== Boundary Values Tests ==================
        // TC01: Adding a zero vector to a point should not change the point
        Vector v2 = (Vector) Point.ZERO;
        result = p1.add(v2);
        assertEquals(p1, result, "Adding a zero vector should return the original point");
    }

    /**
     * Test method for {@link primitives.Point#distance(primitives.Point)}.
     */
    @Test
    void testDistance() {
        // ============ Equivalence Partitions Tests ==============
        Point p1 = new Point(1.0, 2.0, 3.0);
        Point p2 = new Point(4.0, 5.0, 6.0);
        Double result = p1.distance(p2);
        assertEquals(Math.sqrt(27.0), result, DELTA, "Incorrect distance between points");

        // =============== Boundary Values Tests ==================
        // TC01: Distance from a point to itself should be 0
        Point p3 = new Point(0.0, 0.0, 0.0);
        result = p3.distance(p3);
        assertEquals(0.0, result, DELTA, "Distance between a point and itself should be 0");
    }

    /**
     * Test method for {@link primitives.Point#distanceSquared(primitives.Point)}.
     */
    @Test
    void testDistanceSquared() {
        // ============ Equivalence Partitions Tests ==============
        Point p1 = new Point(1.0, 2.0, 3.0);
        Point p2 = new Point(4.0, 5.0, 6.0);
        Double result = p1.distanceSquared(p2);
        assertEquals(27.0, result, DELTA, "Incorrect squared distance between points");

        // =============== Boundary Values Tests ==================
        // TC01: Squared distance from a point to itself should be 0
        Point p3 = new Point(0.0, 0.0, 0.0);
        result = p3.distanceSquared(p3);
        assertEquals(0.0, result, DELTA, "Squared distance between a point and itself should be 0");
    }
}
