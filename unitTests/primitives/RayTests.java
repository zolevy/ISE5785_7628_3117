package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RayTests {

    @Test
    void testGetPoint() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray's line is outside the sphere (0 points)

        Ray ray = new Ray(new Point(1.0, 0.0, 0.0), new Vector(0.0, 1.0, 0.0));
        assertEquals( ray.getPoint(5), new Point(1.0,5.0, 0.0) , "");

        // TC02: Ray's line is inside the sphere (0 points)
        assertEquals( ray.getPoint(-2), new Point(1.0,-2.0, 0.0) , "");

        // =============== Boundary Values Tests ==================
        assertEquals( ray.getPoint(0), new Point(1.0,0.0, 0.0) , "");
    }
}