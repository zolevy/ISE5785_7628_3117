package geometries;

import primitives.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a collection of geometries that can be treated as a single entity.
 * This class implements the {@link Intersectable} interface and allows for
 * adding multiple geometries to be processed together.
 */
public class Geometries implements Intersectable {

    /**
     * A list that holds all the geometries in the composite.
     */
    private final List<Intersectable> geoComposite = new LinkedList<>();

    /**
     * Default constructor to create an empty collection of geometries.
     */
    public Geometries (){}

    /**
     * Constructs a collection of geometries with the specified geometries.
     *
     * @param geometries the geometries to be added to the collection
     */
    public Geometries (Intersectable... geometries){
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
    public List<Point> findIntersections(Ray ray) {
        List<Point> intersectionPoints = null;
        for (Intersectable geometry : geoComposite) {
            List<Point> tempPoints = geometry.findIntersections(ray);
            if (tempPoints != null) {
                if (intersectionPoints == null) {
                    intersectionPoints = new LinkedList<>();
                }
                intersectionPoints.addAll(tempPoints);
            }
        }
        return intersectionPoints;
    }
}
