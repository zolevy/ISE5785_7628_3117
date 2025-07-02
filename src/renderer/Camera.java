package renderer;

import geometries.*;
import geometries.Intersectable;
import primitives.*;
import scene.Scene;

import java.util.LinkedList;
import java.util.MissingResourceException;
import java.util.stream.IntStream;

/**
 * The {@code Camera} class represents a virtual camera in 3D space.
 * It is responsible for generating rays through pixels on a view plane,
 * rendering images, and optionally applying ray tracing strategies like
 * simple or grid-based sampling.
 * <p>
 * The camera has a position and orientation defined by three orthogonal vectors:
 * {@code vTo} (forward), {@code vUp} (upward), and {@code vRight} (right).
 * It supports adjustable resolution, view plane size and distance,
 * and optional rendering improvements.
 */
public class Camera implements Cloneable {
    /**
     * Location of the camera in 3D space
     */
    private Point location = null;

    /**
     * Up, to, and right direction vectors of the camera
     */
    private Vector vUp = null, vTo = null, vRight = null;

    /**
     * View plane height, width and distance from the camera
     */
    private double vpHeight = 0.0;
    private double vpWidth = 0.0;
    private double vpDistance = 0.0;
    private boolean enableImprovement = false;

    /**
     * Private constructor for use by the builder
     */
    private Camera() {
    }

    /**
     * View plane resolution in X and Y directions
     */
    private double resolutionX = 0.0;
    private double resolutionY = 0.0;

    /**
     * Number of pixels in width and height of view plane
     */
    private int nX = 1;
    private int nY = 1;

    /**
     * Image writer for rendering pixels
     */
    private ImageWriter imageWriter;

    /**
     * Ray tracer for computing color of rays
     */
    private RayTracerBase rayTracer;

    private int threadsCount = 0; // -2 auto, -1 range/stream, 0 no threads, 1+ number of threadsprivate
    static final int SPARE_THREADS = 2; // Spare threads if trying to use all the coresprivate
    double printInterval = 0; // printing progress percentage interval (0 – no printing)
    private PixelManager pixelManager; // pixel manager object

    /**
     * Renders the image by casting rays through each pixel.
     *
     * @return this camera instance
     */
    public Camera renderImage() {
        pixelManager = new PixelManager(nY, nX, printInterval);
        return switch (threadsCount) {
            case 0 -> renderImageNoThreads();
            case -1 -> renderImageStream();
            default -> renderImageRawThreads();
        };
    }

    /**
     * Renders the image without using any additional threads.
     *
     * @return this camera instance
     */
    public Camera renderImageNoThreads()
    {
        for (int i = 0; i < nY; ++i) {
            for (int j = 0; j < nX; ++j) {
                castRay(nX, nY, j, i);
            }
        }
        return this;
    }

    /**
     * Render image using multi-threading by creating and running raw threads.
     *
     * @return the camera object itself
     */
    private Camera renderImageRawThreads() {
        var threads = new LinkedList<Thread>();
        while (threadsCount-- > 0)
            threads.add(new Thread(() -> {
                PixelManager.Pixel pixel;
                while ((pixel = pixelManager.nextPixel()) != null)
                    castRay(imageWriter.nX(), imageWriter.nY(), pixel.col(), pixel.row());
            }));
        for (var thread : threads) thread.start();
        try {
            for (var thread : threads) thread.join();
        } catch (InterruptedException ignore) {}
        return this;
    }
    /**
     * Render image using Java streams with parallelization.
     *
     * @return the camera object itself
     */
    public Camera renderImageStream() {
        IntStream.range(0, nY).parallel() //
                .forEach(i -> IntStream.range(0, nX).parallel() //
                        .forEach(j -> castRay(imageWriter.nX(), imageWriter.nY(), j, i)));
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
     * Calculates the 3D point on the view plane corresponding to the center of the given pixel (i, j).
     *
     * @param nX number of pixels along the view plane width
     * @param nY number of pixels along the view plane height
     * @param j  pixel index along the width (X-axis)
     * @param i  pixel index along the height (Y-axis)
     * @return the 3D point corresponding to the pixel center
     * @throws IllegalArgumentException if the computed pixel point equals the camera location
     */
    public Point pixelPointCalc(int nX, int nY, int j, int i)
    {
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
        return pixelPoint;
    }

    /**
     * Constructs a ray from the camera through a specified pixel.
     *
     * @param nX number of pixels along the view plane width
     * @param nY number of pixels along the view plane height
     * @param j  pixel index along the width (X-axis)
     * @param i  pixel index along the height (Y-axis)
     * @return the ray passing through the pixel (i, j)
     */
    public Ray constructRay(int nX, int nY, int j, int i) {
        Vector direction = pixelPointCalc(nX, nY, j, i).subtract(location).normalize();
        return new Ray(location, direction);
    }

    /**
     * Builder class for constructing {@code Camera} instances with flexible configuration.
     */
    public static class Builder {
        final private Camera camera = new Camera();

        /**
         * Sets the number of threads used for rendering.
         *
         * @param threads number of threads (-2 for auto, -1 for stream, 0 for no threads, positive for thread count)
         * @return this builder instance
         * @throws IllegalArgumentException if threads < -2
         */
        public Builder setMultithreading(int threads) {
            if (threads < -2) throw new IllegalArgumentException("Multithreading must be -2 or higher");
            if (threads >= -1) camera.threadsCount = threads;
            else { // == -2
                int cores = Runtime.getRuntime().availableProcessors() - SPARE_THREADS;
                camera.threadsCount = cores <= 2 ? 1 : cores;
            }
            return this;
        }

        /**
         * Enables CBR optimization.
         *
         * @return this builder instance
         */
        public Builder enableCBR() {
            Intersectable.enableCBR = true;
            Geometries.setBoundingVolumeBuilder(new CBRBoundingBoxBuilder());
            return this;
        }

        /**
         * Enables BVH optimization.
         *
         * @return this builder instance
         */
        public Builder enableBVH() {
            Intersectable.enableBVH = true;
            Geometries.setBoundingVolumeBuilder(new BVHBoundingBoxBuilder());
            return this;
        }

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
         * Ray tracer setter.
         *
         * @param tracer to use
         * @return builder itself - for chaining
         */
        public Builder setRayTracer(RayTracerBase tracer) {
            camera.rayTracer = tracer;
            return this;
        }

        /**
         * Enables or disables rendering improvements.
         *
         * @param enableImprovement whether to enable rendering improvements
         * @return this builder instance
         */
        public Builder setEnableImprovement(boolean enableImprovement) {
            camera.enableImprovement = enableImprovement;
            return this;
        }

        /**
         * Selects the ray tracer strategy based on the provided type and initializes it using the given scene.
         *
         * @param scene scene data container
         * @param type  ray tracer type to use
         * @return builder itself - for chaining
         * @throws IllegalArgumentException if the ray tracer type is not recognized
         */
        public Builder setRayTracer(Scene scene, RayTracerType type) {
            if (scene == null) scene = new Scene("fake");
            camera.rayTracer = switch (type) {
                case SIMPLE -> new SimpleRayTracer(scene);
                case GRID -> new GridRayTracer(scene, camera.vpHeight / camera.nX, camera.vRight, camera.vUp);
                default -> throw new IllegalArgumentException("Unexpected tracer type: " + type);
            };
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
            Vector direction;
            try {
                direction = target.subtract(camera.location).normalize();
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("camera location can't be at target");
            }
            camera.vRight = direction.crossProduct(tempVup).normalize();
            camera.vTo = direction;
            camera.vUp = camera.vRight.crossProduct(direction);
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
            ImageWriter imageWriter = new ImageWriter(nX,nY);
            setImageWriter(imageWriter);
            return this;
        }

        /**
         * Builds and returns a fully-initialized {@code Camera} instance.
         *
         * @return the configured {@code Camera}
         * @throws MissingResourceException if any required rendering parameter is missing
         */
        public Camera build() {
            final String MISSING_DATA_MSG = "Missing rendering parameter";

            if (camera.location == null) throw new MissingResourceException(MISSING_DATA_MSG, Camera.class.getName(), "location");
            if (camera.vTo == null) throw new MissingResourceException(MISSING_DATA_MSG, Camera.class.getName(), "vTo");
            if (camera.vUp == null) throw new MissingResourceException(MISSING_DATA_MSG, Camera.class.getName(), "vUp");
            if (camera.vpHeight <= 0) throw new MissingResourceException(MISSING_DATA_MSG, Camera.class.getName(), "vpHeight");
            if (camera.vpWidth <= 0) throw new MissingResourceException(MISSING_DATA_MSG, Camera.class.getName(), "vpWidth");
            if (camera.vpDistance <= 0) throw new MissingResourceException(MISSING_DATA_MSG, Camera.class.getName(), "vpDistance");
            if (camera.nX <= 0 || camera.nY <= 0) throw new MissingResourceException(MISSING_DATA_MSG, Camera.class.getName(), "resolution");
            if (camera.rayTracer == null) {
                throw new MissingResourceException(MISSING_DATA_MSG, Camera.class.getName(), "rayTracer"); }
            if (camera.imageWriter == null) throw new MissingResourceException(MISSING_DATA_MSG, Camera.class.getName(), "imageWriter");
            try {
                return (Camera) camera.clone();
            } catch (CloneNotSupportedException e) {
                return camera;
            }
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
     * Casts a ray through the specified pixel and computes its color using the configured ray tracer.
     *
     * @param Nx     number of pixels in the X (width) direction
     * @param Ny     number of pixels in the Y (height) direction
     * @param column the X-index of the pixel
     * @param row    the Y-index of the pixel
     */
    private void castRay(int Nx, int Ny, int column, int row) {
        Color color;

        if (rayTracer instanceof GridRayTracer gridTracer) {
            color = gridTracer.traceRayGrid(location, this, nX, nY, column, row);
        }
        else if (rayTracer instanceof SimpleRayTracer simpleTracer) {
            Ray ray = constructRay(Nx, Ny, column, row);
            color = simpleTracer.traceRay(ray);
        }
        else {
            Ray ray = constructRay(Nx, Ny, column, row);
            color = rayTracer.traceRay(ray);
        }

        imageWriter.writePixel(column, row, color);
        pixelManager.pixelDone();
    }

}
