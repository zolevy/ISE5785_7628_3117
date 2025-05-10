package lighting;

import primitives.Color;

public class AmbientLight {
    final private Color intensity;
    AmbientLight(Color ambientIntensity)
    {
        intensity = ambientIntensity;
    }
    final public static AmbientLight NONE = new AmbientLight(Color.BLACK);
    public Color getIntensity() { return intensity; }
}
