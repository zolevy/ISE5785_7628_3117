package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the {@link primitives.Point} class.
 * This class contains unit tests to verify the correct functionality of methods
 * for the {@link primitives.Point} class, including {@link primitives.Point#subtract(primitives.Point)},
 * {@link primitives.Point#add(primitives.Vector)}, {@link primitives.Point#distance(primitives.Point)},
 * and {@link primitives.Point#distanceSquared(primitives.Point)}.
 */
class PointTests {
    private static final double DELTA = 0.000001;

    /**
     * Test method for {@link primitives.Point#subtract(primitives.Point)}.
     * Verifies the subtraction of two points and boundary conditions.
     */
    @Test
    void testSubtract() {
        // ============ Equivalence Partitions Tests ==============
        Point p1 = new Point(1.0, 2.0, 3.0);
        Point p2 = new Point(4.0, 5.0, 6.0);
        Vector result = p1.subtract(p2);
        assertEquals(new Vector(-3.0, -3.0, -3.0), result, "Incorrect result for subtraction of points");

        // =============== Boundary Values Tests ==================
        // TC01: Subtraction of a point with itself should result in the zero vector (should throw)
        assertThrows(IllegalArgumentException.class, () -> p1.subtract(p1),
                "Subtraction of a point with the same point should result in an error - zero vector cannot be created");

        // TC02: Subtraction of a point with zero point should result in the original vector
        Point pZero = new Point(0.0, 0.0, 0.0);
        result = p1.subtract(pZero);
        assertEquals(p1, result, "Subtraction of a point with zero point should result in the original vector");

        // TC03: Subtraction of a point from zero point should result in the opposite vector
        result = pZero.subtract(p1);
        Vector oppositeP1 = new Vector(-1.0, -2.0, -3.0);
        assertEquals(oppositeP1, result, "Subtraction of a point from zero point should result in the opposite vector");
    }

    /**
     * Test method for {@link primitives.Point#add(primitives.Vector)}.
     * Verifies the addition of a vector to a point and boundary conditions.
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
        Point zeroPoint = new Point(0.0, 0.0, 0.0);
        result = zeroPoint.add(new Vector(1.0, 2.0, 3.0));
        assertEquals(p1, result, "Adding a zero vector should return the original point");
    }

    /**
     * Test method for {@link primitives.Point#distance(primitives.Point)}.
     * Verifies the calculation of the distance between two points and boundary conditions.
     */
    @Test
    void testDistance() {
        // ============ Equivalence Partitions Tests ==============
        Point p1 = new Point(1.0, 2.0, 3.0);
        Point p2 = new Point(4.0, 5.0, 6.0);
        Double result = p1.distance(p2);
        assertEquals(Math.sqrt(27.0), result, "Incorrect distance between points");

        // =============== Boundary Values Tests ==================
        // TC01: Distance from a point to itself should be 0
        Point p3 = new Point(0.0, 0.0, 0.0);
        result = p3.distance(p3);
        assertEquals(0.0, result, "Distance between a point and itself should be 0");
    }

    /**
     * Test method for {@link primitives.Point#distanceSquared(primitives.Point)}.
     * Verifies the calculation of the squared distance between two points and boundary conditions.
     */
    @Test
    void testDistanceSquared() {
        // ============ Equivalence Partitions Tests ==============
        Point p1 = new Point(1.0, 2.0, 3.0);
        Point p2 = new Point(4.0, 5.0, 6.0);
        Double result = p1.distanceSquared(p2);
        assertEquals(27.0, result, "Incorrect squared distance between points");

        // =============== Boundary Values Tests ==================
        // TC01: Squared distance from a point to itself should be 0
        Point p3 = new Point(0.0, 0.0, 0.0);
        result = p3.distanceSquared(p3);
        assertEquals(0.0, result, "Squared distance between a point and itself should be 0");
    }
}
