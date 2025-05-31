package lighting;

import primitives.*;

/**
 * Abstract base class for all types of lights.
 */
abstract class Light {
    protected final Color intensity;

    /**
     * Constructs a light with given intensity.
     *
     * @param newIntensity the light color/intensity
     */
    protected Light(Color newIntensity) {
        intensity = newIntensity;
    }

    /**
     * Returns the intensity/color of the light.
     */
    public Color getIntensity() {
        return intensity;
    }
}
