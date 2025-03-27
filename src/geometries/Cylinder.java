package geometries;
import primitives.*;

import primitives.Point;
import primitives.Vector;

public class Cylinder extends Tube{
    private final Double height;

    public Cylinder (Double radius, Ray axis, Double height) {
        super(radius, axis);
        this.height = height;
    }

    @Override
    public Vector getNormal (Point q) {
        return null;
    }

}
