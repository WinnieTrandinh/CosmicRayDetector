package cosmicRayDetector;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;


/**
 * This class allows for the drawing on the frame.
 * <br> 
 * Drawings are made on this Panel, and the Panel is displayed onto the frame.
 * @author Winnie Trandinh
 * @see JPanel
 */
public class Panel extends JPanel {

	private static final long serialVersionUID = 1L;
	//image to be set as the background
	private BufferedImage image;
	
	/**
	 * Constructor for the class.
	 * @param _image Image to be used.
	 */
	public Panel(BufferedImage _image) {
		image = _image;
	}

	/**
	 * Overrides method to draw custom images.
	 */
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //paints the image
        g.drawImage(image, 0, 0, this);
    }

}
