package cosmicRayDetector;
/**
 * 
 */

/**
 * @author WinnieTrandinh
 *
 */
public class Coordinate {

	/**
	 * 
	 */
	
	private int x;
	private int y;
	
	public Coordinate(int _x, int _y) {
		x = _x;
		y = _y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setX(int _x) {
		x = _x;
	}
	
	public void setY(int _y) {
		y = _y;
	}
	
	public void setXY(int x, int y) {
		//System.out.println("coordiantes are " + x + ", " + y);
		setX(x);
		//System.out.println("x set to " + x);
		setY(y);
		//System.out.println("y set to " + y);
	}

}
