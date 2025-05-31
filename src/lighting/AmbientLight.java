package lighting;

import primitives.Color;

/**
 * Represents ambient light with a constant intensity illuminating the entire scene equally.
 */
public class AmbientLight extends Light {
    /**
     * Represents no ambient light (black).
     */
    final public static AmbientLight NONE = new AmbientLight(Color.BLACK);

    /**
     * Constructs an ambient light with the given color intensity.
     *
     * @param ambientIntensity the color/intensity of the ambient light
     */
    public AmbientLight(Color ambientIntensity) {
        super(ambientIntensity);
    }
}
