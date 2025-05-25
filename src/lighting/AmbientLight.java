package lighting;

import primitives.Color;

public class AmbientLight extends Light {
    final public static AmbientLight NONE = new AmbientLight(Color.BLACK);
    public AmbientLight(Color ambientIntensity)
    {
        super(ambientIntensity);
    }
}
