import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.BorderLayout;
import java.awt.Component;

/**
 * Program entry point and class used to create the frame for the set  
 */

public class MandelbrotViewer extends JFrame
{
    static protected MandelbrotCanvas canvas;

    static public void setupGUI()
    {
        JFrame frame = new JFrame("Mandelbrot!");
        // set window to exit when closed
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        // panel for slider, border layout so the slider fills panel
        JPanel sliderPanel = new JPanel(new BorderLayout());
        frame.add(sliderPanel, BorderLayout.SOUTH);

        // can't get label on there for some reason???
        /*
        JLabel sliderLabel = new JLabel("Secondary colour", JLabel.CENTER);
        sliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(sliderLabel);
        */

        // slider to adjust secondary colour
        JSlider colourSlider =
            new JSlider(JSlider.HORIZONTAL, 0, 16777215, 4000000);

        // add the listener
        ChangeListener listen = new ChangeListener(){
            public void stateChanged(ChangeEvent e)
            {
                JSlider source = (JSlider)e.getSource();

                // if the slider has stopped call colour update function in canvas
                if (!source.getValueIsAdjusting()){
                    canvas.setSecondaryColour((int)source.getValue());
                }
            }
        };
        colourSlider.addChangeListener(listen);
        // setup scale for slider
        colourSlider.setMinorTickSpacing(1000000);
        colourSlider.setPaintTicks(true);
        colourSlider.setPaintLabels(true);

        canvas = new MandelbrotCanvas();

        frame.add(canvas);
        sliderPanel.add(colourSlider);

        // resize frame to fit elements and set it visible
        frame.pack();
        frame.setVisible(true);
    }

    static public void main(String args[])
    {
        // setting up an anonymous class to extend runnable so that it can be run?
        javax.swing.SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                setupGUI();
            }
        });
    }
}
