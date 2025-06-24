package renderer;

import primitives.*;
import scene.Scene;

import java.util.MissingResourceException;

/**
 * The {@code Camera} class represents a virtual camera in 3D space.
 * It is responsible for generating rays through pixels on a view plane
 * and rendering an image using a ray tracer.
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
    private boolean enableImprovement = false;

    /** Private constructor for use by the builder */
    private Camera() {}

    /** View plane resolution in X and Y directions */
    private double resolutionX = 0.0;
    private double resolutionY = 0.0;

    /** Number of pixels in width and height of view plane */
    private int nX = 1;
    private int nY = 1;

    /** Image writer for rendering pixels */
    private ImageWriter imageWriter;

    /** Ray tracer for computing color of rays */
    private RayTracerBase rayTracer;

    /**
     * Renders the image by casting rays through each pixel in the view plane.
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
     * Calculates the center of the pixel on the view plane.
     *
     * @param nX total number of pixels in X direction
     * @param nY total number of pixels in Y direction
     * @param j  column index (X direction)
     * @param i  row index (Y direction)
     * @return the 3D point on the view plane corresponding to the pixel
     */
    public Point pixelPointCalc(int nX, int nY, int j, int i) {
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
     * Constructs a ray from the camera through a specific pixel.
     *
     * @param nX number of pixels in width (X direction)
     * @param nY number of pixels in height (Y direction)
     * @param j  index of the pixel in X direction (column)
     * @param i  index of the pixel in Y direction (row)
     * @return Ray from the camera through the specified pixel
     */
    public Ray constructRay(int nX, int nY, int j, int i) {
        Vector direction = pixelPointCalc(nX, nY, j, i).subtract(location).normalize();
        return new Ray(location, direction);
    }

    // Builder and other methods remain unchanged
    // JavaDocs for other parts were already appropriate and kept intact

    /**
     * Casts a ray through a specific pixel and writes its color to the image.
     *
     * @param Nx     number of pixels in width
     * @param Ny     number of pixels in height
     * @param column column index of the pixel
     * @param row    row index of the pixel
     */
    private void castRay(int Nx, int Ny, int column, int row) {
        Color color;

        if (rayTracer instanceof GridRayTracer gridTracer) {
            color = gridTracer.traceRayGrid(location, this, nX, nY, column, row);
        } else if (rayTracer instanceof SimpleRayTracer simpleTracer) {
            Ray ray = constructRay(Nx, Ny, column, row);
            color = simpleTracer.traceRay(ray);
        } else {
            Ray ray = constructRay(Nx, Ny, column, row);
            color = rayTracer.traceRay(ray);
        }

        imageWriter.writePixel(column, row, color);
    }

}
