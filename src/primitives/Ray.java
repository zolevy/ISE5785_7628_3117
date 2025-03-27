package primitives;

public class Ray{
    private final Point head;
    private final Vector direction;

    public Ray(Point head, Vector direction){
        this.head = head;
        this.direction = direction.normalize();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Ray other)
                && this.head.equals(other.head) //equals method of Point
                && this.direction.equals(other.direction); //equals method of Vector
    }

    @Override
    public String toString() {
        return "Ray(head=" + head + ", direction=" + direction + ")";//check if the format correct
    }
}
