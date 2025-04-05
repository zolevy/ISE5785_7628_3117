package primitives;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VectorTests {
    private static final double DELTA = 0.000001;

    // Test for Vector constructor
    @Test
    void testConstructor() {
        // Test with non-zero vector
        Vector v = new Vector(1.0, 2.0, 3.0);
        assertNotNull(v, "Vector should be created successfully for non-zero values");

        // Test with zero vector
        boolean flag = false;
        try {
            Vector zero = new Vector (0.0,0.0,0.0);
        }
        catch (Exception e) {
            flag = true;
        }
        assertEquals(true, flag, "Vector cannot be created with zero values");
    }

    // Test for lengthSquared method
    @Test
    void testLengthSquared() {
        // ============ Equivalence Partitions Tests ==============
        Vector v = new Vector(3.0, 4.0, 0.0);
        assertEquals(25.0, v.lengthSquared(), DELTA, "Length squared of vector is incorrect");

        // =============== Boundary Values Tests ==================
        // TC02 : test vector with negative values
        Vector negativeVector = new Vector (-3.0,-4.0,0.0);
        assertEquals(25.0, v.lengthSquared(), DELTA, "Length squared of vector is incorrect");
    }

    // Test for length method
    @Test
    void testLength() {
        // ============ Equivalence Partitions Tests ==============
        Vector v = new Vector(3.0, 4.0, 0.0);
        assertEquals(5.0, v.length(), DELTA, "Length of vector is incorrect");
        // =============== Boundary Values Tests ==================
        // TC02 : test vector with negative values
        Vector negativeVector = new Vector (-3.0,-4.0,0.0);
        assertEquals(5.0, v.length(), DELTA, "Length of vector is incorrect");
    }

    // Test for add method
    @Test
    void testAdd() {
        // ============ Equivalence Partitions Tests ==============
        //TC01: test addition of two vectors
        Vector v1 = new Vector(1.0, 2.0, 3.0);
        Vector v2 = new Vector(4.0, 5.0, 6.0);
        Vector result = v1.add(v2);
        assertEquals(new Vector(5.0, 7.0, 9.0), result, "Vector addition is incorrect");

        // =============== Boundary Values Tests ==================
        //TC02: test that when vector is added to it's opposite exception was thrown
        Vector v1Opposite = v1.scale(-1.0);
        boolean flag = false;
        try {
            result = v1.add(v1Opposite);
        } catch (Exception e) {
            flag = true;
        }
        assertEquals(true, flag, "Vector addition is incorrect, should create a zero vector which can't exist");
    }

    // Test for scale method
    @Test
    void testScale() {
        // ============ Equivalence Partitions Tests ==============
        //TC01: test scaling a vector
        Vector v = new Vector(1.0, 2.0, 3.0);
        Vector result = v.scale(2.0);
        assertEquals(new Vector(2.0, 4.0, 6.0), result, "Vector scaling is incorrect");
        // =============== Boundary Values Tests ==================
        //TC02: test that when vector is scaled with zero an exception is thrown
        boolean flag = false;
        try {
            result = v.scale(0.0);
        } catch (Exception e) {
            flag = true;
        }
        assertEquals(true, flag, "Vector scaling is incorrect, should create a zero vector which can't exist");
    }

    // Test for dotProduct method
    @Test
    void testDotProduct() {
        // ============ Equivalence Partitions Tests ==============
        Vector v1 = new Vector(1.0, 2.0, 3.0);
        Vector v2 = new Vector(4.0, 5.0, 6.0);
        assertEquals(32.0, v1.dotProduct(v2), DELTA, "Dot product calculation is incorrect");

        // =============== Boundary Values Tests ==================
        // TC01: Dot product of two parallel vectors
        Vector vParallel = new Vector(2.0, 4.0, 6.0);
        assertEquals(32.0, v1.dotProduct(vParallel), DELTA, "Dot product of parallel vectors is incorrect");

        // TC02: Dot product of a vector with itself
        assertEquals(v1.lengthSquared(), v1.dotProduct(v1), DELTA, "Dot product of vector with itself is incorrect");

        // TC03: Dot product of orthogonal vectors (should be zero)
        Vector vOrthogonal = new Vector(-2.0, 1.0, 0.0);
        assertEquals(0, v1.dotProduct(vOrthogonal), DELTA, "Dot product of orthogonal vectors is incorrect");
    }

    // Test for crossProduct method
    @Test
    void testCrossProduct() {
        // ============ Equivalence Partitions Tests ==============
        Vector v1 = new Vector(1.0, 2.0, 3.0);
        Vector v2 = new Vector(4.0, 5.0, 6.0);
        assertEquals(new Vector(-3.0, 6.0, -3.0), v1.crossProduct(v2), "Cross product calculation is incorrect");

        // =============== Boundary Values Tests ==================
        Vector result;
        // TC01: Cross product of two parallel vectors (result should be zero vector)
        Vector vParallel = new Vector(2.0, 4.0, 6.0);
        boolean flag = false;
        try {
            result = v1.crossProduct(vParallel);
        }
        catch (Exception e) {
            flag = true;
        }
        assertEquals(true, flag, "Cross Product is incorrect, should create a zero vector which can't exist");


        // TC02: Cross product of orthogonal vectors (should give a vector perpendicular to both)
        Vector vOrthogonal = new Vector(1.0, 0.0, 0.0);
        Vector resultOrthogonal = v1.crossProduct(vOrthogonal);
        assertEquals(new Vector(0.0, -3.0, 2.0), resultOrthogonal, DELTA, "Cross product of orthogonal vectors is incorrect");

        // TC03: Cross product of a vector with itself (should result in zero vector)
        flag = false;
        try {
            result = v1.crossProduct(v1);
        }
        catch (Exception e) {
            flag = true;
        }
        assertEquals(true, flag, "Cross Product is incorrect, should create a zero vector which can't exist");

        //TC04: Cross product of a vector with it's opposite
        Vector v1Opposite = v1.scale(-1.0);
        flag = false;
        try {
            result = v1.crossProduct(v1Opposite);
        }
        catch (Exception e) {
            flag = true;
        }
        assertEquals(true, flag, "Cross Product is incorrect, should create a zero vector which can't exist");
    }

    // Test for normalize method
    @Test
    void testNormalize() {
        // ============ Equivalence Partitions Tests ==============
        //TC01: test normalization  of a vector
        Vector v = new Vector(3.0, 4.0, 0.0);
        Vector normalized = v.normalize();
        Vector reallyNormalized = new Vector(0.6, 0.8, 0.0);
        assertEquals(reallyNormalized, normalized, DELTA, "Normalization of vector is incorrect");

        // =============== Boundary Values Tests ==================
        //TC02: test normalization of already normalized vector
        normalized = reallyNormalized.normalize();
        assertEquals(reallyNormalized, normalized, DELTA, "Normalization of vector is incorrect");
    }

    @Test
    void testSubtract() {
        // ============ Equivalence Partitions Tests ==============
        Vector v1 = new Vector(5.0, 6.0, 7.0);
        Vector v2 = new Vector(2.0, 3.0, 4.0);
        Vector result = v1.subtract(v2);
        assertEquals(new Vector(3.0, 3.0, 3.0), result, DELTA, "Vector subtraction is incorrect");

        // =============== Boundary Values Tests ==================
        // TC01: Test with a vector and the zero vector (expecting the same vector as result)
        Vector vZero = new Vector(0.0, 0.0, 0.0);
        result = v1.subtract(vZero);
        assertEquals(v1, result, DELTA, "Subtraction with zero vector failed");

        // TC02: Test subtraction of a vector from itself (should result in the zero vector)
        boolean flag = false;
        try {
            result = v1.subtract(v1);
        } catch (Exception e) {
            flag = true;
        }
        assertEquals(true, flag, "Vector subtracting is incorrect, should create a zero vector which can't exist");
    }

}
