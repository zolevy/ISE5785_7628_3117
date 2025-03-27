package geometries;

public abstract class RadialGeometry extends Geometry {
    protected final Double radius;

    public RadialGeometry(Double radius) {
        this.radius = radius;
    }
}
