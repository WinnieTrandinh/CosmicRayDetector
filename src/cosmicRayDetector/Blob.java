package cosmicRayDetector;
import java.util.ArrayList;


/**
 * @author WinnieTrandinh
 *
 */
public class Blob {

	// stores all the coordinates that are a part of this blob
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
	// the maximum distance of the pixels in question to be still considered a part
	// of the blob
	// used to determine if the coord is part of a new blob or this blob
	private final int PIXELS_TO_SKIP = Main.pixelsToSkip;
	private final int TOLERANCE = PIXELS_TO_SKIP * 2;

	/**
	 * Constructor for the class.
	 * @param coord First pixel of the blob.
	 */
	public Blob(Coordinate coord) {
		// add given pixel into the array of Coordinates in this blob
		coords.add(coord);
		// sets initial values of the blob, given that there is only one pixel
		center = new Coordinate(coords.get(coords.size() / 2).getX(), coords.get(coords.size() / 2).getY() );
		
		minBounds = new Coordinate(coord.getX(), coord.getY());
		maxBounds = new Coordinate(coord.getX(), coord.getY());

		width = 1;
		height = 1;
		update();
	}

	/**
	 * This determines if the pixel is a part of the blob or not.
	 * @param p Pixel to be tested.
	 * @return Boolean whether it is or not.
	 */
	protected boolean isPartOf(Coordinate p) {
		// creates a larger rectangle around the edges of the blob and sees if the 
		// pixel is within that rectangle
		return (minBounds.getX() - (TOLERANCE) <= p.getX() && p.getX() <= maxBounds.getX() + (TOLERANCE)
				&& minBounds.getY() - (TOLERANCE) <= p.getY() && p.getY() <= maxBounds.getY() + (TOLERANCE));
	}

	/**
	 * This determines if the blob already contains the pixel.
	 * @param p Pixel being tested.
	 * @return Boolean whether it is or not.
	 */
	protected boolean contains(Coordinate p) {
		return coords.contains(p);
	}

	/**
	 * This clears the blob of all the pixels.
	 */
	protected void clear() {
		coords.clear();
	}

	/**
	 * This updates the size of the blob.
	 */
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

	/**
	 * This updates the density of the blob.
	 */
	private void updateDensity() {
		// calculations done by dividing the amount of coords in the blob by the area of
		// the blob
		//System.out.println("min = (" + minBounds.getX() + ", " + minBounds.getY() +
		//		") max = (" + maxBounds.getX() + ", " + maxBounds.getY() + 
		//		") width = " + width + " height = " + height + " area = " + area);

		density = ( (coords.size() * (Math.pow(Main.pixelsToSkip, 2) ) ) / (area) );
	}

	/**
	 * This adds the pixel to the blob.
	 * @param p Pixel that is to be added.
	 */
	protected void addToBlob(Coordinate p) {
		// adds into the Coordinates arrayList of the blob
		coords.add(p);
		// updates the center of the blob due to the added pixel
		center.setXY(coords.get(coords.size() / 2).getX(), 
					 coords.get(coords.size() / 2).getY());

		// updates the min and max bounds by seeing if the new coord is outside 
		// any of these bounds
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

	/**
	 * Returns the width of the blob.
	 * @return Width.
	 */
	protected float getWidth() {
		return width;
	}

	/**
	 * Returns the height of the blob.
	 * @return Height.
	 */
	protected float getHeight() {
		return height;
	}

	/**
	 * Sets the blob to be deleted.
	 */
	protected void setDeleted() {
		deleted = true;
	}

	/**
	 * Returns whether the blob is set to be deleted or not.
	 * @return Boolean whether blob is to be deleted or not.
	 */
	protected boolean getDeleted() {
		return deleted;
	}

	/**
	 * Updates the blob's data.
	 */
	private void update() {
		updateSize();
		updateDensity();
	}
	
	/**
	 * Returns the top left coordinate.
	 * @return Coordinate of top left corner.
	 */
	public Coordinate getMin() {
		return minBounds;
	}
	
	/**
	 * Returns the bottom right coordinate.
	 * @return Coordinate of bottom right corner.
	 */
	public Coordinate getMax() {
		return maxBounds;
	}
	
	/**
	 * Returns the density of the blob.
	 * @return Density.
	 */
	protected double getDensity() {
		return density;
	}
	
	/**
	 * Returns the area of the blob.
	 * @return Area.
	 */
	protected double getArea() {
		return area;
	}
	
	/**
	 * Returns all the coordinates in the blob.
	 * @return ArrayList of all the coordinates.
	 */
	protected ArrayList<Coordinate> getCoordinates() {
		return coords;
	}

}
