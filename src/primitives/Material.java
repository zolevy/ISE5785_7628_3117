package primitives;

public class Material {
    public Double3 kA = Double3.ONE;

    public Material setkA(Double3 kA) {
        this.kA = kA;
        return this;
    }
    public Material setkA(Double kA) {
        Double3 tempKA = new Double3(kA);
        this.kA = tempKA;
        return this;
    }
}
