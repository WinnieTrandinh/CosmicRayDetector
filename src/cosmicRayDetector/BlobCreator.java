/**
 * 
 */
package cosmicRayDetector;

import java.util.ArrayList;

/**
 * @author WinnieTrandinh
 *
 */
public class BlobCreator extends Blob{

	//ArrayList<Coordinate> gammaPixels; 
	//ArrayList<Blob> blobs = new ArrayList<Blob>();
	
	/**
	 * 
	 */
	public BlobCreator(Coordinate coord) {
		super(coord);
	}
	
	public static ArrayList<Blob> createBlobs(ArrayList<Coordinate> pixels) {
		ArrayList<Blob> blobs = new ArrayList<Blob>();
		ArrayList<Boolean> blobCheck = new ArrayList<Boolean>();
		
		if (pixels.size() == 0) {
			//no pixels
			return blobs;
		}

		//first pixel
		blobs.add(new Blob(pixels.get(0) ) );
		blobCheck.add(true);
		
		/*
		for (int i = 1; i < pixels.size(); i++) {
			for (Blob blob : blobs) {
				if (blob.isPartOf(pixels.get(i) ) ) {
					blob.addToBlob(pixels.get(i) );
				}
				//not part of any previous blobs
				blobs.add(new Blob(pixels.get(i) ) );
			}
		}*/
		
		if (pixels.size() > 1) {
			addBlob(pixels, blobs, blobCheck, 0, 1);
		}
		
		//System.out.println("number of blobs = " + blobs.size() );
		
		return blobs;
	}
	
	private static void addBlob(ArrayList<Coordinate> pixels, ArrayList<Blob> blobs, 
						 ArrayList<Boolean> blobCheck, int bIndex, int pIndex) {
		boolean added = false;
		
		//System.out.println("pixels = " + pIndex + "/" + pixels.size() );
		//System.out.println("blobs = " + bIndex + "/" + blobs.size() );

		
		if (blobs.get(bIndex).isPartOf(pixels.get(pIndex) ) ) {
			//pixel is a part of the blob
			blobs.get(bIndex).addToBlob(pixels.get(pIndex) );
			blobCheck.set(bIndex, true);
			added = true;
		} else {
			blobCheck.set(bIndex, false);
		}
		
		if (bIndex == blobs.size()-1 && !blobCheck.contains(true) ) {
			blobs.add(new Blob(pixels.get(pIndex) ) );
			blobCheck.add(false);
		}
		
		if (bIndex == blobs.size()-1 && pIndex == pixels.size()-1) {
		    return;
		}
		
		//System.out.println("bIndex = " + bIndex + " blobSize = " + blobs.size() );
		if (bIndex == blobs.size()-1) {
			//next pixel
			addBlob(pixels, blobs, blobCheck, 0, pIndex+1);
		} else {
			if (!added) {
				//next blob
				addBlob(pixels, blobs, blobCheck, bIndex+1, pIndex);
			} else {
				if (pIndex == pixels.size()-1) {
					return;
				}
				
				addBlob(pixels, blobs, blobCheck, 0, pIndex+1);
			}
		}
		
	}

}
