package lighting;

import primitives.*;

abstract class Light {
    protected final Color intensity;
    protected Light(Color newIntensity)
    {
        intensity = newIntensity;
    }

    public Color getIntensity() {
        return intensity;
    }
}
