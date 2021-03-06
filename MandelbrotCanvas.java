import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.InputMap;
import javax.swing.ActionMap;
import javax.swing.AbstractAction;

import java.awt.Graphics; 
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;

/**
 * Class used to draw the mandelbrot set 
 */
public class MandelbrotCanvas extends JComponent
{
    private final int MAX_ESCAPE_VAL = 500;
    private final int WIDTH = 768;
    private final int HEIGHT = 576;

    private double centreX, centreY, pxScale;

    int colourA = 1000;
    int colourB = 4000000;

    // colour boundary is used to decide if a point is based on colour A or B
    double colourBoundary = 0.4;

    // constructor to set default values for the canvas and setup interactive aspects
    public MandelbrotCanvas()
    {
        // need to use the input and action maps to map keystrokes
        InputMap imap = MandelbrotCanvas.this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap amap = MandelbrotCanvas.this.getActionMap();

        centreX = -0.75;
        centreY = 0;
        pxScale = 0.005;

        // so it is possible to interact using the mouse 
        addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                // calculate new centre
                centreX = getMinX() + e.getX()*pxScale;
                centreY = getMinY() - e.getY()*pxScale;
                // repaint the image
                MandelbrotCanvas.this.repaint();
            }
        });

        // add key bindings for zooming
        imap.put(KeyStroke.getKeyStroke("typed +"), "zoomIn");
        imap.put(KeyStroke.getKeyStroke("typed -"), "zoomOut");
        imap.put(KeyStroke.getKeyStroke("typed ="), "resetZoom");

        // map them to actions
        amap.put("zoomIn", new AbstractAction(){
            public void actionPerformed(ActionEvent e){
                // just need to change the scale to achieve zooming
                pxScale /= 2;
                MandelbrotCanvas.this.repaint();
            }
        });
        amap.put("zoomOut", new AbstractAction(){
            public void actionPerformed(ActionEvent e){
                pxScale *= 2;
                MandelbrotCanvas.this.repaint();
            }
        });
        amap.put("resetZoom", new AbstractAction(){
            public void actionPerformed(ActionEvent e){
                pxScale = 0.005;
                MandelbrotCanvas.this.repaint();
            }
        });
    }

    // protected because it must match the original paintComponent
    protected void paintComponent(Graphics g)
    {
        final int width = this.getWidth();
        final int height = this.getHeight();

        final BufferedImage buff = 
            new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Thread t1 = new Thread(new Runnable(){
            public void run(){
                double real, imaginary, escapeVal;
                Color colour;
                // loop through all pixels to update their colour 
                for (int y = 0; y < height/2; y++){
                    for (int x = 0; x < width; x++){
                        // calculate complex number represented by pixels coords
                        real = getMinX() + x*pxScale;
                        imaginary = getMinY() - y*pxScale;

                        Complex c = new Complex(real, imaginary);
                        escapeVal = Complex.getMandelbrotEscapeVal(c, MAX_ESCAPE_VAL);

                        // now set the colour based on calculated escape value
                        if (escapeVal == MAX_ESCAPE_VAL){
                            // if the point converges then make it black
                            colour = new Color(Color.BLACK.getRGB());

                        }else if (escapeVal/MAX_ESCAPE_VAL < colourBoundary){
                            // may need to limit this as below if I put in a slider for colourA 
                            //colour = new Color((int)Math.round(colourA + (1000 * escapeVal/MAX_ESCAPE_VAL)));
                            colour = new Color((int)Math.round(colourA * escapeVal/MAX_ESCAPE_VAL));
                        }else {
                            // adding variation to the base colour means that gradient is less extreme -> looks nicer!
                            colour = new Color((int)Math.round(colourB + (1000 * escapeVal/MAX_ESCAPE_VAL)));
                        }

                        buff.setRGB(x, y, colour.getRGB());
                    }
                }

            }
        });
        t1.start();

        Thread t2 = new Thread(new Runnable(){
            public void run(){
                double real, imaginary, escapeVal;
                Color colour;
                // loop through all pixels to update their colour 
                for (int y = height/2; y < height; y++){
                    for (int x = 0; x < width; x++){
                        // calculate complex number represented by pixels coords
                        real = getMinX() + x*pxScale;
                        imaginary = getMinY() - y*pxScale;

                        Complex c = new Complex(real, imaginary);
                        escapeVal = Complex.getMandelbrotEscapeVal(c, MAX_ESCAPE_VAL);

                        // now set the colour based on calculated escape value
                        if (escapeVal == MAX_ESCAPE_VAL){
                            // if the point converges then make it black
                            colour = new Color(Color.BLACK.getRGB());

                        }else if (escapeVal/MAX_ESCAPE_VAL < colourBoundary){
                            // may need to limit this as below if I put in a slider for colourA 
                            //colour = new Color((int)Math.round(colourA + (1000 * escapeVal/MAX_ESCAPE_VAL)));
                            colour = new Color((int)Math.round(colourA * escapeVal/MAX_ESCAPE_VAL));
                        }else {
                            // adding variation to the base colour means that gradient is less extreme -> looks nicer!
                            colour = new Color((int)Math.round(colourB + (1000 * escapeVal/MAX_ESCAPE_VAL)));
                        }

                        buff.setRGB(x, y, colour.getRGB());
                    }
                }

            }
        });
        t2.start();

        // now need to wait for the threads to finish before drawing image
        try {
            t1.join();
            t2.join();
        }
        catch (Exception e) {
            // need to have try catch block so it compiles!
            System.err.println("Thread exception raised");
        }

        // finally draw the buffered image
        g.drawImage(buff, 0, 0, null);
    }

    // accessor to change secondary colour
    public void setSecondaryColour(int RGBvalue)
    {
        colourB = RGBvalue;
        MandelbrotCanvas.this.repaint();
    }

    // overridden version of the Jcomponent method
    public Dimension getPreferredSize()
    {
        Dimension d = new Dimension(WIDTH, HEIGHT);
        return d;
    }

    // return X coordinate that the origin currently corresponds to
    private double getMinX()
    {
        return (centreX - ((1 - getWidth()%2) * pxScale/2 ) - (pxScale * getWidth()/2));
    }

    // return Y coordinate that the origin currently corresponds to
    private double getMinY()
    {
        return (centreY + ((1 - getHeight()%2) * pxScale/2) + (pxScale * getHeight()/2));
    }
}
