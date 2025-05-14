package renderer;

import primitives.*;
import scene.Scene;

import java.util.MissingResourceException;

/**
 * The {@code Camera} class represents a virtual camera in 3D space.
 * It is responsible for generating rays through pixels on a view plane.
 */
public class Camera implements Cloneable {
    private Point location = null;
    private Vector vUp = null, vTo = null, vRight = null;
    private double vpHeight = 0.0;
    private double vpWidth = 0.0;
    private double vpDistance = 0.0;

    private Camera() {
    }

    private double resolutionX = 0.0;
    private double resolutionY = 0.0;
    private int nX = 1;
    private int nY = 1;

    ImageWriter imageWriter;
    RayTracerBase rayTracer;


    public Camera renderImage() {
         for (int i = 0; i < nX; i++) {
             for (int j = 0; j < nY; j++) {
                 castRay(nX, nY, i, j);
             }
        }
         return this;
    }

    /**
     * Returns a new builder to construct a camera instance.
     *
     * @return a new {@code Builder} for creating a camera
     */
    public static Builder getBuilder() {
        return new Builder();
    }

    public Camera printGrid(int interval, Color color) {
        if (imageWriter == null) {
            throw new UnsupportedOperationException("ImageWriter is not initialized");
        }
        for (int i = 0; i < nX; i += interval) {
            for (int j = 0; j < nY; j++) {
                imageWriter.writePixel(i, j, color);
            }
        }
        return this;
    }

    public Camera writeToImage(String fileName) {
        imageWriter.writeToImage(fileName);
        return this;
    }

    /**
     * Constructs a ray from the camera through a pixel in the view plane.
     * <p>
     * The coordinate system follows geometrical conventions:
     * - Width corresponds to the X-axis (horizontal)
     * - Height corresponds to the Y-axis (vertical)
     * <p>
     * Parameter order:
     * - nX: Resolution (number of pixels) in width (X-direction)
     * - nY: Resolution (number of pixels) in height (Y-direction)
     * - j: Pixel index in width direction (X-coordinate)
     * - i: Pixel index in height direction (Y-coordinate)
     *
     * @param nX number of pixels in the width of the view plane
     * @param nY number of pixels in the height of the view plane
     * @param j  index of the pixel in the width direction (X)
     * @param i  index of the pixel in the height direction (Y)
     * @return Ray from camera through the specified pixel
     */
    public Ray constructRay(int nX, int nY, int j, int i) {
        // Calculate the center point of the view plane (Pc)
        Point centerPoint = location.add(vTo.scale(vpDistance));

        // Calculate the pixel size (ratios)
        resolutionX = vpWidth / nX;
        resolutionY = vpHeight / nY;

        // Calculate the coordinates of the pixel on the view plane
        double xj = (j - (double) (nX - 1) / 2) * resolutionX;
        double yi = -(i - (double) (nY - 1) / 2) * resolutionY;

        // Adjust the center point to get the point on view plane through which the ray passes
        Point pixelPoint = centerPoint;

        if (xj != 0) {
            pixelPoint = pixelPoint.add(vRight.scale(xj));
        }

        if (yi != 0) {
            pixelPoint = pixelPoint.add(vUp.scale(yi));
        }

        Vector direction = pixelPoint.subtract(location).normalize();
        return new Ray(location, direction);
    }

    /**
     * Builder class for constructing {@code Camera} instances with flexible configuration.
     */
    public static class Builder {
        final private Camera camera = new Camera();

        /**
         * Sets the location of the camera.
         *
         * @param newLocation the location (eye point) of the camera
         * @return this builder instance
         */
        public Builder setLocation(Point newLocation) {
            camera.location = newLocation;
            return this;
        }

        public Builder setRayTracer(Scene scene, RayTracerType rayTracer) {
            switch (rayTracer) {
                case SIMPLE:
                    camera.rayTracer = new SimpleRayTracer(scene);
                    break;
                default:
                    camera.rayTracer = null;
            }
            return this;
        }


        /**
         * Sets the direction vectors (vTo and vUp) for the camera.
         *
         * @param vTo vector toward the view plane (look direction)
         * @param vUp vector upwards from the camera (must be orthogonal to vTo)
         * @return this builder instance
         * @throws IllegalArgumentException if vTo and vUp are not orthogonal
         */
        public Builder setDirection(Vector vTo, Vector vUp) {
            if (vTo.dotProduct(vUp) != 0) {
                throw new IllegalArgumentException("the vectors given for setting direction are not orthogonal");
            }
            camera.vUp = vUp.normalize();
            camera.vTo = vTo.normalize();
            camera.vRight = vTo.crossProduct(vUp);
            return this;
        }

        /**
         * Sets the direction of the camera using a target point and an up vector.
         *
         * @param target  point the camera is looking at
         * @param tempVup temporary up vector to compute orientation
         * @return this builder instance
         */
        public Builder setDirection(Point target, Vector tempVup) {
            camera.vTo = target.subtract(camera.location).normalize();
            camera.vRight = tempVup.crossProduct(camera.vTo).normalize();
            camera.vUp = camera.vTo.crossProduct(camera.vRight);
            return this;
        }

        /**
         * Sets the direction of the camera using a target point.
         *
         * @param target point the camera is looking at
         * @return this builder instance
         */
        public Builder setDirection(Point target) {
            return setDirection(target, new Vector(0.0, 1.0, 0.0));
        }

        /**
         * Sets the view plane size (width and height).
         *
         * @param width  width of the view plane
         * @param height height of the view plane
         * @return this builder instance
         * @throws IllegalArgumentException if width or height is non-positive
         */
        public Builder setVpSize(double width, double height) {
            if (height <= 0 || width <= 0) {
                throw new IllegalArgumentException("high or width given for setting the View Plane are not positive values");
            }
            camera.vpHeight = height;
            camera.vpWidth = width;
            return this;
        }

        /**
         * Sets the distance from the camera to the view plane.
         *
         * @param distance the distance to set
         * @return this builder instance
         * @throws IllegalArgumentException if distance is non-positive
         */
        public Builder setVpDistance(double distance) {
            if (distance <= 0) {
                throw new IllegalArgumentException("distance given for setting the View Plane is not positive value");
            }
            camera.vpDistance = distance;
            return this;
        }

        /**
         * Sets the resolution of the view plane (currently not implemented).
         *
         * @param nX number of pixels in width
         * @param nY number of pixels in height
         * @return {@code null} (method is a placeholder)
         */
        public Builder setResolution(int nX, int nY) {
            if (nX <= 0 || nY <= 0) {
                throw new IllegalArgumentException("high or width given for setting the View Plane are not positive values");
            }
            camera.nY = nY;
            camera.nX = nX;
            camera.imageWriter = new ImageWriter(nX, nY);
            if (this.camera.rayTracer == null) {
                this.camera.rayTracer = new SimpleRayTracer(new Scene(null));
            }
            return this;

        }

        /**
         * Builds and returns a fully-initialized {@code Camera} instance.
         *
         * @return the configured camera
         * @throws MissingResourceException if required parameters are missing
         */
        public Camera build() {
            if (camera.nX <= 0 || camera.nY <= 0) {
                throw new IllegalArgumentException("high or width given for setting the View Plane are not positive values");
            }
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
            if (camera.vpHeight == 0.0) {
                throw new MissingResourceException(MISSING_DATA_MSG, Camera.class.getName(), "hightVP");
            }
            if (camera.vpWidth == 0.0) {
                throw new MissingResourceException(MISSING_DATA_MSG, Camera.class.getName(), "widthVP");
            }
            if (camera.vpDistance == 0.0) {
                throw new MissingResourceException(MISSING_DATA_MSG, Camera.class.getName(), "distanceFromCamera");
            }

            camera.imageWriter = new ImageWriter(camera.nX, camera.nY);
            if (this.camera.rayTracer == null) {
                this.camera.rayTracer = new SimpleRayTracer(new Scene(null));
            }


            try {
                return (Camera) camera.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException("Cloning failed unexpectedly", e);
            }
        }
    }

    /**
     * Creates and returns a deep clone of this camera using the builder.
     * @return a cloned camera
     * @throws CloneNotSupportedException if cloning fails unexpectedly
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        Camera cloned = new Camera();
        Builder builder = cloned.getBuilder()
                .setLocation(this.location)
                .setDirection(this.vTo, this.vUp)
                .setVpDistance(this.vpDistance)
                .setVpSize(this.vpWidth, this.vpHeight);
        if (this.nX > 0 && this.nY > 0) {
            builder.setResolution(this.nX, this.nY);
        }
        return builder.camera;

        }

    private void castRay(int Nx, int Ny, int column, int row){
        Color color = rayTracer.traceRay(constructRay(Nx,Ny,column,row));
        imageWriter.writePixel(column,row,color);

    }

}