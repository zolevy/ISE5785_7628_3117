package renderer;

import primitives.*;
import scene.Scene;

import java.util.MissingResourceException;

/**
 * The {@code Camera} class represents a virtual camera in 3D space.
 * It is responsible for generating rays through pixels on a view plane.
 */
public class Camera implements Cloneable {
    /** Location of the camera in 3D space */
    private Point location = null;

    /** Up, to, and right direction vectors of the camera */
    private Vector vUp = null, vTo = null, vRight = null;

    /** View plane height, width and distance from the camera */
    private double vpHeight = 0.0;
    private double vpWidth = 0.0;
    private double vpDistance = 0.0;

    /** Private constructor for use by the builder */
    private Camera() {
    }

    /** View plane resolution in X and Y directions */
    private double resolutionX = 0.0;
    private double resolutionY = 0.0;

    /** Number of pixels in width and height of view plane */
    private int nX = 1;
    private int nY = 1;

    /** Image writer for rendering pixels */
    ImageWriter imageWriter;

    /** Ray tracer for computing color of rays */
    RayTracerBase rayTracer;

    /**
     * Renders the image by casting rays through each pixel.
     *
     * @return this camera instance
     */
    public Camera renderImage() {
        for (int i = 0; i < nY; ++i) {
            for (int j = 0; j < nX; ++j) {
                castRay(nX, nY, j, i);
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

    /**
     * Prints a grid over the rendered image with the specified interval and color.
     *
     * @param interval interval between grid lines
     * @param color    color of the grid lines
     * @return this camera instance
     */
    public Camera printGrid(int interval, Color color) {
        boolean grid = true;
        for (int yIndex = 0; yIndex < nY; yIndex++) {
            if (yIndex % interval == 0) {
                grid = true;
            } else
                grid = false;
            for (int xIndex = 0; xIndex < nX; xIndex++) {
                if (grid == true) {
                    imageWriter.writePixel(xIndex, yIndex, color);
                } else if (xIndex % interval == 0) {
                    imageWriter.writePixel(xIndex, yIndex, color);
                }
            }
        }
        return this;
    }

    /**
     * Writes the rendered image to a file.
     *
     * @param fileName name of the output image file
     * @return this camera instance
     */
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
        Point centerPoint = location.add(vTo.scale(vpDistance));
        resolutionX = vpWidth / nX;
        resolutionY = vpHeight / nY;

        double xj = (j - (double) (nX - 1) / 2) * resolutionX;
        double yi = -((i - (double) (nY - 1) / 2) * resolutionY);

        Point pixelPoint = centerPoint;

        if (!Util.isZero(xj)) {
            pixelPoint = pixelPoint.add(vRight.scale(xj));
        }

        if (!Util.isZero(yi)) {
            pixelPoint = pixelPoint.add(vUp.scale(yi));
        }

        if (pixelPoint.equals(location)) {
            throw new IllegalArgumentException("Ray direction cannot be a zero vector (origin equals target)");
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

        /**
         * Sets the image writer for the camera.
         *
         * @param imageWriter the image writer to use
         * @return this builder instance
         */
        public Builder setImageWriter(ImageWriter imageWriter) {
            camera.imageWriter = imageWriter;
            return this;
        }

        /**
         * Sets the ray tracer and scene for the camera.
         *
         * @param scene     the scene to trace
         * @param rayTracer the ray tracer type to use
         * @return this builder instance
         */
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
         * Sets the resolution of the view plane.
         *
         * @param nX number of pixels in width
         * @param nY number of pixels in height
         * @return this builder instance
         * @throws IllegalArgumentException if resolution values are not positive
         */
        public Builder setResolution(int nX, int nY) {
            if (nX <= 0 || nY <= 0) {
                throw new IllegalArgumentException("Resolution values must be positive");
            }
            camera.nX = nX;
            camera.nY = nY;
            camera.resolutionX = camera.vpWidth / nX;
            camera.resolutionY = camera.vpHeight / nY;
            return this;
        }

        /**
         * Builds and returns a fully-initialized {@code Camera} instance.
         *
         * @return the configured camera
         * @throws MissingResourceException if required parameters are missing
         */
        public Camera build() {
            final String MISSING_DATA_MSG = "Missing rendering parameter";

            if (camera.location == null) throw new MissingResourceException(MISSING_DATA_MSG, Camera.class.getName(), "location");
            if (camera.vTo == null) throw new MissingResourceException(MISSING_DATA_MSG, Camera.class.getName(), "vTo");
            if (camera.vUp == null) throw new MissingResourceException(MISSING_DATA_MSG, Camera.class.getName(), "vUp");
            if (camera.vpHeight <= 0) throw new MissingResourceException(MISSING_DATA_MSG, Camera.class.getName(), "vpHeight");
            if (camera.vpWidth <= 0) throw new MissingResourceException(MISSING_DATA_MSG, Camera.class.getName(), "vpWidth");
            if (camera.vpDistance <= 0) throw new MissingResourceException(MISSING_DATA_MSG, Camera.class.getName(), "vpDistance");
            if (camera.nX <= 0 || camera.nY <= 0) throw new MissingResourceException(MISSING_DATA_MSG, Camera.class.getName(), "Resolution");
            if (camera.rayTracer == null) {
                camera.rayTracer = new SimpleRayTracer(null);
            }
            camera.imageWriter = new ImageWriter(camera.nX, camera.nY);
            return new Camera(camera);
        }

    }

    /**
     * Deep copy constructor for Camera.
     *
     * @param other the Camera instance to copy from
     */
    public Camera(Camera other) {
        this.location = other.location;
        this.vUp = other.vUp;
        this.vTo = other.vTo;
        this.vRight = other.vRight;

        this.vpHeight = other.vpHeight;
        this.vpWidth = other.vpWidth;
        this.vpDistance = other.vpDistance;

        this.resolutionX = other.resolutionX;
        this.resolutionY = other.resolutionY;
        this.nX = other.nX;
        this.nY = other.nY;

        this.imageWriter = new ImageWriter(nX, nY);
        this.rayTracer = other.rayTracer;
    }

    /**
     * Casts a ray through a specific pixel and writes its color to the image.
     *
     * @param Nx     number of pixels in width
     * @param Ny     number of pixels in height
     * @param column column index of the pixel
     * @param row    row index of the pixel
     */
    private void castRay(int Nx, int Ny, int column, int row) {
        Color color = rayTracer.traceRay(constructRay(Nx, Ny, column, row));
        imageWriter.writePixel(column, row, color);
    }
}
