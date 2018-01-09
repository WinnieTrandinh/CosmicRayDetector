package cosmicRayDetector;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * 
 */

/**
 * @author WinnieTrandinh
 *
 */
public class Panel extends JPanel {

	private BufferedImage image;
	
	/**
	 * 
	 */
	public Panel(BufferedImage _image) {
		image = _image;
	}

	@Override
    protected void paintComponent(Graphics g) {
		System.out.println("paintComponent called");
		//paints it normally
        super.paintComponent(g);
        //also paints the image in the back
        g.drawImage(image, 0, 0, this);
		//g.setColor(Color.RED);
		//g.fillRect(20, 20, 100, 100);
    }

}
