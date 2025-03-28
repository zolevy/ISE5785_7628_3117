package geometries;

import primitives.*;

/**
 * Represents a cylinder in 3D space, defined by a central axis and a height.
 * A cylinder is a special case of a tube with a finite height.
 */
public class Cylinder extends Tube {

    /**
     * The height of the cylinder.
     */
    private final Double height;

    /**
     * Constructs a cylinder with a specified radius, axis, and height.
     *
     * @param radius the radius of the cylinder
     * @param axis the central axis of the cylinder
     * @param height the height of the cylinder
     */
    public Cylinder(Double radius, Ray axis, Double height) {
        super(radius, axis);
        this.height = height;
    }

    /**
     * This method is not implemented for Cylinder.
     *
     * @param q a point on the surface of the cylinder
     * @return always returns null
     */
    @Override
    public Vector getNormal(Point q) {
        return null;
    }
}
