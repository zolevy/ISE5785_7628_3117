package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Geometries class, specifically the findIntersections method.
 * It includes tests for various cases of ray intersections with a collection of geometries.
 */
class GeometriesTests {

    /**
     * Tests the findIntersections method of the Geometries class.
     * This test covers different cases of ray intersections with various geometries in the collection.
     */
    @Test
    void testFindIntersections() {
        // Create a Geometries object
        Geometries geometries = new Geometries();

        // Add valid geometries
        geometries.add(
                new Sphere(1.0, new Point(0.0, 0.0, 2.0)), // Intersected
                new Triangle(                         // Intersected
                        new Point(-1.0, 1.0, 3.0),
                        new Point(1.0, 1.0, 3.0),
                        new Point(0.0, -1.0, 3.0))
        );

        // Create a ray that intersects with the geometries
        Ray ray = new Ray(new Point(0.0, 0.0, 0.0), new Vector(0.0, 0.0, 1.0));

        // ============ Equivalence Partitions Tests ==============
        // TC01: Some geometries intersect (expecting 3 points: 2 from sphere, 1 from triangle)
        List<Point> intersections = geometries.findIntersections(ray);
        assertNotNull(intersections);
        assertEquals(3, intersections.size(), "Expected three intersection points");

        // =============== Boundary Values Tests ==================
        // TC11: Add a geometry that does NOT intersect
        geometries.add(new Plane(new Point(0.0, 0.0, -5.0), new Vector(0.0, -1.0, 0.0))); // Parallel to ray
        intersections = geometries.findIntersections(ray);
        assertNotNull(intersections);
        assertEquals(3, intersections.size(), "Expected still three intersection points");

        // TC12: Empty collection → should return null
        Geometries geometries2 = new Geometries();
        Ray ray2 = new Ray(new Point(1.0, 1.0, 1.0), new Vector(1.0, 0.0, 0.0));
        assertNull(geometries2.findIntersections(ray2), "Expected null for empty geometries");

        // TC13: No geometry intersects
        geometries2.add(
                new Sphere(0.5, new Point(0.0, 5.0, 0.0)),
                new Triangle(
                        new Point(-5.0, 0.0, 3.0),
                        new Point(-6.0, -1.0, 3.0),
                        new Point(-5.5, -0.5, 4.0)),
                new Plane(new Point(0.0, 0.0, 10.0), new Vector(0.0, 1.0, 0.0))
        );
        assertNull(geometries2.findIntersections(ray2), "Expected null – no intersections");

        // TC14: Only one geometry intersects
        geometries2.add(new Plane(new Point(2.0, 1.0, 1.0), new Vector(-1.0, 0.0, 0.0))); // Ray hits it at x=2
        intersections = geometries2.findIntersections(ray2);
        assertNotNull(intersections);
        assertEquals(1, intersections.size(), "Expected one intersection point");
    }

}
