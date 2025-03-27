package geometries;

import primitives.*;

public class Tube extends RadialGeometry {
    protected final Ray axis;

    public Tube(Double radius, Ray axis) {
        super(radius);
        this.axis = axis;
    }

    @Override
    public Vector getNormal (Point q) {
        return null;
    }
}
