
package cosmicRayDetector;

import java.util.ArrayList;

/**
 * This class is responsible for the creation of blobs from the pixels.
 * @author Winnie Trandinh
 * @see Blob
 */
public class BlobCreator extends Blob{

	//boolean representing whether the creation was successful or not
	//unsuccessful indicates a stack overflow error
	private boolean successful;
	
	/**
	 * Constructor for the class. Assumes it processes successfully by default.
	 */
	public BlobCreator(Coordinate coord) {
		super(coord);
		successful = true;
	}
	
	/**
	 * This is responsible for the start of the creation.
	 * @param pixels ArrayList containing all the gamma pixels.
	 * @return Blobs formed from pixels.
	 */
	public ArrayList<Blob> createBlobs(ArrayList<Coordinate> pixels) {
		ArrayList<Blob> blobs = new ArrayList<Blob>();
		
		if (pixels.size() == 0) {
			//no pixels
			return blobs;
		}

		//first pixel is always a new blob
		blobs.add(new Blob(pixels.get(0) ) );
		
		//if there is more pixels, start the recursive call
		if (pixels.size() > 1) {
			addBlob(pixels, blobs, 0, 1);
		}
		
		//System.out.println("number of blobs = " + blobs.size() );
		
		//if the processing was not successful, return null
		if (!successful) {
			//System.out.println("not successful");
			return null;
		}
		//otherwise, return the arrayList of blobs
		return blobs;
	}
	
	/**
	 * This is responsible for the actual creation of the blobs. 
	 * Achieved recursively.
	 * @param pixels ArrayList of all the pixels.
	 * @param blobs ArrayList of all the existing blobs.
	 * @param bIndex Index of the current blob.
	 * @param pIndex Index of the current pixel.
	 */
	private void addBlob(ArrayList<Coordinate> pixels, ArrayList<Blob> blobs, 
								int bIndex, int pIndex) {
		boolean added = false;
		
		//System.out.println("pixels = " + pIndex + "/" + (pixels.size()-1) );
		//System.out.println("blobs = " + bIndex + "/" + (blobs.size()-1) );

		//checks if the pixel is a part of the blob being checked
		if (blobs.get(bIndex).isPartOf(pixels.get(pIndex) ) ) {
			//pixel is a part of the blob
			blobs.get(bIndex).addToBlob(pixels.get(pIndex) );
			added = true;
			//System.out.println("pixel added into blob");
		}
		
		//if at last blob in ArrayList and the pixel has still not been added,
		//create a new blob with that pixel
		if (bIndex == blobs.size()-1 && !added) {
			blobs.add(new Blob(pixels.get(pIndex) ) );
			//System.out.println("**** new blob ****");
			added = true;
		}
		
		if (added && pIndex == pixels.size()-1) {
			//last pixel used
		    return;
		}
	
		
		if (!added) {
			//pixel has not been used, proceed to next blob
			try {
				addBlob(pixels, blobs, bIndex+1, pIndex);
			} catch (StackOverflowError e) {
				successful = false;
				return;
			}
		} else {
			//pixel has been used, proceed to next pixel
			try {
				addBlob(pixels, blobs, 0, pIndex+1);
			} catch (StackOverflowError e) {
				successful = false;
				return;
			}
		}
					
	}

}
