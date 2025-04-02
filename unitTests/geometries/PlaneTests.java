package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

class PlaneTests {

    @Test
    void testConstructor() {
        // Test constructor with three points

        Point point1 = new Point(0.0, 0.0, 2.0);
        Point point2 = new Point(0.0, 3.0, 0.0);
        Point point3 = new Point(6.0, 0.0, 0.0);

        Vector normal = new Vector(3/Math.sqrt(14), -2/Math.sqrt(14), -1/Math.sqrt(14));

        Plane plane = new Plane(point1,point2,point3);
        if(plane.getNormal().equals(normal)){
            System.out.println("The normal vector is correct");
        }


        // Test constructor with a point and a normal vector
        // normal = new Vector(0, 0, 1);
        //Plane plane2 = new Plane(p1, normal);
        //assertNotNull(plane2);
    }

    @Test
    void testGetNormal() {

    }
}