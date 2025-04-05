package geometries;

import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

class TriangleTest {
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: test if the normal that was generated is normalized and orthogonal to all the edges
        // ensure there are no exceptions
        Point p1 = new Point(0.0, 0.0, 1.0);
        Point p2 = new Point(3.0, 4.0, 5.0);
        Point p3 = new Point(6.0, 7.0, 8.0);
        Trinagle triangle = new Triangle(p1, p2, p3);
        //(maybe possible to keep DRY)

        // Generate the test result
        Vector result = triangle.getNormal(p1);
        // Ensure |result| = 1 (normal is a unit vector)
        assertEquals(1.0, result.length(), DELTA, "triangle's normal is not a unit vector");
        // Ensure the result is orthogonal to all edges
        Vector edge1 = p2.subtract(p1); // Vector between p1 and p2
        Vector edge2 = p3.subtract(p1); // Vector between p1 and p3
        Vector edge3 = p3.subtract(p2); // Vector between p2 and p3
        // Check orthogonality with edge1
        assertEquals(0.0, result.dotProduct(edge1), DELTA, "Normal is not orthogonal to all edges");
        // Check orthogonality with edge2
        assertEquals(0.0, result.dotProduct(edge2), DELTA, "Normal is not orthogonal to all edges");
        // Check orthogonality with edge3
        assertEquals(0.0, result.dotProduct(edge3), DELTA, "Normal is not orthogonal to all edges");
    }

}