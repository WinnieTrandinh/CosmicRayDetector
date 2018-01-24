package cosmicRayDetector;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;


/**
 * This class is responsible for all the GUI.
 * <br>
 * It creates the frame and draws on it using JPanels.
 * @author Winnie Trandinh
 *
 */
public class Screen {
	
	//dimensions of the frame
	private int width;
	private int height;
	//information regarding the image
	private String imagePath;
	private Image image;
	private int imageNum;
	
	//frame that is created
	private JFrame frame;
	
	/**
	 * Constructor for Screen object.
	 * @param _width Width of frame.
	 * @param _height Height of frame.
	 */
	public Screen(int _width, int _height) {
		width = _width;
		height = _height;
	}
	
	/**
	 * Sets the image according to the path.
	 * @param image Image's file path.
	 * @param num Image number.
	 */
	public void setImage(String image, int num) {
		imagePath = image;
		imageNum = num;
	}
	
	/**
	 * Casts the image into a BufferedImage.
	 * @return BufferedImage of image.
	 */
	public BufferedImage getBufferedImage() {
		BufferedImage bImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_3BYTE_BGR);
		bImage.getGraphics().drawImage(image, 0, 0 , null);
		return bImage;
	}
	
	/**
	 * Draws the image onto the frame.
	 * @param frame Frame that is being drawn on.
	 * @return Updated frame with image.
	 */
	private JFrame drawImage(JFrame frame) {
		//sets the imageIcon to the image
		ImageIcon imageIcon = null;
		try {
			imageIcon = new ImageIcon(getClass().getResource(imagePath) );
		} catch (Exception e) {
			System.err.println("file not found");
		}
		//casts ImageIcon into Image and rescales it into dimensions of frame
		Image imageOld = imageIcon.getImage();
		Image imageNew = imageOld.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		//recasts back into an ImageIcon
		imageIcon = new ImageIcon(imageNew);
		
		//create a label to hold the image
		JLabel label = new JLabel(imageIcon);
		label.setLocation(0, 0);
		
		//adds label into the frame
		frame.getContentPane().add(label);
		
		//sets global image variable to the new image
		image = imageNew;
		
		//returns updated frame
		return frame;
	}
	
	/**
	 * Creates the frame.
	 */
	public void update() {
		//creates the frame and initializes it
		frame = new JFrame ("Image " + imageNum);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(0, 0, width, height);
		frame.setVisible(true);
		
		//draw the image onto the frame
		frame = drawImage(frame);
		//displays the new frame
		frame.pack();
	}
	
	/**
	 * This adds the Panel into the frame.
	 * @param _panel Panel that is to be added.
	 */
	public void addToFrame(Panel _panel) {
		//adds panel into the frame
		frame.getContentPane().add(_panel);
		
		//done in here to make sure code runs using AWT
		//necessary due to multiple threads wanting to update GUI
		SwingUtilities.invokeLater(new Runnable() {
	      public void run() {
	    	//draws the updated frame
	        frame.setVisible(true);
	      }
	    });
		
	}

}
