/**

 * Integration tests for the Camera class and its intersections with geometries.

package renderer;

import geometries.*;
import primitives.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration tests between Camera and Geometries (Sphere, Plane, Triangle)

public class CameraIntersectionsIntegrationTests {

    /**
     * Helper method to count the total number of intersection points between rays constructed by the camera
     * and a given geometry. It constructs a 3x3 grid of rays through the view plane.
     *
     * @param camera        the camera to construct rays from
     * @param geometry      the geometry to test intersections with
     * @param expectedCount the expected number of intersection points

    private void assertIntersections(Camera camera, Intersectable geometry, int expectedCount) {
        int nX = 3, nY = 3;
        int count = 0;
        for (int i = 0; i < nY; ++i) {
            for (int j = 0; j < nX; ++j) {
                Ray ray = camera.constructRay(nX, nY, j, i);
                List<Point> intersections = geometry.findIntersections(ray);
                if (intersections != null) {
                    count += intersections.size();
                }
            }
        }
        assertEquals(expectedCount, count);
    }

    /**
     * Test integration of camera and sphere intersections for multiple test cases:
     * TC01 - small sphere, 2 intersections
     * TC02 - large sphere encompassing camera, 18 intersections
     * TC03 - medium sphere intersecting center rays, 10 intersections
     * TC04 - very large sphere, fully covering the view plane, 9 intersections
     * TC05 - sphere behind the camera, 0 intersections

    @Test
    public void testCameraSphereIntersections() {
        Camera camera = Camera.getBuilder()
                .setLocation(new Point(0.0, 0.0, 0.0))
                .setDirection(new Vector(0.0, 0.0, -1.0), new Vector(0.0, -1.0, 0.0))
                .setVpDistance(1.0)
                .setVpSize(3.0, 3.0)
                .build();

        // TC01: small sphere 2 intersections
        assertIntersections(camera, new Sphere(1.0, new Point(0.0,0.0,-3.0)), 2);

        // TC02: big sphere - 18 intersections
        Camera camera2 = Camera.getBuilder()
                .setLocation(new Point(0.0, 0.0, 0.5))
                .setDirection(new Vector(0.0, 0.0, -1.0), new Vector(0.0, -1.0, 0.0))
                .setVpDistance(1.0)
                .setVpSize(3.0, 3.0)
                .build();

        assertIntersections(camera2, new Sphere(2.5, new Point(0.0, 0.0, -2.5)), 18);

        // TC03: medium sphere - 10 intersections
        assertIntersections(camera2, new Sphere(2.0, new Point(0.0, 0.0, -2.0)), 10);

        // TC04: large sphere covering view plane - 9 intersections
        assertIntersections(camera, new Sphere( 4.0, new Point(0.0, 0.0, -1.0)), 9);

        // TC05: sphere behind camera - 0 intersections
        assertIntersections(camera, new Sphere( 0.5, new Point(0.0, 0.0, 1.0)), 0);
    }

    /**
     * Test integration of camera and plane intersections for multiple test cases:
     * TC01 - plane perpendicular to view direction, 9 intersections
     * TC02 - plane tilted, all rays intersect, 9 intersections
     * TC03 - steeply angled plane, 6 intersections

    @Test
    public void testCameraPlaneIntersections() {
        Camera camera = Camera.getBuilder()
                .setLocation(new Point(0.0, 0.0, 0.0))
                .setDirection(new Vector(0.0, 0.0, -1.0), new Vector(0.0, -1.0, 0.0))
                .setVpDistance(1.0)
                .setVpSize(3.0, 3.0)
                .build();

        // TC01: plane against camera direction
        assertIntersections(camera, new Plane(new Point(0.0, 0.0, -5.0), new Vector(0.0, 0.0, 1.0)), 9);

        // TC02: plane tilted with all rays intersecting
        assertIntersections(camera, new Plane(new Point(0.0, 0.0, -5.0), new Vector(0.0, -0.5, 1.0)), 9);

        // TC03: steeply angled plane - 6 rays intersect
        assertIntersections(camera, new Plane(new Point(0.0, 0.0, -5.0), new Vector(0.0, -1.0, 1.0)), 6);
    }

    /**
     * Test integration of camera and triangle intersections for multiple test cases:
     * TC01 - small triangle intersected by 1 ray
     * TC02 - larger triangle intersected by 2 rays

    @Test
    public void testCameraTriangleIntersections() {
        Camera camera = Camera.getBuilder()
                .setLocation(new Point(0.0, 0.0, 0.0))
                .setDirection(new Vector(0.0, 0.0, -1.0), new Vector(0.0, -1.0, 0.0))
                .setVpDistance(1.0)
                .setVpSize(3.0, 3.0)
                .build();

        // TC01: small triangle intersected by 1 ray
        assertIntersections(camera,
                new Triangle(new Point(0.0, 1.0, -2.0), new Point(1.0, -1.0, -2.0), new Point(-1.0, -1.0, -2.0)), 1);

        // TC02: wider triangle intersected by 2 rays
        assertIntersections(camera,
                new Triangle(new Point(0.0, 20.0, -2.0), new Point(1.0, -1.0, -2.0), new Point(-1.0, -1.0, -2.0)), 2);
    }
}
*/
