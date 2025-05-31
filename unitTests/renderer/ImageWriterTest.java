package renderer;

import org.junit.jupiter.api.Test;
import primitives.Color;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link ImageWriter} class.
 * This test verifies that pixels are correctly written to the image and saved.
 */
class ImageWriterTest {

    /**
     * Test method for {@link ImageWriter#writeToImage(String)}.
     * <p>
     * This test creates an 800x500 image consisting of a grid of 50x50 pixel squares.
     * It colors each pixel purple, except for grid lines every 50 pixels which are colored yellow.
     * The result is written to an image file named "gridFirstImage".
     * </p>
     */
    @Test
    void testWriteToImage() {
        final int nX = 800;
        final int nY = 500;
        ImageWriter image = new ImageWriter(nX, nY);
        final Color purple = new Color(128, 0, 128);
        final Color yellow = new Color(255, 255, 0);
        final int squareSize = 50;
        final int squaresX = nX / squareSize; // 16
        final int squaresY = nY / squareSize; // 10
        boolean grid = true;
        for (int yIndex = 0; yIndex < nY; yIndex++) {
            if (yIndex % 50 == 0) {
                grid = true;
            } else
                grid = false;
            for (int xIndex = 0; xIndex < nX; xIndex++) {
                if (grid == true) {
                    image.writePixel(xIndex, yIndex, yellow);
                } else if (xIndex % 50 == 0) {
                    image.writePixel(xIndex, yIndex, yellow);
                } else
                    image.writePixel(xIndex, yIndex, purple);
            }
        }
        image.writeToImage("gridFirstImage");
    }
}
