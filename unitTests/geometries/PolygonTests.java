package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing Polygons
 * <p>
 * This class contains tests for the Polygon class to verify its correct behavior
 * in different scenarios.
 * </p>
 *
 * @author Dan
 */
class PolygonTests {
    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in
     * assertEquals
     */
    private static final double DELTA = 0.000001;

    /**
     * Test method for {@link geometries.Polygon#Polygon(primitives.Point...)}.
     * This test verifies that the constructor works correctly under various conditions,
     * including different types of invalid inputs.
     */
    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Correct concave quadrangular with vertices in correct order
        assertDoesNotThrow(() -> new Polygon(new Point(0.0, 0.0, 1.0),
                        new Point(1.0, 0.0, 0.0),
                        new Point(0.0, 1.0, 0.0),
                        new Point(-1.0, 1.0, 1.0)),
                "Failed constructing a correct polygon");

        // TC02: Wrong vertices order
        assertThrows(IllegalArgumentException.class,
                () -> new Polygon(new Point(0.0, 0.0, 1.0), new Point(0.0, 1.0, 0.0), new Point(1.0, 0.0, 0.0), new Point(-1.0, 1.0, 1.0)),
                "Constructed a polygon with wrong order of vertices");

        // TC03: Not in the same plane
        assertThrows(IllegalArgumentException.class,
                () -> new Polygon(new Point(0.0, 0.0, 1.0), new Point(1.0, 0.0, 0.0), new Point(0.0, 1.0, 0.0), new Point(0.0, 2.0, 2.0)),
                "Constructed a polygon with vertices that are not in the same plane");

        // TC04: Concave quadrangular
        assertThrows(IllegalArgumentException.class,
                () -> new Polygon(new Point(0.0, 0.0, 1.0), new Point(1.0, 0.0, 0.0), new Point(0.0, 1.0, 0.0),
                        new Point(0.5, 0.25, 0.5)),
                "Constructed a concave polygon");

        // =============== Boundary Values Tests ==================

        // TC10: Vertex on a side of a quadrangular
        assertThrows(IllegalArgumentException.class,
                () -> new Polygon(new Point(0.0, 0.0, 1.0), new Point(1.0, 0.0, 0.0), new Point(0.0, 1.0, 0.0),
                        new Point(0.0, 0.5, 0.5)),
                "Constructed a polygon with vertex on a side");

        // TC11: Last point = first point
        assertThrows(IllegalArgumentException.class,
                () -> new Polygon(new Point(0.0, 0.0, 1.0), new Point(1.0, 0.0, 0.0), new Point(0.0, 1.0, 0.0), new Point(0.0, 0.0, 1.0)),
                "Constructed a polygon with vertex on a side");

        // TC12: Co-located points
        assertThrows(IllegalArgumentException.class,
                () -> new Polygon(new Point(0.0, 0.0, 1.0), new Point(1.0, 0.0, 0.0), new Point(0.0, 1.0, 0.0), new Point(0.0, 1.0, 0.0)),
                "Constructed a polygon with co-located vertices");

    }

    /**
     * Test method for {@link geometries.Polygon#getNormal(primitives.Point)}.
     * This test ensures that the normal vector of the polygon is calculated correctly.
     * It checks that the normal is a unit vector and is orthogonal to all the edges of the polygon.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Simple test with a quadrilateral
        Point[] pts =
                { new Point(0.0, 0.0, 1.0), new Point(1.0, 0.0, 0.0), new Point(0.0, 1.0, 0.0), new Point(-1.0, 1.0, 1.0) };
        Polygon pol = new Polygon(pts);

        // Ensure there are no exceptions
        assertDoesNotThrow(() -> pol.getNormal(new Point(0.0, 0.0, 1.0)), "No exception should be thrown");

        // Generate the test result
        Vector result = pol.getNormal(new Point(0.0, 0.0, 1.0));

        // Ensure |result| = 1
        assertEquals(1.0, result.length(), DELTA, "Polygon's normal is not a unit vector");

        // Ensure the result is orthogonal to all the edges
        for (int i = 0; i < 3; ++i)
            assertEquals(0.0, result.dotProduct(pts[i].subtract(pts[i == 0 ? 3 : i - 1])), DELTA,
                    "Polygon's normal is not orthogonal to one of the edges");
    }

}