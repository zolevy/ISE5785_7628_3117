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

    private static BoundingVolumeBuilder boundingBoxBuilder = null;

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
     * Sets the global bounding volume builder strategy for all {@code Geometries} instances.
     * This method allows selecting a specific algorithm (e.g., BVH or CBR) to be used when
     * constructing bounding boxes and finding intersections.
     * <p>
     * This setting affects all geometries that use the static builder.
     *
     * @param builder the bounding volume builder to use (e.g., {@link BVHBoundingBoxBuilder}, {@link CBRBoundingBoxBuilder})
     */
    public static void setBoundingVolumeBuilder(BoundingVolumeBuilder builder) {
        boundingBoxBuilder = builder;
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
        if (boundingBoxBuilder == null) {
            List<Intersection> intersectionPoints = null;
            for (Intersectable geometry : geoComposite) {
                List<Intersection> tempPoints = geometry.calculateIntersectionsHelper(ray);
                if (tempPoints != null) {
                    if (intersectionPoints == null) {
                        intersectionPoints = new LinkedList<>();
                    }
                    intersectionPoints.addAll(tempPoints);
                }
            }
            return intersectionPoints;
        } else {
            return boundingBoxBuilder.findIntersections(ray, geoComposite);
        }
    }

    /**
     * Creates the axis-aligned bounding box (AABB) that contains all bounding boxes of
     * the geometries in the collection.
     *
     * @return AABB enclosing all geometries or null if none have bounding boxes.
     */
    @Override
    protected AABB createBoundingBox() {
        return boundingBoxBuilder.createBoundingBox(geoComposite);
    }

}
