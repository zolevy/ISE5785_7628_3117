package geometries;

import primitives.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a general geometric object in 3D space.
 * This is an abstract base class for all geometric shapes.
 */
public abstract class Geometry implements Intersectable {

    /**
     * Calculates the normal vector to the surface at a given point.
     *
     * @param point the point on the surface of the geometry
     * @return the normal vector at the specified point
     */
    public abstract Vector getNormal(Point point);

    private final List<Intersectable> geometries = new LinkedList<>();
}
