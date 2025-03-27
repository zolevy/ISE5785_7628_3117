package primitives;

public class Point {
    protected final Double3 xyz;
    public static final ZERO = new Point(0, 0, 0);

    public Point(Double x, Double y, Double z) {
        this.xyz = new Double3(x, y, z);
    }

    public Point(Double3 point){
        this.xyz = point;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Point other)
                && this.xyz.equals(other.xyz);
    }

    @Override
    public String toString() {
        return String.format(xyz.toString());
    }

    public Vector subtract(Point point) {
        return new Vector(this.xyz.subtract(point.xyz));
    }

    public Point add(Vector vector) {
        Point newPoint = new Point (this.xyz.add(vector.xyz)); //call the add method of Double3.
        return newPoint;//check if the Point newPoint is needed.
    }

    public Double distance(Point point) {
        return (Math.sqrt(distanceSquared(point)));
    }

    public Double distanceSquared(Point point) {
        return ((point.xyz.d1() - this.xyz.d1())*(point.xyz.d1() - this.xyz.d1()) + (point.xyz.d2() - this.xyz.d2())*(point.xyz.d2() - this.xyz.d2()) + (point.xyz.d3() - this.xyz.d3())*(point.xyz.d3() - this.xyz.d3()));//complete.
    }
}
