package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.LinkedList;
import java.util.List;

/**
 * Implementation of BoundingVolumeBuilder that creates a single bounding box
 * enclosing all geometries (CBR - Coarse Bounding Rectangle).
 */
public class CBRBoundingBoxBuilder implements BoundingVolumeBuilder {

    /**
     * Creates a bounding box that encloses all provided geometries.
     * @param geometries The list of geometries to enclose.
     * @return The bounding box enclosing all geometries, or null if empty.
     */
    @Override
    public AABB createBoundingBox(List<Intersectable> geometries) {
        double minX = Double.POSITIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double minZ = Double.POSITIVE_INFINITY;

        double maxX = Double.NEGATIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        double maxZ = Double.NEGATIVE_INFINITY;

        for (Intersectable geometry : geometries) {
            AABB box = geometry.createBoundingBox();
            if (box == null) continue;

            Point bMin = box.min;
            Point bMax = box.max;

            minX = Math.min(minX, bMin.getX());
            minY = Math.min(minY, bMin.getY());
            minZ = Math.min(minZ, bMin.getZ());

            maxX = Math.max(maxX, bMax.getX());
            maxY = Math.max(maxY, bMax.getY());
            maxZ = Math.max(maxZ, bMax.getZ());
        }

        if (minX == Double.POSITIVE_INFINITY) return null;

        return new AABB(new Point(minX, minY, minZ), new Point(maxX, maxY, maxZ));
    }

    /**
     * Finds intersections between the ray and the list of geometries by checking bounding boxes first.
     * @param ray The ray to test.
     * @param geometries The list of geometries to test.
     * @return List of intersections found or null if none.
     */
    @Override
    public List<Intersectable.Intersection> findIntersections(Ray ray, List<Intersectable> geometries) {
        List<Intersectable.Intersection> result = null;

        for (Intersectable geometry : geometries) {
            AABB box = geometry.getBoundingBox();
            if (box != null && !box.intersects(ray)) {
                continue;
            }
            var temp = geometry.calculateIntersections(ray);
            if (temp != null) {
                if (result == null) result = new LinkedList<>();
                result.addAll(temp);
            }
        }

        return result;
    }

}
