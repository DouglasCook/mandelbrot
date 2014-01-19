import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Class used to create the frame for and view the set 
 */

public class MandelbrotViewer extends JFrame
{
    static public void setupGUI()
    {
        JFrame frame = new JFrame("Mandelbrot!");
        // set window to exit when closed
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        MandelbrotCanvas canvas = new MandelbrotCanvas();
        // add canvas to the frame
        frame.add(canvas);
        // resize frame to fit the canvas
        frame.pack();
        // display the frame
        frame.setVisible(true);
    }

    static public void main(String args[])
    {
        // setting up an anonymouse class to extend runnable so that it can be run?
        javax.swing.SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                setupGUI();
            }
        });
    }
}
