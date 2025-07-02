package geometries;

import primitives.Ray;

import java.util.List;

/**
 * Interface defining methods to build bounding volumes for a list of geometries
 * and to find intersections between a ray and these geometries.
 */
public interface BoundingVolumeBuilder {
    /**
     * Creates a bounding box that encloses the given list of geometries.
     * @param geometries The list of geometries to enclose.
     * @return The created Axis-Aligned Bounding Box (AABB).
     */
    AABB createBoundingBox(List<Intersectable> geometries);

    /**
     * Finds the intersections between a ray and a list of geometries.
     * @param ray The ray to test intersections with.
     * @param geometries The list of geometries to test.
     * @return A list of intersections found between the ray and geometries.
     */
    List<Intersectable.Intersection> findIntersections(Ray ray, List<Intersectable> geometries);
}
