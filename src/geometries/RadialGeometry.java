package geometries;

/**
 * Represents a geometric object with a radial shape, such as a sphere or a cylinder.
 * This is an abstract base class for all geometries that have a radius.
 */
public abstract class RadialGeometry extends Geometry {

    /**
     * The radius of the geometric shape.
     */
    protected final Double radius;

    /**
     * Constructs a radial geometry with the specified radius.
     *
     * @param radius the radius of the geometry
     */
    public RadialGeometry(Double radius) {
        this.radius =radius;
    }
}
