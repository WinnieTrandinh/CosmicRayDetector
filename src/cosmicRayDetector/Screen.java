package cosmicRayDetector;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 
 */

/**
 * @author WinnieTrandinh
 *
 */
public class Screen {

	/**
	 * 
	 */
	
	private int width;
	private int height;
	private String imagePath;
	private static Image image;
	
	private static JFrame frame;
	private static Panel panel;
	
	public Screen(int _width, int _height, String image) {
		width = _width;
		height = _height;
		imagePath = image;
	}
	
	public static BufferedImage getBufferedImage() {
		BufferedImage bImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_3BYTE_BGR);
		bImage.getGraphics().drawImage(image, 0, 0 , null);
		return bImage;
		//return (BufferedImage)image;
	}
	
	private JFrame drawImage(JFrame frame) {
		//ImageIcon image = new ImageIcon(loadImage("testImage01.png") );
		ImageIcon imageIcon = null;
		try {
			imageIcon = new ImageIcon(getClass().getResource(imagePath) );
		} catch (Exception e) {
			System.err.println("file not found");
		}
		Image imageOld = imageIcon.getImage();
		Image imageNew = imageOld.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		imageIcon = new ImageIcon(imageNew);
		
		JLabel label = new JLabel(imageIcon);
		label.setLocation(0, 0);
		
		frame.getContentPane().add(label);
		
		image = imageNew;
		
		return frame;
	}
	
	public void update() {
		
		frame = new JFrame ("Cosmic Ray Detector");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(0, 0, width, height);
		frame.setVisible(true);
		
		frame = drawImage(frame);

		frame.pack();
		//frame.repaint();
	}
	
	public static void addToFrame(Panel _panel) {
		frame.getContentPane().add(_panel);
		System.out.println("panel added");
		//frame.repaint();
		/*
		Graphics g = panel.getGraphics();
		if (g == null) {
			System.out.println("g is null");
		}*/
		panel = _panel;
		//_panel.repaint();
		//frame.repaint();
		//this.test();
		//panel = _panel;
		
		//frame.pack();
		frame.setVisible(true);
	}
	
	
	private void test() {
		System.out.println(true);
	}
	
	/*
	public void draw() {
		panel.repaint();
		test();
	}*/
	
	/*
	public void foo() {
		Panel panel = new Panel();
		frame.getContentPane().add(panel);
		frame.pack();
		//frame.setVisible(true);
		//panel.repaint();
		
		
	}*/
	

}
