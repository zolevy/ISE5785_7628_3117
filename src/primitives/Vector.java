package primitives;

import static primitives.Util.isZero;

public class Vector extends Point{
    @Override
    public boolean equals(Object obj){
        if(this == obj) return true;
        return (obj instanceof Vector other)
                && this.xyz.equals(other.xyz); //call the equals method of Double3 or Point?
    }

    public Vector(Double x, Double y, Double z){
        super(x, y, z);
        /*
        Double3 newDouble = new Double3(x, y, z);
        super(newDouble);
        if (newDouble.equals(Double3.ZERO))
        {
            throw new IllegalArgumentException("Vector cannot be created from Point(0,0,0)");
        }*/
        if (x == 0 && y == 0 && z == 0) {
            throw new IllegalArgumentException("Vector cannot be created from Point(0,0,0)");
        }
    }

    public Vector(Double3 values){
        super(values); // check if it is supposed to be here
        if (values.equals(Double3.ZERO)) {
            throw new IllegalArgumentException("Vector cannot be created from Point(0,0,0)");
        }
        //super(values);
    }

    @Override
    public String toString() {
        return String.format(xyz.toString()); //call the equals method of Double3 or Point?
    }

    public Double lengthSquared(){
        return this.dotProduct(this);
    }

    public Double length(){
        return Math.sqrt(this.lengthSquared());
    }

    public Vector add(Vector vector){
        return new Vector(this.xyz.add(vector.xyz));
    }

    public Vector scale(Double scalar){
        return new Vector(this.xyz.scale(scalar));
    }

    public Double dotProduct(Vector vector){
        return this.xyz.d1() * vector.xyz.d1() + this.xyz.d2() * vector.xyz.d2() + this.xyz.d3() * vector.xyz.d3();
    }

    public Vector crossProduct(Vector vector){

        return new Vector(this.xyz.d2() * vector.xyz.d3() - this.xyz.d3() * vector.xyz.d2(),
                this.xyz.d3() * vector.xyz.d1() - this.xyz.d1() * vector.xyz.d3(),
                this.xyz.d1() * vector.xyz.d2() - this.xyz.d2() * vector.xyz.d1());
    }

    public Vector normalize(){
        return this.scale(1/(this.length()));
    }
}
