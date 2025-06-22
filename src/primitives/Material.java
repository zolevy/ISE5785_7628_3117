package primitives;

/**
 * Represents the material properties for a surface, including ambient, diffuse, specular coefficients and shininess.
 */
public class Material {
    /** Ambient reflection coefficient (color). Default is full (1,1,1). */
    public Double3 kA = Double3.ONE;

    /** Specular reflection coefficient (color). Default is zero. */
    public Double3 kS = Double3.ZERO;

    /** Diffuse reflection coefficient (color). Default is zero. */
    public Double3 kD = Double3.ZERO;

    /** Shininess factor for specular reflection. Default is 0 (no shininess). */
    public int nsh = 0;

    public Double3 kT = Double3.ZERO; // Transmission coefficient (not used in this example)

    public Double3 kR = Double3.ZERO; // Reflection coefficient (not used in this example)

    public Material setkT(Double3 kT) {
        this.kT = kT;
        return this;
    }

    public Material setkT(Double kT) {
        this.kT = new Double3(kT);
        return this;
    }

    public Material setkR(Double kR) {
        this.kR = new Double3(kR);
        return this;
    }

    public Material setkR(Double3 kR) {
        this.kR = kR;
        return this;
    }


    /**
     * Sets the specular coefficient using a Double3 value.
     *
     * @param kS specular coefficient
     * @return this material for chaining
     */
    public Material setKS(Double3 kS) {
        this.kS = kS;
        return this;
    }

    /**
     * Sets the specular coefficient using a single double value.
     *
     * @param kS specular coefficient
     * @return this material for chaining
     */
    public Material setKS(Double kS) {
        this.kS = new Double3(kS);
        return this;
    }

    /**
     * Sets the diffuse coefficient using a Double3 value.
     *
     * @param kD diffuse coefficient
     * @return this material for chaining
     */
    public Material setKD(Double3 kD) {
        this.kD = kD;
        return this;
    }

    /**
     * Sets the diffuse coefficient using a single double value.
     *
     * @param kD diffuse coefficient
     * @return this material for chaining
     */
    public Material setKD(Double kD) {
        this.kD = new Double3(kD);
        return this;
    }

    /**
     * Sets the shininess factor.
     *
     * @param nsh shininess exponent
     * @return this material for chaining
     */
    public Material setShininess(int nsh) {
        this.nsh = nsh;
        return this;
    }

    /**
     * Sets the ambient coefficient using a Double3 value.
     *
     * @param kA ambient coefficient
     * @return this material for chaining
     */
    public Material setkA(Double3 kA) {
        this.kA = kA;
        return this;
    }

    /**
     * Sets the ambient coefficient using a single double value.
     *
     * @param kA ambient coefficient
     * @return this material for chaining
     */
    public Material setkA(Double kA) {
        this.kA = new Double3(kA);
        return this;
    }
}
