package geometries;
import primitives.*;

public class Sphere extends RadialGeometry{
    private final Point center;

    public Sphere(Double radius, Point center) {
        super(radius);
        this.center = center;
    }

    public Vector getNormal (Point q) {
        return null;
    }


}
