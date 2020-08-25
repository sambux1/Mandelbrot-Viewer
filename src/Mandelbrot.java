/*
The class to calculate and create the image to be shown on the screen
 */

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Mandelbrot {

    // the image to be rendered to the screen
    public BufferedImage graph;

    // the domain and range to be graphed
    double leftBound;
    double rightBound;
    double lowerBound;
    double upperBound;

    // the step sizes between pixels
    double xStep;
    double yStep;

    // the maximum number of iterations
    int maxIterations;

    // the power of z
    int exponent;

    // the dimensions of the image to be created (in pixels)
    static int imageWidth = 600;
    static int imageHeight = 400;

    // the list of colors to be used for setting each pixel
    int red = (255 << 16);
    int orange = (255 << 16) | (127 << 8);
    int yellow = (255 << 16) | (255 << 8);
    int green = (255 << 8);
    int blue = 255;
    int[] colors = {blue, green, yellow, orange, red};
    // the iteration thresholds used to determine thresholds
    int[] colorThresholds = new int[this.colors.length];

    // constructor
    Mandelbrot(double left, double right, double lower, double upper, int iters, int exp) {
        this.leftBound = left;
        this.rightBound = right;
        this.lowerBound = lower;
        this.upperBound = upper;
        this.maxIterations = iters;
        this.exponent = exp;

        // create the BufferedImage
        graph = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);

        // calculate the step sizes to be used
        this.calculateStepSizes();

        // initialize the color thresholds
        this.setColors();
    }

    // calculate the step sizes to be used based on the bounds and dimensions of the graph
    public void calculateStepSizes() {
        this.xStep = (this.rightBound - this.leftBound) / imageWidth;
        this.yStep = (this.upperBound - this.lowerBound) / imageHeight;
    }

    // iterate over each pixel and add the data to the image
    public void generateGraph() {
        // values are calculated from the center of each pixel's area
        double xStart = this.leftBound + (this.xStep / 2);
        double yStart = this.upperBound - (this.yStep / 2);

        // the array to store pixel values
        int[] data = new int[imageWidth * imageHeight];
        int count = 0;

        for (double y = yStart; y > this.lowerBound; y -= yStep) {
            for (double x = xStart; x < this.rightBound; x += xStep) {
                int pixel;
                int iterations = iteratePoint(x, y);
                if (iterations == -1) {
                    // set pixel to black if it did not diverge
                    pixel = 0;
                } else {
                    // get the color for the pixel based on the number of iterations it took to diverge
                    pixel = getColor(iterations);
                }
                data[count] = pixel;
                count++;
            }
        }

        final int[] old = ( (DataBufferInt) graph.getRaster().getDataBuffer() ).getData();

        System.arraycopy(data, 0, old, 0, data.length);
    }

    // performs the calculation for an individual point, returns the number of iterations before it diverges
    // if it does not diverge, returns -1
    // z(i+1) = (z(i) ** 2) + c
    public int iteratePoint(double x, double y) {
        // the number to be squared each iteration, initialized to 0 + 0i
        Complex z = new Complex();
        // the constant to be added after each iteration
        Complex c = new Complex(x, y);
        for (int i = 0; i < this.maxIterations; i++) {
            // square z
            z.power(exponent);
            // add c
            z.add(c);

            // check if magnitude is greater than 2
            if (z.magnitude() >= 2.0) {
                // diverged, return number of iterations taken to reach divergence
                return (i + 1);
            }
        }

        // only reaches here if it did not diverge
        return -1;
    }

    // set the iteration thresholds for the colors and create the color index
    public void setColors() {
        for (int i = 0; i < this.colors.length; i++) {
            this.colorThresholds[i] = (this.maxIterations / this.colors.length) * i;
        }
    }

    // gets the appropriate color given the number of iterations it took to diverge
    public int getColor(int iterations) {
        for (int i = 0; i < this.colorThresholds.length; i++) {
            // if it has gotten to the last value, return the last value to avoid index out of bounds error
            if (i == this.colorThresholds.length - 1) {
                return this.colors[i];
            }
            // check if the correct color has been found
            if ((iterations > this.colorThresholds[i]) && (iterations < this.colorThresholds[i+1])) {
                return this.colors[i];
            }
        }

        // if none of the conditions above apply (error), return white
        return (255 << 16) | (255 << 8) | 255;
    }

}
