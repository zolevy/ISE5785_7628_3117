package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GeometriesTests {

    @Test
    void testFindIntersections() {
        // Create a Geometries object
        Geometries geometries = new Geometries();

        // Add some geometries to the Geometries object
        geometries.add(new Sphere(1.0, new Point(0.0, 0.0, 0.0)));
        geometries.add(new Triangle(new Point(1.0, 1.0, 1.0), new Point(2.0, 2.0, 2.0), new Point(3.0, 3.0, 3.0)));

        // Create a ray that intersects with the geometries
        Ray ray = new Ray(new Point(0.0, 0.0, -1.0), new Vector(0.0, 0.0, 1.0));


        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray's line is outside the sphere (0 points)
        List<Point> intersections = geometries.findIntersections(ray);
        geometries.add(new Plane(new Point(0.0, 0.0, 2.0), new Vector(0.0, 0.0, 1.0)));

        // Check if the intersections are not null and have the expected size
        assertNotNull(intersections);
        assertEquals(3, intersections.size(), "Expected three intersection points");



        // =============== Boundary Values Tests ==================
        // TC12: Ray starts at sphere and goes outside (0 points)
        geometries.add(new Plane(new Point(0.0, 0.0, -5.0), new Vector(0.0, 0.0, -1.0)));

        assertNotNull(intersections);
        assertEquals(3, intersections.size(), "Expected three intersection points");

        // TC12: Empty Geometries collection should return null
        Geometries geometries2 = new Geometries(); // This creates an empty composite
        Ray ray2 = new Ray(new Point(0.0, 0.0, 0.0), new Vector(1.0, 0.0, 0.0)); // Any ray

        assertNull(geometries2.findIntersections(ray2), "Expected null for an empty geometries list");

        // TC13: Ray starts at sphere and goes inside (1 points)
        // A sphere far above the ray path
        geometries2.add(new Sphere(1.0, new Point(0.0, 5.0, 0.0)));
        geometries2.add(new Triangle(new Point(-5.0, -3.0, 1.0), new Point(-6.0, -3.0, 2.0), new Point(-5.5, -2.0, 1.5)));
        geometries2.add(new Plane(new Point(0.0, 0.0, 5.0), new Vector(0.0, 0.0, 1.0)));

        assertNull(geometries2.findIntersections(ray2), "Expected null, there are no intersections");


        //TC14: Ray starts at sphere and goes inside (1 points)
        geometries2.add(new Plane(new Point(5.0, 0.0, 0.0), new Vector(-1.0, 0.0, 0.0)));
        assertNotNull(geometries2.findIntersections(ray2));
        assertEquals(1, intersections.size(), "Expected one intersection points");

    }
}