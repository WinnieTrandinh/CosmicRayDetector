package cosmicRayDetector;
import java.util.ArrayList;

/**
 * 
 */

/**
 * @author WinnieTrandinh
 *
 */
public class Blob {

	// stores all the coords that are a part of this blob
	private ArrayList<Coordinate> coords = new ArrayList<Coordinate>();
	// size of the blob
	private int width;
	private int height;
	// the blob's center location
	private Coordinate center;
	// used to delete blobs when merging two blobs
	private boolean deleted = false;
	// coordinates of the top left and bottom right corner
	// used to draw the rectangle
	private Coordinate minBounds;
	private Coordinate maxBounds;
	// coord density of the blob
	private double density;
	// area of the blob
	private double area;
	// the maximum distance of the coord in question to be still considered a part
	// of the blob
	// used to determine if the coord is part of a new blob or this blob
	private final int PIXELS_TO_SKIP = Main.PIXELS_TO_SKIP;
	private final int TOLERANCE = PIXELS_TO_SKIP * 2;

	/**
	 * 
	 */
	public Blob(Coordinate coord) {
		//System.out.println("blob creation start");
		// add given coord into the array of coords in this blob
		coords.add(coord);
		//System.out.println("coordinate added");
		// sets initial values of the blob, given that there is only one coord
		//System.out.println(coords.get(coords.size() / 2).getY() );
		center = new Coordinate(coords.get(coords.size() / 2).getX(), coords.get(coords.size() / 2).getY() );
		//System.out.println("center added");
		
		minBounds = new Coordinate(coord.getX(), coord.getY());
		maxBounds = new Coordinate(coord.getX(), coord.getY());

		width = 1;
		height = 1;
	}

	protected boolean isPartOf(Coordinate p) {

		// creates a larger rectangle around the edges of the blob and sees if the coord
		// is within that rectangle
		return (minBounds.getX() - (TOLERANCE) <= p.getX() && p.getX() <= maxBounds.getX() + (TOLERANCE)
				&& minBounds.getY() - (TOLERANCE) <= p.getY() && p.getY() <= maxBounds.getY() + (TOLERANCE));

	}

	// method to see if the coord in question is already contained within the blob
	// takes in a coord and returns a booelan
	protected boolean contains(Coordinate p) {
		return coords.contains(p);
	}

	// clears all coords in the blob
	protected void clear() {
		coords.clear();
	}

	// updates the size of the blob
	private void updateSize() {
		// calculations for size based on top left and bottom right corner
		// adds one since a pixel is a rectangle, not a point
		// hence one pixel has width and height of one, not zero
		width = maxBounds.getX() - minBounds.getX() + PIXELS_TO_SKIP;
		height = maxBounds.getY() - minBounds.getY() + PIXELS_TO_SKIP;

		area = width * height;
		//System.out.println("Area: " + width * height +", Coordinate Length: "+ coords.size() + 
		//		", Density: " + density);
	}

	// updates the coord density of the blob
	private void updateDensity() {
		// calculations done by dividing the amount of coords in the blob by the area of
		// the blob
		//area = width * height;
		System.out.println("min = (" + minBounds.getX() + ", " + minBounds.getY() +
				") max = (" + maxBounds.getX() + ", " + maxBounds.getY() + 
				") width = " + width + " height = " + height + " area = " + area);

		//System.out.println("coords size = " + coords.size() );
		density = ( (coords.size() * (Math.pow(Main.PIXELS_TO_SKIP, 2) ) ) / (area) );
	}

	// adds the coord to the blob
	// takes in the coord that is to be added
	protected void addToBlob(Coordinate p) {

		// adds into the coord array of the blob
		coords.add(p);
		// updates the center of the blob due to the added coord
		center.setXY(coords.get(coords.size() / 2).getX(), coords.get(coords.size() / 2).getY());

		// updates the min and max bounds by seeing if the new coord is outside any of
		// these bounds
		if (p.getX() < minBounds.getX()) {
			minBounds.setX(p.getX());
		}
		if (p.getY() < minBounds.getY()) {
			minBounds.setY(p.getY());
		}
		if (p.getX() > maxBounds.getX()) {
			maxBounds.setX(p.getX());
		}
		if (p.getY() > maxBounds.getY()) {
			maxBounds.setY(p.getY());
		}
		// calls update function to update other properties of the blob
		update();
	}

	// this method draws the rectange around the blob
	private void show() {
		/*
	    //Coordinate topLeft = coords.get(0);
	    //Coordinate bottomRight = coords.get(coords.size()-1);
	    // fill(255 - (255*density));
	    
	    noFill();
	    stroke(0);
	    if (VisionProcessor.runType == RunType.PC) {
	      //Coordinate topLeft = coords.get(0);
	      //Coordinate bottomRight = coords.get(coords.size()-1);
	      // fill(255 - (255*density));
	      //draws a rectangle on the blank screen and on top of the camara image
	      rect(minBounds.x, minBounds.y, maxBounds.x, maxBounds.y);
	      rect(minBounds.x + width/2, minBounds.y, maxBounds.x + width/2, maxBounds.y);
	    }*/
		
		
		//instead of showing, return min and max and have it draw using detector's method
	}

	// returns the width of the rect
	protected float getWidth() {
		updateSize();
		//return maxBounds.getX() - minBounds.getX();
		return width;
	}

	// returns the height of the rect
	protected float getHeight() {
		updateSize();
		//return maxBounds.getY() - minBounds.getY();
		return height;
	}

	// set the blob to be deleted
	protected void setDeleted() {
		deleted = true;
	}

	// returns whether the blob is set to be deleted
	protected boolean getDeleted() {
		return deleted;
	}

	// updates other properties of the blob
	private void update() {
		updateSize();
		updateDensity();
	}
	
	public Coordinate getMin() {
		return minBounds;
	}
	
	public Coordinate getMax() {
		return maxBounds;
	}
	
	protected double getDensity() {
		update();
		return density;
	}
	
	protected double getArea() {
		updateSize();
		return area;
	}
	
	protected ArrayList<Coordinate> getCoordinates() {
		return coords;
	}

}
