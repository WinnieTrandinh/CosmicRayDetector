
package cosmicRayDetector;

import java.util.ArrayList;

/**
 * This class handles the processing of the blobs to get rid of false blobs.
 * @author Winnie Trandinh
 * @see Blob
 */
public class BlobProcessor extends Blob {

	//stores all the blobs
	private ArrayList<Blob> blobs;
	//constant to store max distance that blobs can be from each other and 
	//still be considered one blob
	private final float MAX_DISTANCE = Main.pixelsToSkip*3;
	
	
	/**
	 * Constructor for the class.
	 * @param coord
	 */
	public BlobProcessor(Coordinate coord) {
		super(coord);
	}
	
	/**
	 * This is the main method that is called.
	 * <br>
	 * It handles the start and end of the processing.
	 * @param _blobs ArrayList of blobs.
	 * @param MIN_DENSITY Minimum density for a blob.
	 * @param MIN_AREA Minimum area for a blob.
	 * @return New and processed ArrayList of blobs.
	 */
	public ArrayList<Blob> calcBlobs(ArrayList<Blob> _blobs, 
									double MIN_DENSITY, double MIN_AREA) {
		blobs = _blobs;
		process(MIN_DENSITY, MIN_AREA);
		return blobs;
	}
	
	/**
	 * This is responsible for the processing of the blobs.
	 * @param MIN_DENSITY Minimum density for a blob.
	 * @param MIN_AREA Minimum area for a blob.
	 */
	private void process(double MIN_DENSITY, double MIN_AREA) {
		//check density to get rid of alpha particles before merging
		checkDensity(MIN_DENSITY);
		//merge any close blobs together
		mergeAll();
		//deletes any false/merged blobs
		//this delete prevents combined blobs from being checked
		deleteAll();
		//check for area
		checkArea(MIN_AREA);
		//delete any false blobs
		deleteAll();
	}
	
	/**
	 * This checks the blob to see if it exceeds the minimum density.
	 * @param density Minimum density of a blob.
	 */
	private void checkDensity(double density) {
		//goes through each blob and deletes them if they are too low
		for (Blob blob : blobs) {
			if (blob.getDensity() < density) {
				//System.out.println("density too low");
				//System.out.println("density = " + blob.getDensity() + " < " + density);
				blob.setDeleted();
			}
		}
	}
	
	/**
	 * This checks the blob to see if it exceeds the minimum area.
	 * @param area Minimum area for a blob.
	 */
	private void checkArea(double area) {
		//goes through each blob and deletes them if they are too low
		for (Blob blob : blobs) {
			if (blob.getArea() < area) {
				//System.out.println("area too low");
				//System.out.println("area = " + blob.getArea() + " < " + area);
				blob.setDeleted();
			}
		}
	}
	
	/**
	 * Starts the merging of the blobs. Done recursively.
	 */
	private void mergeAll() {
		if (blobs.size() > 1) {
			merge(blobs.size()-2, blobs.size()-1);
		}
	}
	
	/**
	 * Deletes any blobs that are set to be deleted.
	 */
	private void deleteAll() {
		for (int i = blobs.size()-1; i >= 0; i--) {
			if (blobs.get(i).getDeleted() ) {
				//System.out.println("blob removed");
				blobs.remove(i);
			}
		}
	}
	
	/**
	 * This merges the two blobs together.
	 * @param a The first blob.
	 * @param b The second blob.
	 */
	private void combine(Blob a, Blob b) {
		//makes blob b bigger than blob a
		Blob temp;
		if (a.getCoordinates().size() > b.getCoordinates().size() ) {
			//switch blobs
			temp = a;
			a = b;
			b = temp;
		}
		
		//goes through each pixel in blob a and adds them to blob b
		for (Coordinate coord : a.getCoordinates() ) {
			if (!b.contains(coord) ) {
				b.addToBlob(coord);
			}
		}
		//sets blob a to be deleted
		a.setDeleted();
	}
	
	/**
	 * Responsible for the merging of the blobs.
	 * @param a The first blob.
	 * @param b The second blob.
	 */
	private void merge(int a, int b) {
		Blob blobA = blobs.get(a);
		Blob blobB = blobs.get(b);
		
		//if neither blobs are set to be deleted
		if ( (!blobA.getDeleted() ) && (!blobB.getDeleted() ) ) {
			//if the blobs are within range to be considered one blob
			if (overlapping(blobA, blobB) ) {
				//combine the two blobs
				combine(blobA, blobB);
			}
		}
		//no more blobs left, break out of recursive loop
		if (b <= 1 && a <= 0) {
			return;
		}
		//checks with more blobs
		if (a == 0) {
			merge(b-2, b-1);
		} else {
			merge(a-1, b);
		}
	}
	
	/**
	 * This checks to see if the blobs are within range to be considered one blob.
	 * @param a The first blob.
	 * @param b The second blob.
	 * @return Boolean whether they are one blob or not.
	 */
	private boolean overlapping(Blob a, Blob b) {
		Coordinate minA = a.getMin();
		Coordinate minB = b.getMin();
		Coordinate maxA = a.getMax();
		Coordinate maxB = b.getMax();

		return ((minA.getX()-MAX_DISTANCE <= minB.getX() && minB.getX() <= maxA.getX()+MAX_DISTANCE ||
			      minA.getX()-MAX_DISTANCE <= maxB.getX() && maxB.getX() <= maxA.getX()+MAX_DISTANCE) &&
			      (minA.getY()-MAX_DISTANCE <= minB.getY() && minB.getY() <= maxA.getY()+MAX_DISTANCE ||
			      minA.getY()-MAX_DISTANCE <= maxB.getY() && maxB.getY() <= maxA.getY()+MAX_DISTANCE));
	}
	
	/**
	 * This determines the direction of the ray.
	 * <br>
	 * It breaks the blob up into nine sections and looks for the section with
	 * the highest density.
	 * @param blob Blob being processed.
	 * @return Integer representing which direction it is.
	 */
	public int getDirection(Blob blob) {
		//ignore PIXELS_TO_SKIP value since it is relative
		//dimensions of smaller sections
		double width = blob.getWidth()/3;
		double height = blob.getHeight()/3;
		double area = width * height;
		
		if (width < 1 || height < 1) {
			//blob is too small
			return 9;
		}
		
		//stores all the pixels in the blob
		ArrayList<Coordinate> pixels = blob.getCoordinates();
		Coordinate minPixel = blob.getMin();
		//each index represents the number of pixels in each section
		int [] numPixels = new int [9];
		double highestDensity = -1;
		int highestSection = 4;
		
		//goes through each pixel and places them into the respective sections
		for (Coordinate pixel : pixels) {
			int x = pixel.getX() - minPixel.getX();
			int y = pixel.getY() - minPixel.getY();

			double col = Math.floor(x / width);
			double row = Math.floor(y / height);
			numPixels[(int) (col + (row*3) )]++;
		}
		
		//finds the section with the highest density
		for (int i = 0; i < 9; i++) {
			double density = numPixels[i]/area;
			//System.out.println("density of " + i + " = " + density);
			if (density >= highestDensity && i != 4) {
				//ignore sector 4 since that is the middle
				highestDensity = density;
				highestSection = i;
			}
		}
		
		
		//returns direction with highest density
		// 0 1 2
		// 3 4 5
		// 6 7 8
		return highestSection;
	}
	
	
}

