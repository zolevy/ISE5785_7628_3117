package geometries;

import primitives.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a collection of geometries that can be treated as a single entity.
 * This class implements the {@link Intersectable} interface and allows for
 * adding multiple geometries to be processed together.
 */
public class Geometries extends Intersectable {

    /**
     * A list that holds all the geometries in the composite.
     */
    private final List<Intersectable> geoComposite = new LinkedList<>();

    /**
     * Default constructor to create an empty collection of geometries.
     */
    public Geometries() {}

    /**
     * Constructs a collection of geometries with the specified geometries.
     *
     * @param geometries the geometries to be added to the collection
     */
    public Geometries(Intersectable... geometries) {
        add(geometries);
    }

    /**
     * Adds one or more geometries to the collection.
     *
     * @param geometries the geometries to be added
     */
    public void add(Intersectable... geometries) {
        for (Intersectable geometry : geometries) {
            geoComposite.add(geometry);
        }
    }

    /**
     * Finds the intersections of the ray with all geometries in the collection.
     *
     * @param ray the ray to check for intersections
     * @return a list of points where the ray intersects the geometries in the collection
     */
    @Override
    public List<Intersection> calculateIntersectionsHelper(Ray ray) {
        List<Intersection> intersectionPoints = null;

        for (Intersectable geometry : geoComposite) {
            // Use bounding box check only if AABB optimization is enabled
            AABB box = geometry.getBoundingBox();
            if (box != null && !box.intersects(ray)) {
                continue; // Ray doesn't hit the bounding box – skip geometry
            }
            List<Intersection> tempPoints = geometry.calculateIntersectionsHelper(ray);
            if (tempPoints != null) {
                if (intersectionPoints == null) {
                    intersectionPoints = new LinkedList<>();
                }
                intersectionPoints.addAll(tempPoints);
            }
        }
        return intersectionPoints;
    }


    /**
     * Creates the axis-aligned bounding box (AABB) that contains all bounding boxes of
     * the geometries in the collection.
     *
     * @return AABB enclosing all geometries or null if none have bounding boxes.
     */
    @Override
    public AABB createBoundingBox() {
        if (geoComposite.isEmpty()) {
            return null;
        }

        double minX = Double.POSITIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double minZ = Double.POSITIVE_INFINITY;

        double maxX = Double.NEGATIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        double maxZ = Double.NEGATIVE_INFINITY;

        for (Intersectable geometry : geoComposite) {
            AABB box = geometry.createBoundingBox();
            if (box == null) continue;

            Point bMin = box.min;
            Point bMax = box.max;

            if (bMin.getX() < minX) minX = bMin.getX();
            if (bMin.getY() < minY) minY = bMin.getY();
            if (bMin.getZ() < minZ) minZ = bMin.getZ();

            if (bMax.getX() > maxX) maxX = bMax.getX();
            if (bMax.getY() > maxY) maxY = bMax.getY();
            if (bMax.getZ() > maxZ) maxZ = bMax.getZ();
        }

        if (minX == Double.POSITIVE_INFINITY) {
            return null;
        }

        Point min = new Point(minX, minY, minZ);
        Point max = new Point(maxX, maxY, maxZ);

        return new AABB(min, max);
    }
}
