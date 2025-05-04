package geometries;

import primitives.*;
import java.util.List;

/**
 * Interface representing an object that can be intersected by a ray.
 * Classes implementing this interface must provide a method to find intersection points with a ray.
 */
public interface Intersectable {

    /**
     * Finds the intersection points between a ray and the object.
     *
     * @param ray the ray to intersect with the object
     * @return a list of intersection points, or null if there are no intersections
     */
    public List<Point> findIntersections(Ray ray);
}
