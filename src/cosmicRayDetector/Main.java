package cosmicRayDetector;
/**
 * 
 */

/**
 * @author WinnieTrandinh
 *
 */
public class Main {

	/**
	 * 
	 */
	private static String imagePath = "/images/testImage03.png";
	private static int threshold = 100;
	private static final double MIN_DENSITY = 0.40;
	private static final double MIN_AREA = 100;
	public static final int PIXELS_TO_SKIP = 1; 
	
	private static Screen screen = new Screen(200, 200, imagePath);
	private static Detector detector = new Detector(threshold, PIXELS_TO_SKIP,
													MIN_DENSITY, MIN_AREA);

		
	public Main() {

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//Screen screen = new Screen(200, 200);
		screen.update();
		detector.findPixels();
		//screen.draw();
		//screen.foo();
		
		
	}

}
