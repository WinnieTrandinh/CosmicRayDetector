package cosmicRayDetector;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;

import javax.swing.JPanel;

/**
 * 
 */

/**
 * @author WinnieTrandinh
 *
 */
public class Detector {

	/**
	 * 
	 */

	private BufferedImage image;
	//private ArrayList<Coordinate> gammaPixels;
	private int threshold;
	private final int PIXELS_TO_SKIP;
	
	
	public Detector(int thresh, int skip) {
		//gammaPixels = new ArrayList<Coordinate>();
		threshold = thresh;
		PIXELS_TO_SKIP = skip;
	}
	
	private void setImage() {
		image = Screen.getBufferedImage();		
	}

	private ArrayList<Coordinate> getGammaPixels() {		
		ArrayList<Coordinate> gammaPixels = new ArrayList<Coordinate>();
		
		final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		final int width = image.getWidth();
		//final int height = image.getHeight();

		//int[][] result = new int[height][width];
		final int pixelLength = 3;
		for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength * PIXELS_TO_SKIP) {
			int bgr = 0;
			bgr += -16777216; // 255 alpha
			bgr += ((int) pixels[pixel] & 0xff); // blue
			bgr += (((int) pixels[pixel + 1] & 0xff) << 8); // green
			bgr += (((int) pixels[pixel + 2] & 0xff) << 16); // red
			//result[row][col] = argb;
			if (isGamma(bgr) ) {
				gammaPixels.add(new Coordinate(col, row) );
			}
			
			col++;
			if (col == width) {
				col = 0;
				row++;
			}
		}
		
		return gammaPixels;
	}
	
	private boolean isGamma(int bgr) {
		int blue = (bgr >> 16) & 0x000000FF;
		int green = (bgr >>8 ) & 0x000000FF;
		int red = (bgr) & 0x000000FF;
		
		if (blue < 50 && green < 50 && red < 50) {
			return true;
		}
		return false;
	}
	
	private void drawGamma(ArrayList<Coordinate> pixels) {
		/*
		JPanel panel = new JPanel() {
	        @Override
	        protected void paintComponent(Graphics g) {
	            super.paintComponent(g);
	            g.drawImage(image, 0, 0, this);
	        }
	    };
		
		Graphics g = image.getGraphics();
	    g.setColor(Color.red);
	    for (int i = 0; i < pixels.size(); i++) {
            g.drawRect(pixels.get(i).getX(), pixels.get(i).getY(), 1, 1);
            System.out.println("pixel at (" + pixels.get(i).getX() + ", " + pixels.get(i).getY() + ")");
	    }
	    g.dispose();
	    panel.repaint();
	    Screen.addToFrame(panel);
	    */
	    
		Panel panel = new Panel(image);
		
		Graphics g = image.getGraphics();
	    g.setColor(Color.red);
	    for (int i = 0; i < pixels.size(); i++) {
            g.drawRect(pixels.get(i).getX(), pixels.get(i).getY(), 1, 1);
            //System.out.println("pixel at (" + pixels.get(i).getX() + ", " + pixels.get(i).getY() + ")");
	    }
	    g.dispose();
		
		Screen.addToFrame(panel);
	}
	
	public void findPixels() {
		setImage();
		ArrayList<Coordinate> gammaPixels = getGammaPixels();
		drawGamma(gammaPixels);
		//System.out.println(gammaPixels.size());
		
		ArrayList<Blob> blobs = BlobCreator.createBlobs(gammaPixels);
	}
	
	

}
