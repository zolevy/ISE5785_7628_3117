package geometries;

import primitives.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a general geometric object in 3D space.
 * This is an abstract base class for all geometric shapes.
 * It defines the method for calculating the normal vector at a given point
 * and can be extended by specific geometric shapes.
 */
public abstract class Geometry implements Intersectable {

    /**
     * Calculates the normal vector to the surface at a given point.
     * The normal vector is used to represent the direction perpendicular to the surface
     * at the specified point.
     *
     * @param point the point on the surface of the geometry
     * @return the normal vector at the specified point
     */
    public abstract Vector getNormal(Point point);

    /**
     * A list that holds other intersectable geometries.
     * This is not used in this class but can be used in subclasses for managing multiple geometries.
     */
    private final List<Intersectable> geometries = new LinkedList<>();
}
