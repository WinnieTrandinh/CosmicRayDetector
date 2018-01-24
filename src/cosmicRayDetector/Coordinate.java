package cosmicRayDetector;

/**
 * This object stores the x and y vales of the pixel.
 * @author WinnieTrandinh
 *
 */
public class Coordinate {
	//coordinates of the pixel
	private int x;
	private int y;
	
	/**
	 * Constructor for the class.
	 * @param _x X coordinate.
	 * @param _y Y coordinate.
	 */
	public Coordinate(int _x, int _y) {
		x = _x;
		y = _y;
	}
	
	/**
	 * Returns the x value.
	 * @return X coordinate.
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Returns the y value.
	 * @return Y coordinate.
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Sets the x value to a specified value.
	 * @param _x X value to be set to.
	 */
	public void setX(int _x) {
		x = _x;
	}
	
	/**
	 * Sets the y value to a specified value.
	 * @param _y Y value to be set to.
	 */
	public void setY(int _y) {
		y = _y;
	}
	
	/**
	 * Sets the x and y values to specified values.
	 * @param x X value to be set to.
	 * @param y Y value to be set to.
	 */
	public void setXY(int x, int y) {
		setX(x);
		setY(y);
	}

}
