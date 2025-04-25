package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RayTests {

    @Test
    void testGetPoint() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: positive t
        Ray ray = new Ray(new Point(1.0, 0.0, 0.0), new Vector(0.0, 1.0, 0.0));
        assertEquals( ray.getPoint(5), new Point(1.0,5.0, 0.0) , "wrong result for positive t");

        // TC02: negative t
        assertEquals( ray.getPoint(-2), new Point(1.0,-2.0, 0.0) , "wrong result for negative t");

        // =============== Boundary Values Tests ==================
        //TC03:  t=0
        assertEquals( ray.getPoint(0), new Point(1.0,0.0, 0.0) , "wrong result for t=0");
    }
}