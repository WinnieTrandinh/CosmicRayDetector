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
	private final double MIN_DENSITY;
	private final double MIN_AREA;
	
	
	public Detector(int thresh, int skip, double density, double area) {
		//gammaPixels = new ArrayList<Coordinate>();
		threshold = thresh;
		PIXELS_TO_SKIP = skip;
		MIN_DENSITY = density;
		MIN_AREA = area;
	}
	
	private void setImage() {
		image = Screen.getBufferedImage();		
	}

	private ArrayList<Coordinate> getGammaPixels() {		
		System.out.println("start of search for pixels");
		
		ArrayList<Coordinate> gammaPixels = new ArrayList<Coordinate>();
		
		final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		final int width = image.getWidth();
		final int height = image.getHeight();
		
		final int pixelLength = 3;

		
		int counter = 0;
		//int[][] result = new int[height][width];
		//final int pixelLength = 3;
		for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += (pixelLength * PIXELS_TO_SKIP) ) {
			int bgr = 0;
			bgr += -16777216; // 255 alpha
			bgr += ((int) pixels[pixel] & 0xff); // blue
			bgr += (((int) pixels[pixel + 1] & 0xff) << 8); // green
			bgr += (((int) pixels[pixel + 2] & 0xff) << 16); // red
			//result[row][col] = argb;
			//System.out.println("col = " + col + " row = " + row);
			if (isGamma(bgr) ) {
				//System.out.println("new pixel at (" + col + ", " + row + ")");
				gammaPixels.add(new Coordinate(col, row) );
			}
			
			col += PIXELS_TO_SKIP;
			//col++;
			if (col >= width) {
				col = col%width;
				//row++;
				row += PIXELS_TO_SKIP;
				pixel += (width*pixelLength) * (PIXELS_TO_SKIP - 1);
			}
			counter++;
		}
		System.out.println("counter = " + counter);
		
		//min = (51, 43)
		//max = (118, 55)
		
		/*
		for (int col = 0, pixel = 0; col < width; col += PIXELS_TO_SKIP) {
			for (int row = 0; row < height; row += PIXELS_TO_SKIP) {
				int bgr = 0;
				bgr += -16777216; // 255 alpha
				bgr += ((int) pixels[pixel] & 0xff); // blue
				bgr += (((int) pixels[pixel + 1] & 0xff) << 8); // green
				bgr += (((int) pixels[pixel + 2] & 0xff) << 16); // red
				//result[row][col] = argb;
				//System.out.println("col = " + col + " row = " + row);
				if (isGamma(bgr) ) {
					//System.out.println("new pixel at (" + col + ", " + row + ")");
					gammaPixels.add(new Coordinate(col, row) );
				}
				pixel += PIXELS_TO_SKIP * pixelLength;
			}
			pixel += (width*(PIXELS_TO_SKIP-1) ) * pixelLength;
		}*/
		
		return gammaPixels;
	}
	
	private boolean isGamma(int bgr) {
		int blue = (bgr >> 16) & 0x000000FF;
		int green = (bgr >>8 ) & 0x000000FF;
		int red = (bgr) & 0x000000FF;
		
		/*
		if (blue < threshold && green < threshold && red < threshold) {
			return true;
		}*/
		
		if ( (blue+green+red)/3 < threshold) {
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
	    g.setColor(Color.blue);
	    for (int i = 0; i < pixels.size(); i++) {
            //g.drawRect(pixels.get(i).getX(), pixels.get(i).getY(), 1, 1);
            g.drawLine(pixels.get(i).getX(), pixels.get(i).getY(), 
            		pixels.get(i).getX(), pixels.get(i).getY() );
            //System.out.println("pixel at (" + pixels.get(i).getX() + ", " + pixels.get(i).getY() + ")");
	    }
	    g.dispose();
		
		Screen.addToFrame(panel);
	}
	
	public void findPixels() {
		setImage();
		ArrayList<Coordinate> gammaPixels = getGammaPixels();
		drawGamma(gammaPixels);
		System.out.println(gammaPixels.size() + " gamma pixels");
		
		ArrayList<Blob> blobs = BlobCreator.createBlobs(gammaPixels);
		System.out.println("********** number of blobs before procesing = " + 
							blobs.size() );
		
		for (Blob blob : blobs) {
			System.out.println("area = " + blob.getArea() + ", density = " + blob.getDensity() );
		}
		
		//process blobs
		blobs = BlobProcessor.calcBlobs(blobs, MIN_DENSITY, MIN_AREA);
		System.out.println("********** number of blobs after procesing = " + blobs.size() );
		for (Blob blob : blobs) {
			System.out.println("area = " + blob.getArea() + ", density = " + blob.getDensity() );
		}
		
		Panel panel = new Panel(image);
		
		for (Blob blob : blobs) {
			
			Graphics g = image.getGraphics();
		    g.setColor(Color.GREEN);
		    Coordinate min = blob.getMin();
		    Coordinate max = blob.getMax();
		    g.drawRect(min.getX(), min.getY(), (int)blob.getWidth(), (int)blob.getHeight() );
		    
		    g.setColor(Color.RED);
		    //g.drawRect(min.getX(), min.getY(), 1, 1);
		    //g.drawRect(blob.getMax().getX(), blob.getMax().getY(), 1, 1);
		    g.drawLine(min.getX(), min.getY(), min.getX(), min.getY() );
		    g.drawLine(max.getX(), max.getY(), max.getX(), max.getY() );
		    
		    g.dispose();
		}
				
		Screen.addToFrame(panel);

	}
	
	

}
