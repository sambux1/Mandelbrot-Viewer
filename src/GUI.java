/*
The GUI class for the mandelbrot viewing program
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class GUI extends JFrame {

    JPanel optionsPanel;
    JPanel optionsSubPanel;     // only necessary for formatting

    // values will be needed outside createAndShowGui()
    JTextField exponent;
    JTextField numIterations;

    // displays the graph and checks for clicks
    JLabel imageLabel;

    JPanel graphPanel;

    // domain and range of the graph
    double leftBound;
    double rightBound;
    double lowerBound;
    double upperBound;

    // determines how much to zoom in or out on left or right click
    static double scalingFactor = 1.5;

    public static void main(String[] args) {
        GUI gui = new GUI();
        gui.createAndShowGui();
    }

    public GUI() {
        // set initial viewing window
        leftBound = -2.0;
        rightBound = 1.0;
        lowerBound = -1.0;
        upperBound = 1.0;
    }

    public void createAndShowGui() {
        /*
        setup the window
         */

        // the main window that panels are added to
        Container pane = this.getContentPane();
        // closes the window when the X button is clicked
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // set the layout of the window to a border layout
        this.setLayout(new BorderLayout());
        // set window size
        int width = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()) / 2;
        int height = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()) / 2;
        this.setSize(width, height);

        /*
        the options panel on the left side of the window
         */

        optionsPanel = new JPanel();

        // set the layout of the options panel
        optionsPanel.setLayout(new BorderLayout());

        // create a label and text field for updating the number of iterations
        JLabel iterationsLabel = new JLabel("Iterations:");
        numIterations = new JTextField("1000", 6);

        // create a label and text field for updating the exponent used in the iteration equation
        JLabel exponentLabel = new JLabel("<html>Exponent (z<sup>n</sup> + c):</html>");
        exponent = new JTextField("2", 3);

        // create a button to update the image with the new iterations exponent
        JButton updateButton = new JButton("Update");
        // handle button click
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                drawGraph();
                pane.validate();
            }
        });

        // create sub-panel to get the desired formatting
        optionsSubPanel = new JPanel();
        optionsSubPanel.setLayout(new GridLayout(3, 2));

        // add all components to the options sub-panel
        optionsSubPanel.add(iterationsLabel);
        optionsSubPanel.add(numIterations);
        optionsSubPanel.add(exponentLabel);
        optionsSubPanel.add(exponent);
        optionsSubPanel.add(updateButton);
        // add the sub-panel to the main options panel
        optionsPanel.add(optionsSubPanel, BorderLayout.NORTH);

        /*
        panel to draw the graph image to the screen
         */
        graphPanel = new JPanel();

        imageLabel = new JLabel();
        // listen for mouse clicks and zoom in or out
        imageLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                // find the ranges along each axis
                double xRange = rightBound - leftBound;
                double yRange = upperBound - lowerBound;

                // find the coordinates of the clicked position in the graph's coordinate system
                double xClick = (double) e.getX();
                double yClick = (double) (imageLabel.getHeight() - e.getY()); // vertical coordinates are inverted
                double xRatio = xClick / imageLabel.getWidth();
                double yRatio = yClick / imageLabel.getHeight();
                double xCoordinate = (xRatio * xRange) + leftBound;
                double yCoordinate = (yRatio * yRange) + lowerBound;


                // find the new axis ranges depending on left or right click and scaling factor
                if (SwingUtilities.isLeftMouseButton(e)) {
                    // if left click, zoom in
                    xRange /= scalingFactor;
                    yRange /= scalingFactor;
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    // if right click, zoom out
                    xRange *= scalingFactor;
                    yRange *= scalingFactor;
                }

                // update the graph's ranges
                leftBound = xCoordinate - (xRange / 2);
                rightBound = xCoordinate + (xRange / 2);
                lowerBound = yCoordinate - (yRange / 2);
                upperBound = yCoordinate + (yRange / 2);
                
                // update the graph
                drawGraph();
                pane.validate();

            }
        }) ;

        drawGraph();

        // add the components to the window
        pane.add(optionsPanel, BorderLayout.WEST);
        pane.add(graphPanel, BorderLayout.CENTER);

        // display the window to the screen
        this.setVisible(true);

    }

    public void drawGraph() {
        // get the number of iterations and the exponent from the text fields
        int iters = Integer.parseInt(numIterations.getText());
        int exp = Integer.parseInt(exponent.getText());

        // create the graph
        Mandelbrot m = new Mandelbrot(leftBound, rightBound, lowerBound, upperBound, iters, exp);
        m.generateGraph();

        // add the graph to a label
        BufferedImage image = m.graph;
        ImageIcon icon = new ImageIcon(image);
        imageLabel.setIcon(icon);

        // update the window
        graphPanel.removeAll();
        graphPanel.repaint();
        graphPanel.add(imageLabel);
    }

}

