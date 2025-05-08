package renderer;
import geometries.*;
import primitives.*;
import java.util.MissingResourceException;

public class Camera implements Cloneable{
    private Point location = null;
    private Vector vUp = null, vTo = null, vRight = null;
    private double vpHeight = 0.0;
    private double vpWidth = 0.0;
    private double vpDistance = 0.0;
    private Camera() {}
    private  double resolutionX = 0.0;
    private  double resolutionY = 0.0;
    private int nX = 0;
    private int nY = 0;


    public static Builder getBuilder()
    {
        return new Builder();
    }


    /**
     * Constructs a ray from the camera through a pixel in the view plane.
     *
     * The coordinate system follows geometrical conventions:
     * - Width corresponds to the X-axis (horizontal)
     * - Height corresponds to the Y-axis (vertical)
     *
     * Parameter order:
     * - nX: Resolution (number of pixels) in width (X-direction)
     * - nY: Resolution (number of pixels) in height (Y-direction)
     * - j: Pixel index in width direction (X-coordinate)
     * - i: Pixel index in height direction (Y-coordinate)
     *
     * @param nX number of pixels in the width of the view plane
     * @param nY number of pixels in the height of the view plane
     * @param j index of the pixel in the width direction (X)
     * @param i index of the pixel in the height direction (Y)
     * @return Ray from camera through the specified pixel
     */
    public Ray constructRay(int nX, int nY, int j, int i) {
        // Calculate the center point of the view plane (Pc)
        Point centerPoint = location.add(vTo.scale(vpDistance));

        // Calculate the pixel size (ratios)
        resolutionX = vpWidth / nX;
        resolutionY = vpHeight / nY;

        // Calculate the coordinates of the pixel on the view plane
        // First, find the distance from center to the pixel in X and Y directions
        //double xj = (j - ((double)nX) / 2) * resolutionX + resolutionX / 2;
        //double yi = (i - ((double)nY) / 2) * resolutionY + resolutionY / 2;
        double xj = (j - (double) (nX - 1) / 2) * resolutionX;
        double yi = -(i - (double) (nY - 1) / 2) * resolutionY;

        // Adjust the center point to get the point on view plane through which the ray passes
        Point pixelPoint = centerPoint;

        // Add the X component (move right)
        if (xj != 0) {
            pixelPoint = pixelPoint.add(vRight.scale(xj));
        }

        // Subtract the Y component (move up)
        // The negative sign is because the Y-coordinate increases downward in the pixel grid
        if (yi != 0) {
            pixelPoint = pixelPoint.add(vUp.scale(yi));
        }

        // Construct and return the ray from camera location through the pixel point
        Vector direction = pixelPoint.subtract(location).normalize();
        return new Ray(location, direction);
    }

    public static class Builder
    {
        final private Camera camera = new Camera();
        public Builder setLocation(Point newLocation)
        {
            camera.location = newLocation;
            return this;
        }

        public Builder setDirection(Vector vTo, Vector vUp)
        {
            if (vTo.dotProduct(vUp)!=0)
            {
                throw new IllegalArgumentException("the vectors given for setting direction are not orthogonal");
            }
            camera.vUp = vUp.normalize();
            camera.vTo = vTo.normalize();
            camera.vRight = vTo.crossProduct(vUp).normalize();

            return this;
        }

        public Builder setDirection(Point target, Vector tempVup)
        {
            camera.vTo = target.subtract(camera.location).normalize();
            camera.vRight = tempVup.crossProduct(camera.vTo).normalize();
            camera.vUp = camera.vTo.crossProduct(camera.vRight); //maybe normalize?
            return this;
        }

        public Builder setDirection(Point target)
        {
            Vector tempVup = new Vector (0.0,1.0,0.0);
            camera.vTo = target.subtract(camera.location).normalize();
            camera.vRight = tempVup.crossProduct(camera.vTo).normalize();
            camera.vUp = camera.vTo.crossProduct(camera.vRight); //maybe normalize?
            return this;
        }

        public Builder setVpSize(double width, double height)
        {
            if (height<=0||width<=0)
            {
                throw new IllegalArgumentException("high or width given for setting the View Plane are not positive values");
            }
            camera.vpHeight = height;
            camera.vpWidth = width;
            return this;
        }

        public Builder setVpDistance(double distance)
        {
            if (distance<=0)
            {
                throw new IllegalArgumentException("distance given for setting the View Plane is not positive value");
            }
            camera.vpDistance = distance;
            return this;
        }

        public Builder setResolution(int nX, int nY)
        {
            return null;
        }

        public Camera build()
        {
            final String MISSING_DATA_MSG = "missing rendering parameter";
            if (camera.location == null) {
                throw new MissingResourceException(MISSING_DATA_MSG, Camera.class.getName(), "location");
            }
            if (camera.vTo == null) {
                throw new MissingResourceException(MISSING_DATA_MSG, Camera.class.getName(), "Vto");
            }
            if (camera.vUp == null) {
                throw new MissingResourceException(MISSING_DATA_MSG, Camera.class.getName(), "Vup");
            }
            /*if (camera.Vright == null) {
                throw new MissingResourceException(MISSING_DATA_MSG, Camera.class.getName(), "Vright");
            }*/
            if (camera.vpHeight == 0.0) {
                throw new MissingResourceException(MISSING_DATA_MSG, Camera.class.getName(), "hightVP");
            }
            if (camera.vpWidth == 0.0) {
                throw new MissingResourceException(MISSING_DATA_MSG, Camera.class.getName(), "widthVP");
            }
            if (camera.vpDistance == 0.0) {
                throw new MissingResourceException(MISSING_DATA_MSG, Camera.class.getName(), "distanceFromCamera");
            }

            //maybe calculate Vright? bit it's not missing

            try {
                return (Camera) camera.clone();
            }
            catch (CloneNotSupportedException e){
                throw new RuntimeException("Cloning failed unexpectedly", e);
            }



        }
    }
    @Override
    protected Object clone() throws CloneNotSupportedException{
        Camera cloned = new Camera();
        Builder builder = cloned.getBuilder()
                .setLocation(this.location)
                .setDirection(this.vTo, this.vUp)
                .setVpDistance(this.vpDistance)
                .setVpSize(this.vpWidth, this.vpHeight);
        if (this.nX > 0 && this.nY > 0)
        {
            builder.setResolution(this.nX, this.nY);
        }
        return builder.camera;

    }
}
