package cosmicRayDetector;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;



/**
 * This class handles all the processing of the images.
 * <br>
 * @author WinnieTrandinh
 * @see BlobCreator
 * @see BlobProcessor
 */
public class Detector {

	//stores the image
	private BufferedImage image;
	//variables for processing of image
	private int threshold;
	private final int PIXELS_TO_SKIP;
	private final double MIN_DENSITY;
	private final double MIN_AREA;
	//stores the screen
	private Screen screen;
	
	/**
	 * Constructor of class.
	 * @param thresh Colour threshold.
	 * @param skip Pixels to skip when processing.
	 * @param density Minimum density.
	 * @param area Minimum area.
	 * @param _screen Frame of image.
	 */
	public Detector(int thresh, int skip, double density, double area, Screen _screen) {
		threshold = thresh;
		PIXELS_TO_SKIP = skip;
		MIN_DENSITY = density;
		MIN_AREA = area;
		screen = _screen;
	}
	
	/**
	 * Gets the image from the screen and stores it.
	 */
	private void setImage() {
		image = screen.getBufferedImage();		
	}

	/**
	 * This iterates through the pixels in the image and finds possible gamma pixels.
	 * @return ArrayList of possible gamma pixels.
	 */
	private ArrayList<Coordinate> getGammaPixels() {		
		//System.out.println("start of search for pixels");
		
		//creates the arrayList to store the pixels
		ArrayList<Coordinate> gammaPixels = new ArrayList<Coordinate>();
		
		//converts the image into its pixels
		final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		//stores the width of the image
		final int width = image.getWidth();
		//indicates how many indexes of pixels[] are for one pixel
		final int pixelLength = 3;

		//iterates through each pixel
		for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += (pixelLength * PIXELS_TO_SKIP) ) {
			int bgr = 0;
			bgr += -16777216; // 255 alpha
			bgr += ((int) pixels[pixel] & 0xff); // blue
			bgr += (((int) pixels[pixel + 1] & 0xff) << 8); // green
			bgr += (((int) pixels[pixel + 2] & 0xff) << 16); // red
			
			//checks if the pixel is a gamma pixel or not
			if (isGamma(bgr) ) {
				//if it is, add it into the arrayList by creating a new Coordinate
				//at the pixel's location
				//System.out.println("new pixel at (" + col + ", " + row + ")");
				gammaPixels.add(new Coordinate(col, row) );
			}
			
			col += PIXELS_TO_SKIP;
			if (col >= width) {
				col = col%width;
				//row++;
				row += PIXELS_TO_SKIP;
				pixel += (width*pixelLength) * (PIXELS_TO_SKIP - 1);
			}
		}
		//returns the arrayList of possible gamma pixels
		return gammaPixels;
	}
	
	/**
	 * This checks to see if the pixel is a gamma pixel or not.
	 * @param bgr Integer containing data on pixel.
	 * @return Boolean of whether it is gamma or not.
	 */
	private boolean isGamma(int bgr) {
		//finds RGB values of pixel
		int blue = (bgr >> 16) & 0x000000FF;
		int green = (bgr >>8 ) & 0x000000FF;
		int red = (bgr) & 0x000000FF;
		
		//checks to see if average value is above the threshold, indicating white
		if ( (blue+green+red)/3 > threshold) {
			return true;
		}
		return false;
	}
	
	/**
	 * This draws the gamma pixels on a Panel.
	 * @param pixels ArrayList containing all the gamma pixels.
	 * @see Panel
	 */
	private void drawGamma(ArrayList<Coordinate> pixels) {
	    //creates a new Panel with the image as the background
		Panel panel = new Panel(image);
		
		//draws a line of length 1 on each gamma pixel
		Graphics g = image.getGraphics();
	    g.setColor(Color.blue);
	    for (int i = 0; i < pixels.size(); i++) {
            g.drawLine(pixels.get(i).getX(), pixels.get(i).getY(), 
            		pixels.get(i).getX(), pixels.get(i).getY() );
	    }
	    g.dispose();
		
	    //adds this panel to the screen
		screen.addToFrame(panel);
	}
	
	/**
	 * This draws the rectangle around each blob onto a Panel. 
	 * @param blobs ArrayList containing all the blobs.
	 * @see Panel
	 */
	private void drawBlob(ArrayList<Blob> blobs) {
		//creates a new Panel with the image as the background
		Panel panel = new Panel(image);
		
		//iterates through each blob and draws a rectangle around them
		for (Blob blob : blobs) {
			
			Graphics g = image.getGraphics();
		    g.setColor(Color.GREEN);
		    //creates a Coordinate at top left and bottom right corner of blob
		    Coordinate min = blob.getMin();
		    Coordinate max = blob.getMax();
		    //creates a rect using the two points above
		    g.drawRect(min.getX(), min.getY(), (int)blob.getWidth(), (int)blob.getHeight() );
		    
		    //draws a dot at top left and bottom right corner for debug purposes
		    g.setColor(Color.RED);
		    g.drawLine(min.getX(), min.getY(), min.getX(), min.getY() );
		    g.drawLine(max.getX(), max.getY(), max.getX(), max.getY() );
		    
		    g.dispose();
		}
		//adds the panel to the screen
		screen.addToFrame(panel);
	}
	
	/**
	 * This is the main method that is called.
	 * <br>
	 * It is responsible for finding and creating all the blobs.
	 * @return String containing the data for the blob.
	 * @see BlobCreator
	 * @see BlobProcessor
	 */
	public String findPixels() {
		//sets the image
		setImage();
		//finds and draws all the gamma pixels
		ArrayList<Coordinate> gammaPixels = getGammaPixels();
		drawGamma(gammaPixels);

		//creates the blobs using the gamma pixels
		BlobCreator creator = new BlobCreator(new Coordinate(-1, -1) );
		ArrayList<Blob> blobs = creator.createBlobs(gammaPixels);
		
		if (blobs == null) {
			//stack overflow error occurred
			return "error";
		}
		
		/*
		System.out.println("* number of blobs before procesing = " + 
							blobs.size() );
		
		for (Blob blob : blobs) {
			System.out.println("area = " + blob.getArea() + ", density = " + blob.getDensity() );
		}*/
		
		//drawBlob(blobs);
		
		//processes the blobs to get rid of false blobs
		BlobProcessor processor = new BlobProcessor(new Coordinate(-1, -1) );
		blobs = processor.calcBlobs(blobs, MIN_DENSITY, MIN_AREA);
		
		/*
		System.out.println("* number of blobs after procesing = " + blobs.size() );
		
		for (Blob blob : blobs) {
			System.out.println("area = " + blob.getArea() + ", density = " + blob.getDensity() );
		}*/
		
		//draws the rectangle around the blob
		drawBlob(blobs);
		
		//returns the data from getInfo
		return (getInfo(blobs) );

	}
	
	/**
	 * This provides information about the blob, which is the cosmic ray.
	 * @param blobs ArrayList of all the blobs.
	 * @return String containing the direction and the area of the ray.
	 */
	private String getInfo(ArrayList<Blob> blobs) {
		String [] directions = new String [] {"Top Left", "Top Center", "Top Right",
				"Middle Left", "Center", "Middle Right",
				"Bottom Left", "Bottom Center", "Bottom Right", 
				"Too small, undetermined"};
		String info = "";
		for (Blob blob : blobs) {
			BlobProcessor processor = new BlobProcessor(new Coordinate(-1, -1) );
			int direction = processor.getDirection(blob);
			info += "Direction = " + directions[direction] + ", Area = " + 
					blob.getArea() + '\n';
		}
		return info;
	}
	
	

}
