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
	ArrayList<Coordinate> coords = new ArrayList<Coordinate>();
	// size of the blob
	int width;
	int height;
	// the blob's center location
	Coordinate center;
	// used to delete blobs when merging two blobs
	private boolean deleted = false;
	// coordinates of the top left and bottom right corner
	// used to draw the rectangle
	private Coordinate minBounds;
	private Coordinate maxBounds;
	// coord density of the blob
	private float density;
	// area of the blob
	private float area;
	// the maximum distance of the coord in question to be still considered a part
	// of the blob
	// used to determine if the coord is part of a new blob or this blob
	private final int TOLERANCE = Main.PIXELS_TO_SKIP * 2;

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

	boolean isPartOf(Coordinate p) {

		// creates a larger rectangle around the edges of the blob and sees if the coord
		// is within that rectangle
		return (minBounds.getX() - (TOLERANCE) <= p.getX() && p.getX() <= maxBounds.getX() + (TOLERANCE)
				&& minBounds.getY() - (TOLERANCE) <= p.getY() && p.getY() <= maxBounds.getY() + (TOLERANCE));

	}

	// method to see if the coord in question is already contained within the blob
	// takes in a coord and returns a booelan
	boolean contains(Coordinate p) {
		return coords.contains(p);
	}

	// clears all coords in the blob
	void clear() {
		coords.clear();
	}

	// updates the size of the blob
	private void updateSize() {
		// calculations for size based on top left and bottom right corner
		width = maxBounds.getX() - minBounds.getX();
		height = maxBounds.getY() - minBounds.getY();

		// println("Area: " + size.x * size.y +", Coordinate Length: "+ coords.size() + ",
		// Density: " + density);
	}

	// updates the coord density of the blob
	void updateDensity() {
		// calculations done by dividing the amount of coords in the blob by the area of
		// the blob
		area = width * height;
		density = ((coords.size() * (2 * Main.PIXELS_TO_SKIP)) / (area));
	}

	// adds the coord to the blob
	// takes in the coord that is to be added
	void addToBlob(Coordinate p) {
		// may not be needed
		// if (this.contains(p)) {
		// return;
		// }

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
	void show() {
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
	float getWidth() {
		return maxBounds.getX() - minBounds.getX();
	}

	// returns the height of the rect
	float getHeight() {
		return maxBounds.getY() - minBounds.getY();
	}

	// set the blob to be deleted
	void setDeleted() {
		deleted = true;
	}

	// returns whether the blob is set to be deleted
	boolean getDeleted() {
		return deleted;
	}

	// updates other properties of the blob
	void update() {
		updateSize();
		updateDensity();
	}

}

/*
 * 
 * //this class creates the blobs that store information about the blob class
 * Blob { //stores all the coords that are a part of this blob ArrayList <Coordinate>
 * coords = new ArrayList<Coordinate>(); //size of the blob PVector size; //the
 * blob's center location PVector center; //used to delete blobs when merging
 * two blobs private boolean deleted = false; //coordinates of the top left and
 * bottom right corner //used to draw the rectangle private PVector minBounds;
 * private PVector maxBounds; //coord density of the blob private float density;
 * //area of the blob private float area; //the maximum distance of the coord in
 * question to be still considered a part of the blob //used to determine if the
 * coord is part of a new blob or this blob private final int TOLERANCE = int
 * (coordsToSkip * 2);
 * 
 * //constructor for blobs //pass in the initial coord of the blob Blob(Coordinate p)
 * { //add given coord into the array of coords in this blob coords.add(p);
 * //sets initial values of the blob, given that there is only one coord center
 * = coords.get(coords.size()/2).pos; minBounds = new PVector(p.pos.x, p.pos.y);
 * maxBounds = new PVector(p.pos.x, p.pos.y); size = new PVector(1, 1); }
 * 
 * 
 * //method determines if the coord in question is a part of this blob or not
 * //takes in a coord and returns a boolean boolean isPartOf (Coordinate p) {
 * 
 * //if (this.contains(p)) { //return false; //} //Coordinate topLeft =
 * coords.get(0); //Coordinate bottomRight = coords.get(coords.size()-1); //double
 * dist = dist(center.x, center.y, p.pos.x, p.pos.y); //println("dist = " + dist
 * + " top left = " + topLeft.pos + " bottom right = " + bottomRight.pos);
 * //return dist < 5 * ((dist(bottomRight.pos.x,bottomRight.pos.y,
 * topLeft.pos.x, topLeft.pos.y))); //return dist<60;
 * 
 * //updateSize(); //int errorRange = 5;
 * 
 * //creates a larger rectangle around the edges of the blob and sees if the
 * coord is within that rectangle return (minBounds.x-(TOLERANCE) <= p.pos.x &&
 * p.pos.x <= maxBounds.x+(TOLERANCE) && minBounds.y-(TOLERANCE) <= p.pos.y &&
 * p.pos.y <= maxBounds.y+(TOLERANCE));
 * 
 * /* return (minBounds.x-( (size.x/5)+5) <= p.pos.x && p.pos.x <= maxBounds.x+(
 * (size.x/5)+5) && minBounds.y-( (size.y/5)+5) <= p.pos.y && p.pos.y <=
 * maxBounds.y+( (size.y/5+5) ) );
 */ /*
	 * }
	 * 
	 * //method to see if the coord in question is already contained within the blob
	 * //takes in a coord and returns a booelan boolean contains(Coordinate p) { return
	 * coords.contains(p); }
	 * 
	 * //clears all coords in the blob void clear() { coords.clear(); }
	 * 
	 * //updates the size of the blob private void updateSize() { //Coordinate topLeft =
	 * coords.get(0); //Coordinate bottomRight = coords.get(coords.size()-1);
	 * 
	 * //size.x = bottomRight.pos.x - topLeft.pos.x; //size.y = bottomRight.pos.y -
	 * topLeft.pos.y;
	 * 
	 * //calculations for size based on top left and bottom right corner size.x =
	 * maxBounds.x - minBounds.x; size.y = maxBounds.y - minBounds.y;
	 * 
	 * //println("Area: " + size.x * size.y +", Coordinate Length: "+ coords.size() +
	 * ", Density: " + density); }
	 * 
	 * //updates the coord density of the blob void updateDensity() { //calculations
	 * done by dividing the amount of coords in the blob by the area of the blob
	 * area = size.x * size.y; density = ((coords.size()*(2*coordsToSkip)) /
	 * (area)); }
	 * 
	 * //adds the coord to the blob //takes in the coord that is to be added void
	 * addToBlob(Coordinate p) { //may not be needed //if (this.contains(p)) { //return;
	 * //}
	 * 
	 * //adds into the coord array of the blob coords.add(p); //updates the center
	 * of the blob due to the added coord center = coords.get(coords.size()/2).pos;
	 * 
	 * //updates the min and max bounds by seeing if the new coord is outside any of
	 * these bounds if (p.pos.x < minBounds.x) { minBounds.x = p.pos.x; } if
	 * (p.pos.y < minBounds.y) { minBounds.y = p.pos.y; } if (p.pos.x > maxBounds.x)
	 * { maxBounds.x = p.pos.x; } if (p.pos.y > maxBounds.y) { maxBounds.y =
	 * p.pos.y; } //calls update function to update other properties of the blob
	 * update(); }
	 * 
	 * //this method draws the rectange around the blob void show() { //Coordinate
	 * topLeft = coords.get(0); //Coordinate bottomRight = coords.get(coords.size()-1);
	 * // fill(255 - (255*density)); noFill(); stroke(0); if
	 * (VisionProcessor.runType == RunType.PC) { //Coordinate topLeft = coords.get(0);
	 * //Coordinate bottomRight = coords.get(coords.size()-1); // fill(255 -
	 * (255*density)); //draws a rectangle on the blank screen and on top of the
	 * camara image rect(minBounds.x, minBounds.y, maxBounds.x, maxBounds.y);
	 * rect(minBounds.x + width/2, minBounds.y, maxBounds.x + width/2, maxBounds.y);
	 * } else if (VisionProcessor.runType == RunType.ANDROID) { //unfinished...
	 * PVector shiftedMinPos = new PVector(minBounds.y + width/2 -
	 * capture.getCurrentImage().height/2, minBounds.x + height/2 -
	 * capture.getCurrentImage().width/2); PVector shiftedMaxPos = new
	 * PVector(maxBounds.y + width/2 - capture.getCurrentImage().height/2,
	 * maxBounds.x + height/2 - capture.getCurrentImage().width/2); pushMatrix();
	 * rectMode(CENTER); translate(((shiftedMinPos.x+getRealWidth()/2)),
	 * (shiftedMinPos.y+getRealHeight()/2)); //rotate(HALF_PI);
	 * rect(0,0,getRealWidth(), getRealHeight()); rectMode(CORNERS); //);
	 * popMatrix(); //rect(shiftedMinPos.x, shiftedMinPos.y, shiftedMaxPos.x,
	 * shiftedMaxPos.y); text(getWidth() + "x" + getHeight(), 20, height*0.8); //
	 * rect(minBounds.x + width/2, minBounds.y, maxBounds.x + width/2, maxBounds.y);
	 * } }
	 * 
	 * //overload of previous function for debug purposes //makes the rectangles
	 * different colours to differentiate different blobs void show(int red, int
	 * green, int blue) { //Coordinate topLeft = coords.get(0); //Coordinate bottomRight =
	 * coords.get(coords.size()-1); // fill(255 - (255*density)); //noFill();
	 * stroke(red, green, blue); rect(minBounds.x, minBounds.y, maxBounds.x,
	 * maxBounds.y); rect(minBounds.x + width/2, minBounds.y, maxBounds.x + width/2,
	 * maxBounds.y); stroke(0); }
	 * 
	 * //returns the width of the rect float getWidth() { return maxBounds.x -
	 * minBounds.x; } //returns the height of the rect float getHeight() { return
	 * maxBounds.y - minBounds.y; }
	 * 
	 * //the image on the android cam is rotated so the width and height must be
	 * switched float getRealWidth() { return maxBounds.y - minBounds.y; } float
	 * getRealHeight() { return maxBounds.x - minBounds.x; }
	 * 
	 * //set the blob to be deleted void setDeleted() { deleted = true; }
	 * 
	 * //returns whether the blob is set to be deleted boolean getDeleted() { return
	 * deleted; }
	 * 
	 * //updates other properties of the blob void update() { updateSize();
	 * updateDensity(); }
	 * 
	 * //changes x and y coordinates //used in android mode due to the image being
	 * rotated PVector swapVector(PVector pos) { return new PVector(pos.y, pos.x); }
	 * }
	 * 
	 */